package com.cly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.cly.dao")
public class MapperConfiguration {
}
