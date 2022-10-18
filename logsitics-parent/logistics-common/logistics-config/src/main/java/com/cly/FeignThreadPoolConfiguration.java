package com.cly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class FeignThreadPoolConfiguration {

    /**
     * 创建专门用于远程调用而使用的固定线程池
     * 专门用于远程调用 转化串行为并行
     *
     * @return 构建一个固定的线程池
     */
    @Bean
    public ExecutorService feignExecutorService() {
        return Executors.newFixedThreadPool(2);
    }

}
