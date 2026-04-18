package services;

public final class TimedTextService {

    private static final String EN = """
        [00:00:00] (theme music)
        [00:00:12] — Initiating handoff protocol.
        [00:00:18] — Confirm telemetry before stage three.
        [00:00:24] — Copy. Uplink stable.
        [00:00:29] (transition tone)
        """;

    private static final String RO = """
        [00:00:00] (muzică de generic)
        [00:00:12] — Se inițiază protocolul de predare.
        [00:00:18] — Confirmă telemetria înainte de etapa trei.
        [00:00:24] — Recepționat. Uplink stabil.
        [00:00:29] (ton de tranziție)
        """;

    public String englishTranscript() { return EN; }
    public String romanianTranslation() { return RO; }
    public byte[] romanianDubStub()   { return new byte[0]; }
}