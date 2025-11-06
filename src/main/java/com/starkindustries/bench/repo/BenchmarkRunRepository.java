package com.starkindustries.bench.repo;

import com.starkindustries.bench.model.BenchmarkRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenchmarkRunRepository extends JpaRepository<BenchmarkRunEntity, String> {
}