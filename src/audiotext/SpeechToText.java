package audiotext;

import core.*;
import services.TimedTextService;

import java.nio.file.Files;
import java.nio.file.Path;

public class SpeechToText extends Stage {

    private final TimedTextService service;

    public SpeechToText(TimedTextService service) { this.service = service; }

    @Override public String title()        { return "SpeechToText"; }
    @Override public PipelineState phase() { return PipelineState.LOCALISING_TEXT_AUDIO; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path textDir = ctx.bundleDir().resolve("text");
        try {
            Files.createDirectories(textDir);
            String transcript = service.englishTranscript();
            Files.writeString(textDir.resolve("source_transcript.txt"), transcript);
            ctx.put("transcript.en", "text/source_transcript.txt");
            return StageResult.success(transcript.lines().count() + " lines");
        } catch (Exception e) {
            return StageResult.failure("could not write transcript", e);
        }
    }
}