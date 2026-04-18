package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class VideoEncodingService {

    private static final String[][] VARIANTS = {
            {"h264", "mp4"},
            {"vp9",  "webm"},
            {"hevc", "mkv"}
    };
    private static final String[] SIZES = {"4k", "1080p", "720p"};

    public void layoutVariantTree(Path videoRoot) throws Exception {
        for (String[] v : VARIANTS) {
            Path codecDir = videoRoot.resolve(v[0]);
            Files.createDirectories(codecDir);
            for (String size : SIZES) {
                Path file = codecDir.resolve(size + "_" + v[0] + "." + v[1]);
                if (!Files.exists(file)) Files.createFile(file);
            }
        }
    }

    public Path encodeCanonical(Path source, Path videoRoot) throws Exception {
        Path target = videoRoot.resolve("h264").resolve("1080p_h264.mp4");
        Files.deleteIfExists(target);

        runFfmpeg(Arrays.asList(
                "ffmpeg", "-hide_banner", "-loglevel", "error", "-y",
                "-i", source.toString(),
                "-vf", "scale=1920:1080",
                "-c:v", "libx264", "-preset", "ultrafast", "-crf", "28",
                "-c:a", "aac", "-b:a", "128k",
                target.toString()
        ));
        return target;
    }

    public Path buildSpriteMap(Path source, Path imagesRoot) throws Exception {
        Path sprite = imagesRoot.resolve("sprite_map.jpg");
        Files.createDirectories(imagesRoot.resolve("thumbnails"));
        runFfmpeg(Arrays.asList(
                "ffmpeg", "-hide_banner", "-loglevel", "error", "-y",
                "-i", source.toString(),
                "-vf", "fps=1/2,scale=160:90,tile=4x4",
                "-frames:v", "1",
                sprite.toString()
        ));
        return sprite;
    }

    private void runFfmpeg(java.util.List<String> cmd) throws Exception {
        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while (r.readLine() != null) {  }
        }
        int exit = p.waitFor();
        if (exit != 0) throw new RuntimeException("ffmpeg exit " + exit + " for " + String.join(" ", cmd));
    }
}