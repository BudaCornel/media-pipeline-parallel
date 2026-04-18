package visuals;

import core.*;
import services.VideoEncodingService;

import java.nio.file.Path;

public class Transcoder extends Stage {

    private final VideoEncodingService service;

    public Transcoder(VideoEncodingService service) { this.service = service; }

    @Override public String title()        { return "Transcoder"; }
    @Override public PipelineState phase() { return PipelineState.RENDERING_VISUALS; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path videoRoot = ctx.bundleDir().resolve("video");
        try {
            service.layoutVariantTree(videoRoot);
            Path canonical = service.encodeCanonical(ctx.source(), videoRoot);
            ctx.put("canonicalRendition", "video/h264/1080p_h264.mp4");
            return StageResult.success("1 real + 8 placeholders (" + canonical.getFileName() + ")");
        } catch (Exception e) {
            return StageResult.failure("transcode failed", e);
        }
    }
}