package com.starkindustries.bench.service;


import java.util.concurrent.Callable;

public class SimulatedTask implements Callable<Long> {

    private final int workUnits;

    public SimulatedTask(int workUnits) {
        this.workUnits = workUnits;
    }

    @Override
    public Long call() {
        // Carga CPU determinista: calcular primos/ hashes sencillos
        long acc = 0;
        int n = 2000 + workUnits; // pequeño param para variar
        for (int i = 2; i < n; i++) {
            boolean p = true;
            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) { p = false; break; }
            }
            if (p) acc += i;
        }
        return acc; // resultado irrelevante; evita que el compilador elimine el bucle
    }
}