package core;


public abstract class Stage {

    public abstract String title();

    public abstract PipelineState phase();

    protected abstract StageResult doWork(PipelineContext ctx);

    public final StageResult invoke(PipelineContext ctx) {
        try {
            return doWork(ctx);
        } catch (Exception e) {
            return StageResult.failure("uncaught exception in " + title(), e);
        }
    }
}