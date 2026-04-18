package core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class PipelineLogger {

    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private PipelineLogger() {}

    public static void banner(String text) {
        System.out.println();
        System.out.println("========== " + text + " ==========");
    }

    public static void info(String text) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(CLOCK), text);
    }

    public static void ok(String stage, String detail, long ms) {
        System.out.printf("[%s]   ok  %-22s %6d ms   %s%n",
                LocalTime.now().format(CLOCK), stage, ms, detail == null ? "" : detail);
    }

    public static void fail(String stage, String reason) {
        System.out.printf("[%s]   !!  %-22s FAILED — %s%n",
                LocalTime.now().format(CLOCK), stage, reason);
    }
}