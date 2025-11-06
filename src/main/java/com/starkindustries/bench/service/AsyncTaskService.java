package com.starkindustries.bench.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTaskService {

    @Async("benchExecutor")
    public CompletableFuture<Long> runAsyncTask(int workUnits) {
        long result = new SimulatedTask(workUnits).call();
        return CompletableFuture.completedFuture(result);
    }
}