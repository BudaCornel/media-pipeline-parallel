package audiotext;

import core.*;
import services.TimedTextService;

import java.nio.file.Files;
import java.nio.file.Path;

public class Translator extends Stage {

    private final TimedTextService service;

    public Translator(TimedTextService service) { this.service = service; }

    @Override public String title()        { return "Translator"; }
    @Override public PipelineState phase() { return PipelineState.LOCALISING_TEXT_AUDIO; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path textDir = ctx.bundleDir().resolve("text");
        try {
            Files.writeString(textDir.resolve("ro_translation.txt"), service.romanianTranslation());
            ctx.put("transcript.ro", "text/ro_translation.txt");
            return StageResult.success("ro translation");
        } catch (Exception e) {
            return StageResult.failure("could not write ro translation", e);
        }
    }
}