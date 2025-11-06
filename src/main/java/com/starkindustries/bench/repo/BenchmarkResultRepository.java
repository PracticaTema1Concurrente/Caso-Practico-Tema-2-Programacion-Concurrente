package com.starkindustries.bench.repo;

import com.starkindustries.bench.model.BenchmarkResultEntity;
import com.starkindustries.bench.domain.BenchmarkMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BenchmarkResultRepository extends JpaRepository<BenchmarkResultEntity, Long> {
    List<BenchmarkResultEntity> findByModeOrderByIdAsc(BenchmarkMode mode);
}