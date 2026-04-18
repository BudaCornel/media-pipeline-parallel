package analysis;

import core.*;

public class CreditRoller extends Stage {
    @Override public String title()        { return "CreditRoller"; }
    @Override public PipelineState phase() { return PipelineState.ANALYSING; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        ctx.put("creditRoll.startsAt", 2610);
        return StageResult.success("watchNext @ 00:43:30");
    }
}