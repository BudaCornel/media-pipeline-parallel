package packaging;

import core.*;
import services.PackagingService;

public class DRMWrapper extends Stage {

    private final PackagingService service;

    public DRMWrapper(PackagingService service) { this.service = service; }

    @Override public String title()        { return "DRMWrapper"; }
    @Override public PipelineState phase() { return PipelineState.PACKAGING_BUNDLE; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        service.sealDrm(ctx);
        return StageResult.success("sealed");
    }
}