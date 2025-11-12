package com.starkindustries.bench.model;

import com.starkindustries.bench.domain.BenchmarkMode;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "benchmark_result")
public class BenchmarkResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenchmarkMode mode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "run_id")
    @JsonBackReference
    private BenchmarkRunEntity run;

    @Column(nullable = false)
    private long timeMs;

    @Column(nullable = false)
    private double speedup;

    @Column(nullable = false)
    private double efficiency;

    public Long getId() { return id; }
    public BenchmarkMode getMode() { return mode; }
    public void setMode(BenchmarkMode mode) { this.mode = mode; }
    public BenchmarkRunEntity getRun() { return run; }
    public void setRun(BenchmarkRunEntity run) { this.run = run; }
    public long getTimeMs() { return timeMs; }
    public void setTimeMs(long timeMs) { this.timeMs = timeMs; }
    public double getSpeedup() { return speedup; }
    public void setSpeedup(double speedup) { this.speedup = speedup; }
    public double getEfficiency() { return efficiency; }
    public void setEfficiency(double efficiency) { this.efficiency = efficiency; }
}

