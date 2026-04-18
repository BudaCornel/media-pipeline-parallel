package compliance;

import core.*;

import java.util.List;
import java.util.Map;

public class SafetyScanner extends Stage {
    @Override public String title()        { return "SafetyScanner"; }
    @Override public PipelineState phase() { return PipelineState.CHECKING_COMPLIANCE; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        List<Map<String, Object>> directives = List.of(
                Map.of("atSeconds", 142, "action", "blur", "regions", List.of("IN", "AE"))
        );
        ctx.put("safety.directives", directives);
        return StageResult.success(directives.size() + " directive(s)");
    }
}