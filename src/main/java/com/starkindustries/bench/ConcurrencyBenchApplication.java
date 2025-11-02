package com.starkindustries.bench;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrencyBenchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrencyBenchApplication.class, args);
    }
}
