package com.starkindustries.bench.controller;

import com.stark.bench.domain.BenchmarkRequest;
import com.stark.bench.domain.RunSummary;
import com.stark.bench.service.BenchmarkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    public BenchmarkController(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    // POST /benchmark/start?tasks=50&threads=8
    @PostMapping("/start")
    public ResponseEntity<RunSummary> start(
            @Valid BenchmarkRequest req,
            @RequestParam(name = "tasks", required = false) Integer tasksParam,
            @RequestParam(name = "threads", required = false) Integer threadsParam
    ) throws Exception {
        // admite body form/query; prioriza query params
        int tasks = tasksParam != null ? tasksParam : (req.getTasks() != null ? req.getTasks() : 50);
        int threads = threadsParam != null ? threadsParam : (req.getThreads() != null ? req.getThreads() : 8);
        return ResponseEntity.ok(benchmarkService.runBenchmark(tasks, threads));
    }

    // GET /benchmark/result
    @GetMapping("/result")
    public ResponseEntity<?> lastResult() {
        RunSummary rs = benchmarkService.getLastSummary();
        return rs == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(rs);
    }

    // GET /benchmark/modes
    @GetMapping("/modes")
    public ResponseEntity<?> modes() {
        return ResponseEntity.ok(benchmarkService.availableModes());
    }
}

