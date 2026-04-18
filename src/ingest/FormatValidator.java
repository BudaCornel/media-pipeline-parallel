package ingest;

import core.PipelineContext;
import core.PipelineState;
import core.Stage;
import core.StageResult;

import java.util.Arrays;
import java.util.List;

public class FormatValidator extends Stage {

    private static final List<String> SUPPORTED =
            Arrays.asList("mp4", "mov", "mkv", "webm", "m4v");

    @Override public String title()        { return "FormatValidator"; }
    @Override public PipelineState phase() { return PipelineState.INGESTING; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        String filename = ctx.source().getFileName().toString();
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1)
            return StageResult.failure("no extension: " + filename, null);

        String ext = filename.substring(dot + 1).toLowerCase();
        if (!SUPPORTED.contains(ext))
            return StageResult.failure("unsupported container: ." + ext, null);

        ctx.setContainerExt(ext);
        return StageResult.success("container=." + ext);
    }
}