package core;

public enum PipelineState {
    AWAITING_MASTER,
    INGESTING,
    ANALYSING,
    RENDERING_VISUALS,
    LOCALISING_TEXT_AUDIO,
    CHECKING_COMPLIANCE,
    PACKAGING_BUNDLE,
    DELIVERED,
    ABORTED;

    public boolean isTerminal() {
        return this == DELIVERED || this == ABORTED;
    }
}