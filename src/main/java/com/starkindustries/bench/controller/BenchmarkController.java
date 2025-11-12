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

    // POST /benchmark/start?tasks=50&threads=8&pool=fixed|cached
    @PostMapping("/start")
    public ResponseEntity<RunSummary> start(@RequestParam int tasks,
                                            @RequestParam int threads,
                                            @RequestParam(defaultValue = "fixed") String pool) throws Exception {
        if (tasks < 1 || threads < 1) return ResponseEntity.badRequest().build();

        RunSummary summary = benchmarkService.runBenchmark(tasks, threads /*, pool si lo usas */);
        // Persistimos pero NO devolvemos la entidad
        persistenceService.persistFromSummary(summary);
        return ResponseEntity.ok(summary); // <- devuelve JSON plano esperado
    }

    // Último resultado en memoria
    @GetMapping("/result")
    public ResponseEntity<RunSummary> last() {
        RunSummary s = benchmarkService.getLastSummary();
        return (s == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(s);
    }

    // Modos disponibles
    @GetMapping("/modes")
    public List<String> modes() { return benchmarkService.availableModes(); }

    // Histórico (opcional, ya lo tenías)
    @GetMapping("/runs")
    public List<BenchmarkRunEntity> runs() {
        return runRepo.findAll().stream()
                .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }
}

