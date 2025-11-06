package com.starkindustries.bench.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("benchExecutor")
    public ThreadPoolTaskExecutor benchExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(4);        // valores por defecto (se pueden reajustar en runtime)
        ex.setMaxPoolSize(8);
        ex.setQueueCapacity(1000);
        ex.setThreadNamePrefix("bench-");
        ex.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.setAwaitTerminationSeconds(30);
        ex.initialize();
        return ex;
    }
}
