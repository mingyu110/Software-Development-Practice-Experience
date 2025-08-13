
package com.example.webfluxspringboot;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisPublisher {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    /**
     * 设计模式：依赖注入 (Dependency Injection)
     *
     * 通过构造函数注入 ReactiveRedisTemplate 的实例。
     * 注意这里使用了 @Qualifier("reactiveRedisTemplate")，这是一个很好的实践，
     * 即使当前只有一个该类型的 Bean，它也能明确指定依赖，避免未来因新增配置而引发的歧义（NoUniqueBeanDefinitionException）。
     */
    public RedisPublisher(@org.springframework.beans.factory.annotation.Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设计模式：发布/订阅 (Publish-Subscribe)
     * 这是发布者的核心方法，负责将消息发布到指定的频道 (channel)。
     *
     * 健壮性设计：
     * 1. 异步非阻塞：方法返回一个 Mono<Long>，这是一个响应式类型。调用者不会被阻塞，
     *    而是得到一个“未来”的凭证，可以在操作完成或失败时执行后续逻辑（例如记录日志、重试等）。
     * 2. 明确的返回：返回的 Long 值代表接收到该消息的订阅者数量，为系统监控提供了有效数据。
     * 3. 错误处理：响应式流 Mono 内置了强大的错误处理机制。调用方可以通过 .doOnError() 或 .onErrorResume() 等操作符
     *    来优雅地处理网络中断、Redis 服务不可用等异常情况，防止程序崩溃。
     *
     * @param channel 目标频道
     * @param message 要发送的消息
     * @return 一个 Mono<Long>，发布成功后会发出订阅者的数量。
     */
    public Mono<Long> publish(String channel, String message) {
        return redisTemplate.convertAndSend(channel, message);
    }
}
