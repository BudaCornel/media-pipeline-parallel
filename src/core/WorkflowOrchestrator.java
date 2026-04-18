package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public final class WorkflowOrchestrator {

    private final List<Stage> stages = new ArrayList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public WorkflowOrchestrator register(Stage stage) {
        stages.add(stage);
        return this;
    }

    public void run(PipelineContext ctx) {
        PipelineLogger.banner("Workflow start: " + ctx.titleId());
        PipelineLogger.info("initial state = " + ctx.state());
        PipelineLogger.info("source=" + ctx.source() + "  out=" + ctx.outputRoot());

        ctx.transitionTo(StateTransitions.next(ctx.state()));

        Map<PipelineState, List<Stage>> groupedStages = stages.stream()
                .collect(Collectors.groupingBy(Stage::phase, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<PipelineState, List<Stage>> entry : groupedStages.entrySet()) {
            PipelineState currentPhase = entry.getKey();
            List<Stage> phaseStages = entry.getValue();

            if (currentPhase != ctx.state()) {
                PipelineLogger.fail("FSM guard", "stage expects " + currentPhase + ", context is " + ctx.state());
                ctx.transitionTo(PipelineState.ABORTED);
                executor.shutdown();
                return;
            }

            List<CompletableFuture<StageResult>> futures = phaseStages.stream()
                    .map(stage -> CompletableFuture.supplyAsync(() -> {
                        long t0 = System.nanoTime();
                        StageResult result = stage.invoke(ctx);
                        long ms = (System.nanoTime() - t0) / 1_000_000;

                        if (result.isOk()) {
                            PipelineLogger.ok(stage.title(), result.summary(), ms);
                        } else {
                            PipelineLogger.fail(stage.title(), result.summary());
                            if (result.cause() != null) result.cause().printStackTrace(System.err);
                        }
                        return result;
                    }, executor))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            boolean phaseFailed = futures.stream()
                    .map(CompletableFuture::join)
                    .anyMatch(result -> !result.isOk());

            if (phaseFailed) {
                ctx.transitionTo(PipelineState.ABORTED);
                PipelineLogger.banner("Workflow ABORTED in " + currentPhase);
                executor.shutdown();
                return;
            }

            if (StateTransitions.canAdvance(ctx.state())) {
                PipelineState before = ctx.state();
                PipelineState after  = StateTransitions.next(before);
                ctx.transitionTo(after);
                PipelineLogger.info("state: " + before + " --> " + after);
            }
        }

        PipelineLogger.banner("Workflow DELIVERED: " + ctx.titleId());

        executor.shutdown();
    }
}