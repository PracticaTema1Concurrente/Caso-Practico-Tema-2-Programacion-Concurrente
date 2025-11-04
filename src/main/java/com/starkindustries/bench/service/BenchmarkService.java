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

    // almacena el último resultado para GET /benchmark/result
    private volatile RunSummary lastSummary;

    public BenchmarkService(AsyncTaskService asyncTaskService,
                            ThreadPoolTaskExecutor benchExecutor) {
        this.asyncTaskService = asyncTaskService;
        this.benchExecutor = benchExecutor;
    }

    public synchronized RunSummary runBenchmark(int tasks, int threads) throws Exception {
        // 1) Modo secuencial (baseline)
        long t0 = Metrics.now();
        for (int i = 0; i < tasks; i++) {
            new SimulatedTask(i % 7).call();
        }
        long seqMs = Metrics.elapsedMs(t0);

        // 2) Modo ExecutorService manual
        long execMs = runWithExecutorService(tasks, threads);

        // 3) Modo Spring @Async
        long asyncMs = runWithSpringAsync(tasks, threads);

        // speedup & efficiency (respecto a secuencial)
        List<BenchmarkResult> results = new ArrayList<>();
        results.add(new BenchmarkResult(BenchmarkMode.SEQUENTIAL, seqMs, 1.0, 1.0));
        results.add(new BenchmarkResult(BenchmarkMode.EXECUTOR_SERVICE,
                execMs,
                Metrics.round2((double) seqMs / execMs),
                Metrics.round2(((double) seqMs / execMs) / threads)));
        results.add(new BenchmarkResult(BenchmarkMode.SPRING_ASYNC,
                asyncMs,
                Metrics.round2((double) seqMs / asyncMs),
                Metrics.round2(((double) seqMs / asyncMs) / threads)));

        RunSummary summary = new RunSummary(tasks, threads, results);
        this.lastSummary = summary;
        return summary;
    }

    private long runWithExecutorService(int tasks, int threads) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        long start = Metrics.now();
        try {
            List<Future<Long>> futures = new ArrayList<>(tasks);
            for (int i = 0; i < tasks; i++) {
                futures.add(pool.submit(new SimulatedTask(i % 7)));
            }
            for (Future<Long> f : futures) f.get();
        } finally {
            pool.shutdown();
            pool.awaitTermination(30, TimeUnit.SECONDS);
        }
        return Metrics.elapsedMs(start);
    }

    private long runWithSpringAsync(int tasks, int threads) throws Exception {
        // Guardamos valores originales del bean para restaurarlos
        int core0 = benchExecutor.getCorePoolSize();
        int max0  = benchExecutor.getMaxPoolSize();

        // Ajustamos temporalmente el tamaño de pool a la petición
        benchExecutor.setCorePoolSize(threads);
        benchExecutor.setMaxPoolSize(threads);

        long start = Metrics.now();
        try {
            List<CompletableFuture<Long>> futures = new ArrayList<>(tasks);
            for (int i = 0; i < tasks; i++) {
                futures.add(asyncTaskService.runAsyncTask(i % 7));
            }
            // Esperar a todas
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);
        } finally {
            // Restaurar configuración por defecto
            benchExecutor.setCorePoolSize(core0);
            benchExecutor.setMaxPoolSize(max0);
        }
        return Metrics.elapsedMs(start);
    }

    public RunSummary getLastSummary() {
        return lastSummary;
    }

    public List<String> availableModes() {
        List<String> modes = new ArrayList<>();
        modes.add(BenchmarkMode.SEQUENTIAL.name() + " — ejecución en un único hilo.");
        modes.add(BenchmarkMode.EXECUTOR_SERVICE.name() + " — pool fijo/cached con java.util.concurrent.");
        modes.add(BenchmarkMode.SPRING_ASYNC.name() + " — métodos @Async con ThreadPoolTaskExecutor.");
        return modes;
    }
}

