package core;

import java.util.EnumMap;
import java.util.Map;


public final class StateTransitions {

    private static final Map<PipelineState, PipelineState> FORWARD =
            new EnumMap<>(PipelineState.class);

    static {
        FORWARD.put(PipelineState.AWAITING_MASTER,       PipelineState.INGESTING);
        FORWARD.put(PipelineState.INGESTING,             PipelineState.ANALYSING);
        FORWARD.put(PipelineState.ANALYSING,             PipelineState.RENDERING_VISUALS);
        FORWARD.put(PipelineState.RENDERING_VISUALS,     PipelineState.LOCALISING_TEXT_AUDIO);
        FORWARD.put(PipelineState.LOCALISING_TEXT_AUDIO, PipelineState.CHECKING_COMPLIANCE);
        FORWARD.put(PipelineState.CHECKING_COMPLIANCE,   PipelineState.PACKAGING_BUNDLE);
        FORWARD.put(PipelineState.PACKAGING_BUNDLE,      PipelineState.DELIVERED);
    }

    private StateTransitions() {}

    public static PipelineState next(PipelineState current) {
        PipelineState n = FORWARD.get(current);
        if (n == null)
            throw new IllegalStateException("No forward transition from " + current);
        return n;
    }

    public static boolean canAdvance(PipelineState current) {
        return FORWARD.containsKey(current);
    }
}