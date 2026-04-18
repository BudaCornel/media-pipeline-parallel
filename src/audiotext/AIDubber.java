package audiotext;

import core.*;
import services.TimedTextService;

import java.nio.file.Files;
import java.nio.file.Path;

public class AIDubber extends Stage {

    private final TimedTextService service;

    public AIDubber(TimedTextService service) { this.service = service; }

    @Override public String title()        { return "AIDubber"; }
    @Override public PipelineState phase() { return PipelineState.LOCALISING_TEXT_AUDIO; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path audioDir = ctx.bundleDir().resolve("audio");
        try {
            Files.createDirectories(audioDir);
            Path dub = audioDir.resolve("ro_dub_synthetic.aac");
            Files.write(dub, service.romanianDubStub());
            ctx.put("dub.ro", "audio/ro_dub_synthetic.aac");
            return StageResult.success("ro dub placeholder");
        } catch (Exception e) {
            return StageResult.failure("could not write dub", e);
        }
    }
}