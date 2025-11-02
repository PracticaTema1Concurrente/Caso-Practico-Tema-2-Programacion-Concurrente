package com.starkindustries.bench.util;

public class Metrics {
    public static long now() {
        return System.nanoTime();
    }
    public static long elapsedMs(long startNs) {
        return (System.nanoTime() - startNs) / 1_000_000L;
    }
    public static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

