package com.cly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单系统
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LogisticsOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsOrderApplication.class);
    }

}
