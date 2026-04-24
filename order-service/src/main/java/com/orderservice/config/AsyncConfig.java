package com.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "outboxExecutor")
    public Executor outboxExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);       // số worker chạy song song
        executor.setMaxPoolSize(10);       // scale khi cần
        executor.setQueueCapacity(100);    // hàng đợi
        executor.setThreadNamePrefix("outbox-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // nếu full → thread hiện tại xử lý (không mất task)

        executor.initialize();
        return executor;
    }
}
