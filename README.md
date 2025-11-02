Iván Hidalgo y Gabriel Kaakedjian

# Objetivo
El objetivo de esta práctica es analizar el rendimiento y eficiencia de diferentes estrategias de ejecución concurrente en Java, utilizando Spring Boot como marco de trabajo.
El estudiante deberá implementar un benchmark que compare el tiempo de ejecución de distintas configuraciones de hilos (manuales y gestionadas por Spring) para un conjunto de tareas computacionalmente intensivas.

# Descripción general
Implemente una aplicación Spring Boot que permita lanzar pruebas de rendimiento (benchmarks) de distintas formas de ejecutar tareas concurrentes.
Las pruebas deberán ejecutarse a través de un endpoint REST, y el resultado deberá incluir tiempos de ejecución, número de hilos usados y eficiencia relativa.

El benchmark consistirá en ejecutar un conjunto de tareas simuladas (por ejemplo, cálculo de números primos, multiplicación de matrices o generación de hashes) bajo tres estrategias distintas:

### 1. Ejecución secuencial (monohilo)

### 2. Ejecución concurrente con ExecutorService manual

### 3. Ejecución asíncrona con anotaciones de Spring (@Async)

# Requerimientos técnicos
### Estructura del proyecto

- Aplicación Spring Boot (Java 17 o superior).

- Un controlador REST (@RestController) que permita lanzar y consultar benchmarks.

- Un servicio principal (@Service) encargado de ejecutar las pruebas.

- Activación de ejecución asíncrona con @EnableAsync.

### Estrategias de ejecución

- Modo 1: Ejecución secuencial en un solo hilo.

- Modo 2: Ejecución concurrente con ExecutorService configurable (FixedThreadPool o CachedThreadPool).

- Modo 3: Ejecución asíncrona con métodos anotados con @Async.

### Medición de rendimiento

- Utilizar System.nanoTime() o Instant.now() para medir el tiempo total de ejecución en cada modo.

- Calcular métricas básicas:

-- Tiempo total de ejecución

-- Promedio por tarea

-- Aceleración (Speedup = T_secuencial / T_concurrente)

-- Eficiencia (Efficiency = Speedup / número_de_hilos)

- Los resultados deben mostrarse en formato JSON.

### Configuración de hilos

- Permitir configurar el número de tareas y el número máximo de hilos desde el endpoint (por ejemplo, /benchmark?tasks=100&threads=8).

- Personalizar el Executor de Spring con @Configuration y ThreadPoolTaskExecutor.

### Concurrencia y seguridad

- Si las tareas acceden a recursos compartidos, garantizar sincronización.

### Endpoints mínimos

- POST /benchmark/start → inicia una prueba con parámetros configurables.

- GET /benchmark/result → devuelve los resultados del último test (modo, tiempos, speedup, eficiencia).

- GET /benchmark/modes → lista los modos disponibles y su descripción.

# Salida esperada
## Petición:
POST /benchmark/start?tasks=50&threads=8
## Respuesta:
 
{ "totalTasks": 50, "threadsUsed": 8, "results": [ { "mode": "SEQUENTIAL", "timeMs": 4321, "speedup": 1.0, "efficiency": 1.0 }, { "mode": "EXECUTOR_SERVICE", "timeMs": 987, "speedup": 4.38, "efficiency": 0.55 }, { "mode": "SPRING_ASYNC", "timeMs": 1012, "speedup": 4.27, "efficiency": 0.53 } ] }
