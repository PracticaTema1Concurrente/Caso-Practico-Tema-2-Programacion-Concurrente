package com.starkindustries.bench.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BenchmarkRequest {
    @NotNull @Min(1)
    private Integer tasks;          // nº total de tareas simuladas

    @Min(1)
    private Integer threads;        // nº de hilos a usar en modos concurrentes

    public Integer getTasks() { return tasks; }
    public void setTasks(Integer tasks) { this.tasks = tasks; }
    public Integer getThreads() { return threads; }
    public void setThreads(Integer threads) { this.threads = threads; }
}