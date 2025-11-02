package com.starkindustries.bench.domain;

import java.util.List;

public class RunSummary {
    private int totalTasks;
    private int threadsUsed;
    private List<BenchmarkResult> results;

    public RunSummary(int totalTasks, int threadsUsed, List<BenchmarkResult> results) {
        this.totalTasks = totalTasks;
        this.threadsUsed = threadsUsed;
        this.results = results;
    }

    public int getTotalTasks() { return totalTasks; }
    public int getThreadsUsed() { return threadsUsed; }
    public List<BenchmarkResult> getResults() { return results; }
}
