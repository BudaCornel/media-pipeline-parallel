package compliance;

import core.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegionalBranding extends Stage {
    @Override public String title()        { return "RegionalBranding"; }
    @Override public PipelineState phase() { return PipelineState.CHECKING_COMPLIANCE; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Map<String, String> byRegion = new LinkedHashMap<>();
        byRegion.put("NA",    "NetflixOriginal_NA");
        byRegion.put("EMEA",  "NetflixOriginal_EMEA");
        byRegion.put("APAC",  "NetflixOriginal_APAC");
        byRegion.put("LATAM", "NetflixOriginal_LATAM");
        ctx.put("branding.byRegion", byRegion);
        return StageResult.success(byRegion.size() + " regions");
    }
}