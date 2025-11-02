package com.starkindustries.bench.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTaskService {

    @Async("benchExecutor")
    public CompletableFuture<Long> runAsyncTask(int workUnits) {
        // Reutiliza la misma carga que SimulatedTask
        return CompletableFuture.supplyAsync(() -> new SimulatedTask(workUnits).call());
    }
}