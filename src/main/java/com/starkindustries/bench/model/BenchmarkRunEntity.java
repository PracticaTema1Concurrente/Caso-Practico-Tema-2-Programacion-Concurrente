package com.starkindustries.bench.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "benchmark_run")
public class BenchmarkRunEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private int totalTasks;

    @Column(nullable = false)
    private int threadsUsed;

    // BenchmarkRunEntity.java
    @OneToMany(
            mappedBy = "run",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY   // mejor LAZY para no arrastrar todo siempre
    )
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<BenchmarkResultEntity> results = new ArrayList<>();


    public String getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getThreadsUsed() { return threadsUsed; }
    public void setThreadsUsed(int threadsUsed) { this.threadsUsed = threadsUsed; }

    public List<BenchmarkResultEntity> getResults() { return results; }
    public void addResult(BenchmarkResultEntity r) {
        r.setRun(this);
        results.add(r);
    }
}
