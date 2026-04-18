package services;

import core.PipelineContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class PackagingService {

    public void sealDrm(PipelineContext ctx) {
        ctx.put("drm.scheme", "widevine-like");
        ctx.put("drm.keyId",  "k-" + ctx.sourceDigest().substring(0, 8));
        ctx.sealDrm();
    }

    public Path writeManifest(PipelineContext ctx) throws Exception {
        Path metaDir = ctx.bundleDir().resolve("metadata");
        Files.createDirectories(metaDir);
        Path file = metaDir.resolve("manifest.json");
        Files.writeString(file, renderJson(ctx));
        return file;
    }

    private String renderJson(PipelineContext ctx) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("{\n");
        kv(sb, "titleId",      q(ctx.titleId()),                 true);
        kv(sb, "state",        q(ctx.state().name()),            true);
        kv(sb, "sourceDigest", q(ctx.sourceDigest()),            true);
        kv(sb, "container",    q(ctx.containerExt()),            true);
        kv(sb, "drmSealed",    String.valueOf(ctx.drmSealed()),  true);
        kv(sb, "canonical",    q(asString(ctx.get("canonicalRendition"))), true);

        sb.append("  \"timeline\": {\n");
        innerKv(sb, "introEndsAt",   String.valueOf(ctx.get("intro.endsAt")),        true);
        innerKv(sb, "outroStartsAt", String.valueOf(ctx.get("outro.startsAt")),      true);
        innerKv(sb, "watchNextCue",  String.valueOf(ctx.get("creditRoll.startsAt")), false);
        sb.append("  },\n");

        sb.append("  \"assets\": {\n");
        innerKv(sb, "spriteMap",    q("images/sprite_map.jpg"),      true);
        innerKv(sb, "transcriptEn", q("text/source_transcript.txt"), true);
        innerKv(sb, "transcriptRo", q("text/ro_translation.txt"),    true);
        innerKv(sb, "dubRo",        q("audio/ro_dub_synthetic.aac"), false);
        sb.append("  },\n");

        Map<?, ?> branding = (Map<?, ?>) ctx.get("branding.byRegion");
        sb.append("  \"branding\": {");
        if (branding != null && !branding.isEmpty()) {
            sb.append('\n');
            int i = 0, n = branding.size();
            for (var e : branding.entrySet()) {
                sb.append("    ").append(q(String.valueOf(e.getKey())))
                        .append(": ").append(q(String.valueOf(e.getValue())));
                if (++i < n) sb.append(',');
                sb.append('\n');
            }
            sb.append("  ");
        }
        sb.append("}\n");
        sb.append("}\n");
        return sb.toString();
    }

    private static String asString(Object o) { return o == null ? "" : o.toString(); }
    private static String q(String s)        { return "\"" + s.replace("\"", "\\\"") + "\""; }
    private static void kv(StringBuilder b, String k, String v, boolean comma) {
        b.append("  ").append(q(k)).append(": ").append(v).append(comma ? ",\n" : "\n");
    }
    private static void innerKv(StringBuilder b, String k, String v, boolean comma) {
        b.append("    ").append(q(k)).append(": ").append(v).append(comma ? ",\n" : "\n");
    }
}