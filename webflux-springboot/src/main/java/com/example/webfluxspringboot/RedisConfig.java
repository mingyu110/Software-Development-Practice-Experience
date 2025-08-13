
package com.example.webfluxspringboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 设计模式：工厂方法 (Factory Method)
     *
     * @Bean 注解标志着这是一个工厂方法，负责创建和配置一个 ReactiveRedisTemplate Bean。
     * Spring 容器会管理这个 Bean 的生命周期，并在需要的地方注入它。
     * 这种方式将对象的创建和配置逻辑集中管理，降低了代码的耦合度。
     *
     * @param factory Spring Boot 自动配置并注入的 Redis 连接工厂。
     * @return 配置完成的 ReactiveRedisTemplate 实例。
     */
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // 使用 StringRedisSerializer 来确保 Redis 中存储的键和值都是可读的字符串格式。
        // 这是构建健壮 Redis 操作的基础，避免了因序列化问题导致的乱码或数据解析失败。
        return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
    }
}
