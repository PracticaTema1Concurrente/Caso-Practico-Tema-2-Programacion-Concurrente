package com.starkindustries.bench.service;

import com.starkindustries.bench.domain.RunSummary;
import com.starkindustries.bench.domain.BenchmarkResult;
import com.starkindustries.bench.model.BenchmarkRunEntity;
import com.starkindustries.bench.model.BenchmarkResultEntity;
import com.starkindustries.bench.repo.BenchmarkRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersistenceService {

    private final BenchmarkRunRepository runRepo;

    public PersistenceService(BenchmarkRunRepository runRepo) {
        this.runRepo = runRepo;
    }

    @Transactional
    public BenchmarkRunEntity persistFromSummary(RunSummary summary) {
        BenchmarkRunEntity run = new BenchmarkRunEntity();
        run.setTotalTasks(summary.getTotalTasks());
        run.setThreadsUsed(summary.getThreadsUsed());

        for (BenchmarkResult r : summary.getResults()) {
            BenchmarkResultEntity e = new BenchmarkResultEntity();
            e.setMode(r.getMode());
            e.setTimeMs(r.getTimeMs());
            e.setSpeedup(r.getSpeedup());
            e.setEfficiency(r.getEfficiency());
            run.addResult(e);
        }
        return runRepo.save(run);
    }
}

