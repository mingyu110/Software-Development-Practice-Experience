
package com.example.webfluxspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RedisSubscriber {

    private final ReactiveRedisConnectionFactory factory;

    /**
     * 通过构造函数注入 ReactiveRedisConnectionFactory。
     * Spring 的 @Autowired 注解会自动寻找一个类型匹配的 Bean 并注入。
     *
     * [潜在问题与 @Qualifier 的作用]
     * 当前项目中，Spring Boot 自动配置了唯一的 ReactiveRedisConnectionFactory Bean，因此注入没有歧义。
     * 但是，如果未来在其他配置类（如 @Configuration 文件）中手动定义了另一个同类型的 Bean，
     * Spring 将无法确定注入哪一个，从而导致 NoUniqueBeanDefinitionException 异常。
     *
     * 届时，就需要使用 @Qualifier 注解来明确指定要注入的 Bean 的名称，例如：
     * public RedisSubscriber(@Qualifier("customFactoryName") ReactiveRedisConnectionFactory factory) { ... }
     *
     * 这是一种良好的防御性编程实践，有助于理解和维护。
     */
    @Autowired
    public RedisSubscriber(ReactiveRedisConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * 设计模式：观察者模式 (Observer Pattern)
     * @PostConstruct 注解确保在 Bean 初始化后立即执行此方法，建立订阅关系。
     * RedisSubscriber 作为一个“观察者”，订阅（observe）了名为 "product-updates" 的主题（Subject）。
     * 当有新消息发布到该主题时，Redis 会通知所有观察者，并触发这里的 .subscribe() 中的逻辑。
     */
    @PostConstruct
    private void init() {
        ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(factory);
        container.receive(ChannelTopic.of("product-updates"))
                .map(msg -> msg.getMessage())
                .subscribe(message -> {
                    System.out.println("Received from Redis: " + message);
                    // 在这里可以添加将消息通过 WebSocket 广播出去的逻辑
                });
        
        /*
         * --- 健壮性设计说明 ---
         * 上述 .subscribe() 方法只处理了正常消息（onNext）。
         * 在生产环境中，一个健壮的订阅者必须处理错误情况，以防止因单个错误消息或网络问题导致整个订阅流中断。
         * 一个更健壮的实现应该提供第二个参数（onError consumer），如下所示：
         *
         * .subscribe(
         *     message -> {
         *         System.out.println("Received from Redis: " + message);
         *         // Handle or broadcast via WebSocket
         *     },
         *     error -> {
         *         // 使用日志框架记录错误，例如：log.error("Redis subscription error!", error);
         *         // 在这里可以加入重连或告警逻辑
         *     }
         * );
         * 这样可以确保即使发生错误，程序也能捕获异常、记录日志，而不会悄无声息地失败。
         */
    }
}
