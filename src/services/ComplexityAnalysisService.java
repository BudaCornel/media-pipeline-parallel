package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ComplexityAnalysisService {

    public Map<String, String> probe(Path source) throws Exception {
        Process p = new ProcessBuilder(Arrays.asList(
                "ffprobe", "-hide_banner", "-loglevel", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height,avg_frame_rate,r_frame_rate,codec_name",
                "-of", "default=nw=1",
                source.toString()
        )).redirectErrorStream(true).start();

        Map<String, String> result = new LinkedHashMap<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                int eq = line.indexOf('=');
                if (eq > 0) result.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
        }
        int exit = p.waitFor();
        if (exit != 0) throw new RuntimeException("ffprobe exit " + exit);
        return result;
    }

    public double complexityScore(Map<String, String> probe) {
        int w = parseInt(probe.get("width"),  1280);
        int h = parseInt(probe.get("height"), 720);
        return Math.min(1.0, (w * h) / (double) (3840 * 2160));
    }

    private static int parseInt(String s, int fallback) {
        try { return s == null ? fallback : Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return fallback; }
    }
}