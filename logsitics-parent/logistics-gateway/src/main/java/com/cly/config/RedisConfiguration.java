package com.cly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    /**
     * RedisTemplate是在java代码中实现对redis操作的类对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 为string类型key设置序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 为string类型value设置序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 为hash类型key设置序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 为hash类型value设置序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 放入工厂对象，正真的生成操作redis对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }
}
