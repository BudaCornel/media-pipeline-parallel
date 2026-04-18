package core;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PipelineContext {

    private final String titleId;
    private final Path source;
    private final Path outputRoot;
    private final Map<String, Object> attrs = new ConcurrentHashMap<>();

    private volatile PipelineState state = PipelineState.AWAITING_MASTER;
    private volatile String sourceDigest = "";
    private volatile String containerExt = "";
    private volatile boolean drmSealed = false;

    public PipelineContext(String titleId, Path source, Path outputRoot) {
        this.titleId = titleId;
        this.source = source;
        this.outputRoot = outputRoot;
    }

    public String titleId()   { return titleId; }
    public Path source()      { return source; }
    public Path outputRoot()  { return outputRoot; }
    public Path bundleDir()   { return outputRoot.resolve(titleId); }

    public PipelineState state()                  { return state; }
    public void transitionTo(PipelineState next)  { this.state = next; }

    public String sourceDigest()          { return sourceDigest; }
    public void setSourceDigest(String s) { this.sourceDigest = s; }

    public String containerExt()          { return containerExt; }
    public void setContainerExt(String e) { this.containerExt = e; }

    public boolean drmSealed()   { return drmSealed; }
    public void sealDrm()        { this.drmSealed = true; }

    public void put(String key, Object value) { attrs.put(key, value); }
    public Object get(String key)             { return attrs.get(key); }
    public Map<String, Object> attrs()        { return attrs; }
}