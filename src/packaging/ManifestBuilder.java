package packaging;

import core.*;
import services.PackagingService;

import java.nio.file.Path;

public class ManifestBuilder extends Stage {

    private final PackagingService service;

    public ManifestBuilder(PackagingService service) { this.service = service; }

    @Override public String title()        { return "ManifestBuilder"; }
    @Override public PipelineState phase() { return PipelineState.PACKAGING_BUNDLE; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        try {
            Path manifest = service.writeManifest(ctx);
            return StageResult.success(manifest.getFileName().toString());
        } catch (Exception e) {
            return StageResult.failure("manifest write failed", e);
        }
    }
}