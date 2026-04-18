package core;

public final class StageResult {

    private final boolean ok;
    private final String summary;
    private final Throwable cause;

    private StageResult(boolean ok, String summary, Throwable cause) {
        this.ok = ok;
        this.summary = summary;
        this.cause = cause;
    }

    public static StageResult success(String summary) {
        return new StageResult(true, summary, null);
    }

    public static StageResult failure(String summary, Throwable cause) {
        return new StageResult(false, summary, cause);
    }

    public boolean isOk()    { return ok; }
    public String summary()  { return summary; }
    public Throwable cause() { return cause; }
}