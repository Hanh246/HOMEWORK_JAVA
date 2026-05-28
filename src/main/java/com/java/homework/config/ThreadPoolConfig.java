package com.java.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "excelExcelExecutor")
    public Executor excelExcelExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // Số luồng chạy thường trực (tùy thuộc vào số nhân CPU)
        executor.setMaxPoolSize(8);  // Số luồng tối đa
        executor.setQueueCapacity(100); // Kích thước tối đa của BlockingQueue
        executor.setThreadNamePrefix("ExcelWorker-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
