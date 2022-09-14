package com.cly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 厂库管理模块
 */
@SpringBootApplication
@EnableDiscoveryClient  // 开启服务注册
public class LogisticsWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsWarehouseApplication.class);
    }
}
