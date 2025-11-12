package com.starkindustries.bench.domain;

public class BenchmarkResult {
    private BenchmarkMode mode;
    private long timeMs;
    private double speedup;
    private double efficiency;
    private double avgPerTaskMs;

    public BenchmarkResult() {}

    public BenchmarkResult(BenchmarkMode mode, long timeMs, double speedup, double efficiency, double avgPerTaskMs) {
        this.mode = mode;
        this.timeMs = timeMs;
        this.speedup = speedup;
        this.efficiency = efficiency;
        this.avgPerTaskMs = avgPerTaskMs;
    }
    public BenchmarkMode getMode() { return mode; }
    public long getTimeMs() { return timeMs; }
    public double getSpeedup() { return speedup; }
    public double getEfficiency() { return efficiency; }
    public void setMode(BenchmarkMode mode) { this.mode = mode; }
    public void setTimeMs(long timeMs) { this.timeMs = timeMs; }
    public void setSpeedup(double speedup) { this.speedup = speedup; }
    public void setEfficiency(double efficiency) { this.efficiency = efficiency; }
}