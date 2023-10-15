package com.doksanbir.ratelimiter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorConfig {

    @Value("${executor.poolSize}")
    private int poolSize;

    @Bean
    public ScheduledExecutorService customScheduledExecutorService() {
        return Executors.newScheduledThreadPool(poolSize);
    }
}



