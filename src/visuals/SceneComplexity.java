package visuals;

import core.*;
import services.ComplexityAnalysisService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SceneComplexity extends Stage {

    private final ComplexityAnalysisService service;

    public SceneComplexity(ComplexityAnalysisService service) { this.service = service; }

    @Override public String title()        { return "SceneComplexity"; }
    @Override public PipelineState phase() { return PipelineState.RENDERING_VISUALS; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        try {
            Map<String, String> probe = service.probe(ctx.source());
            double score = service.complexityScore(probe);
            ctx.put("ffprobe", probe);
            ctx.put("complexity.score", score);

            Path metaDir = ctx.bundleDir().resolve("metadata");
            Files.createDirectories(metaDir);
            Files.writeString(metaDir.resolve("scene_analysis.json"), renderJson(probe, score));

            return StageResult.success("score=" + String.format("%.2f", score));
        } catch (Exception e) {
            return StageResult.failure("ffprobe failed", e);
        }
    }

    private String renderJson(Map<String, String> probe, double score) {
        StringBuilder sb = new StringBuilder("{\n");
        for (var e : probe.entrySet()) {
            sb.append("  \"").append(e.getKey()).append("\": \"")
                    .append(e.getValue()).append("\",\n");
        }
        sb.append("  \"complexityScore\": ").append(String.format("%.4f", score)).append("\n}");
        return sb.toString();
    }
}