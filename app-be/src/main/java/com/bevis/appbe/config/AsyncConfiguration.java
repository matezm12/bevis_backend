package com.bevis.appbe.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    private final BevisProperties bevisProperties;

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(bevisProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(bevisProperties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(bevisProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix("bevis-Executor-");
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
