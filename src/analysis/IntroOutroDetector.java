package analysis;

import core.*;

public class IntroOutroDetector extends Stage {
    @Override public String title()        { return "IntroOutroDetector"; }
    @Override public PipelineState phase() { return PipelineState.ANALYSING; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        ctx.put("intro.endsAt",   85);
        ctx.put("outro.startsAt", 2505);
        return StageResult.success("intro @ 00:01:25, outro @ 00:41:45");
    }
}