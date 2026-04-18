import analysis.CreditRoller;
import analysis.IntroOutroDetector;
import analysis.SceneIndexer;
import audiotext.AIDubber;
import audiotext.SpeechToText;
import audiotext.Translator;
import compliance.RegionalBranding;
import compliance.SafetyScanner;
import core.PipelineContext;
import core.PipelineLogger;
import core.PipelineState;
import core.WorkflowOrchestrator;
import ingest.FormatValidator;
import ingest.IntegrityCheck;
import packaging.DRMWrapper;
import packaging.ManifestBuilder;
import services.ComplexityAnalysisService;
import services.PackagingService;
import services.TimedTextService;
import services.VideoEncodingService;
import visuals.SceneComplexity;
import visuals.SpriteGenerator;
import visuals.Transcoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Path source = args.length > 0 ? Paths.get(args[0]) : Paths.get("assets", "master.mp4");
        Path out    = args.length > 1 ? Paths.get(args[1]) : Paths.get("output");

        if (!Files.isRegularFile(source)) {
            PipelineLogger.info("master file not found at " + source.toAbsolutePath());
            PipelineLogger.info("create one with:");
            PipelineLogger.info("  ffmpeg -f lavfi -i testsrc=duration=5:size=854x480:rate=25 \\");
            PipelineLogger.info("         -f lavfi -i sine=frequency=440:duration=5 \\");
            PipelineLogger.info("         -c:v libx264 -pix_fmt yuv420p -c:a aac assets/master.mp4");
            System.exit(2);
        }

        ComplexityAnalysisService complexity = new ComplexityAnalysisService();
        VideoEncodingService      encoding   = new VideoEncodingService();
        TimedTextService          timedText  = new TimedTextService();
        PackagingService          packaging  = new PackagingService();

        PipelineContext ctx = new PipelineContext("movie_101", source, out);

        WorkflowOrchestrator workflow = new WorkflowOrchestrator()

                .register(new IntegrityCheck())
                .register(new FormatValidator())

                .register(new IntroOutroDetector())
                .register(new CreditRoller())
                .register(new SceneIndexer())
                .register(new SceneComplexity(complexity))
                .register(new Transcoder(encoding))
                .register(new SpriteGenerator(encoding))

                .register(new SpeechToText(timedText))
                .register(new Translator(timedText))
                .register(new AIDubber(timedText))

                .register(new SafetyScanner())
                .register(new RegionalBranding())

                .register(new DRMWrapper(packaging))
                .register(new ManifestBuilder(packaging));

        workflow.run(ctx);

        if (ctx.state() != PipelineState.DELIVERED) {
            System.exit(1);
        }
    }
}