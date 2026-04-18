package visuals;

import core.*;
import services.VideoEncodingService;

import java.nio.file.Path;

public class SpriteGenerator extends Stage {

    private final VideoEncodingService service;

    public SpriteGenerator(VideoEncodingService service) { this.service = service; }

    @Override public String title()        { return "SpriteGenerator"; }
    @Override public PipelineState phase() { return PipelineState.RENDERING_VISUALS; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path imagesRoot = ctx.bundleDir().resolve("images");
        try {
            Path sprite = service.buildSpriteMap(ctx.source(), imagesRoot);
            return StageResult.success(sprite.getFileName().toString());
        } catch (Exception e) {
            return StageResult.failure("Failed to generate sprite map", e);
        }
    }
}