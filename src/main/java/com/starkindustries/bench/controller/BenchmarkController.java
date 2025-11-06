package com.starkindustries.bench.controller;

import com.starkindustries.bench.domain.RunSummary;
import com.starkindustries.bench.model.BenchmarkRunEntity;
import com.starkindustries.bench.repo.BenchmarkRunRepository;
import com.starkindustries.bench.service.BenchmarkService;
import com.starkindustries.bench.service.PersistenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benchmark")
@CrossOrigin(origins = "*")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;
    private final PersistenceService persistenceService;
    private final BenchmarkRunRepository runRepo;

    public BenchmarkController(BenchmarkService benchmarkService,
                               PersistenceService persistenceService,
                               BenchmarkRunRepository runRepo) {
        this.benchmarkService = benchmarkService;
        this.persistenceService = persistenceService;
        this.runRepo = runRepo;
    }

    // POST /benchmark/start?tasks=150&threads=8
    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestParam int tasks, @RequestParam int threads) throws Exception {
        RunSummary summary = benchmarkService.runBenchmark(tasks, threads);
        BenchmarkRunEntity saved = persistenceService.persistFromSummary(summary);
        return ResponseEntity.ok(saved);
    }

    // Histórico (ordenado desc por fecha)
    @GetMapping("/runs")
    public List<BenchmarkRunEntity> runs() {
        return runRepo.findAll().stream()
                .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }
}
