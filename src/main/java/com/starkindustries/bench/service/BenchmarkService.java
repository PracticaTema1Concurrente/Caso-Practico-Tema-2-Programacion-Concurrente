package com.starkindustries.bench.service;

import com.starkindustries.bench.domain.*;
import com.starkindustries.bench.util.Metrics;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class BenchmarkService {

    private final AsyncTaskService asyncTaskService;
    private final ThreadPoolTaskExecutor benchExecutor;

    private volatile RunSummary lastSummary;

    public BenchmarkService(AsyncTaskService asyncTaskService,
                            ThreadPoolTaskExecutor benchExecutor) {
        this.asyncTaskService = asyncTaskService;
        this.benchExecutor = benchExecutor;
    }

    public RunSummary runBenchmark(int tasks, int threads) throws Exception {
        // 1) Secuencial
        long t0 = Metrics.now();
        for (int i = 0; i < tasks; i++) new SimulatedTask(i % 7).call();
        long seqMs = Metrics.elapsedMs(t0);

        // 2) Concurrente con ExecutorService (tipo configurable FIXED/CACHED)
        long execMs = runWithExecutorService(tasks, threads, PoolType.FIXED); // o parámetro recibido

        // 3) Asíncrono Spring @Async (reajustando el pool)
        long asyncMs = runWithSpringAsync(tasks, threads);

        // métricas
        double seqAvg = (double) seqMs / tasks;
        double execAvg = (double) execMs / tasks;
        double asyncAvg = (double) asyncMs / tasks;

        List<BenchmarkResult> results = new ArrayList<>();
        results.add(new BenchmarkResult(BenchmarkMode.SEQUENTIAL,     seqMs, 1.0, 1.0, Metrics.round2(seqAvg)));
        results.add(new BenchmarkResult(BenchmarkMode.EXECUTOR_SERVICE,execMs,
                Metrics.round2((double) seqMs / execMs),
                Metrics.round2(((double) seqMs / execMs) / threads),
                Metrics.round2(execAvg)));
        results.add(new BenchmarkResult(BenchmarkMode.SPRING_ASYNC,   asyncMs,
                Metrics.round2((double) seqMs / asyncMs),
                Metrics.round2(((double) seqMs / asyncMs) / threads),
                Metrics.round2(asyncAvg)));

        RunSummary summary = new RunSummary(tasks, threads, results);
        this.lastSummary = summary;
        return summary;
    }

    enum PoolType { FIXED, CACHED }

    private long runWithExecutorService(int tasks, int threads, PoolType type) throws Exception {
        ExecutorService pool = (type == PoolType.CACHED)
                ? Executors.newCachedThreadPool()
                : Executors.newFixedThreadPool(threads);

        long t = Metrics.now();
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < tasks; i++) futures.add(pool.submit(new SimulatedTask(i % 7)));
        for (Future<Long> f : futures) f.get();
        pool.shutdown();
        return Metrics.elapsedMs(t);
    }

    private long runWithSpringAsync(int tasks, int threads) throws Exception {
        // Reajuste dinámico del pool Spring
        benchExecutor.setCorePoolSize(threads);
        benchExecutor.setMaxPoolSize(threads);

        long t = Metrics.now();
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        for (int i = 0; i < tasks; i++) futures.add(asyncTaskService.runAsyncTask(i % 7));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return Metrics.elapsedMs(t);
    }

    public RunSummary getLastSummary() { return lastSummary; }

    public List<String> availableModes() {
        return List.of(
                BenchmarkMode.SEQUENTIAL.name() + " — ejecución en un único hilo.",
                BenchmarkMode.EXECUTOR_SERVICE.name() + " — pool fijo/cached con java.util.concurrent.",
                BenchmarkMode.SPRING_ASYNC.name() + " — métodos @Async con ThreadPoolTaskExecutor."
        );
    }
}

