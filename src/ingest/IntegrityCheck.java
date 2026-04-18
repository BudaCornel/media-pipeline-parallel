package ingest;

import core.PipelineContext;
import core.PipelineState;
import core.Stage;
import core.StageResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class IntegrityCheck extends Stage {

    @Override public String title()        { return "IntegrityCheck"; }
    @Override public PipelineState phase() { return PipelineState.INGESTING; }

    @Override
    protected StageResult doWork(PipelineContext ctx) {
        Path src = ctx.source();
        if (!Files.isRegularFile(src))
            return StageResult.failure("master not found: " + src, null);

        try {
            byte[] bytes = Files.readAllBytes(src);
            if (bytes.length < 8)
                return StageResult.failure("file header too short", null);

            byte[] hash = MessageDigest.getInstance("SHA-256").digest(bytes);
            String hex = toHex(hash);
            ctx.setSourceDigest(hex);
            return StageResult.success("sha256=" + hex.substring(0, 12) + "...");
        } catch (Exception e) {
            return StageResult.failure("hashing failed", e);
        }
    }

    private static String toHex(byte[] raw) {
        char[] out = new char[raw.length * 2];
        char[] hex = "0123456789abcdef".toCharArray();
        for (int i = 0; i < raw.length; i++) {
            int v = raw[i] & 0xFF;
            out[i * 2]     = hex[v >>> 4];
            out[i * 2 + 1] = hex[v & 0x0F];
        }
        return new String(out);
    }
}