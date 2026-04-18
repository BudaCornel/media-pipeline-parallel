package analysis;

import core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SceneIndexer extends Stage {
    @Override public String title()        { return "SceneIndexer"; }
    @Override public PipelineState phase() { return PipelineState.ANALYSING; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        List<Map<String, Object>> index = new ArrayList<>();
        index.add(Map.of("label", "establishing_shot", "from", 0,    "to", 135));
        index.add(Map.of("label", "dialogue",          "from", 135,  "to", 600));
        index.add(Map.of("label", "action",            "from", 600,  "to", 2505));
        index.add(Map.of("label", "dialogue",          "from", 2505, "to", 2610));
        ctx.put("sceneIndex", index);
        return StageResult.success(index.size() + " segments");
    }
}