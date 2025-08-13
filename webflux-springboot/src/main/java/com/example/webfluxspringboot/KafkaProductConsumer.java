
package com.example.webfluxspringboot;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class KafkaProductConsumer {

    /**
     * 设计模式：适配器/桥接模式 (Adapter/Bridge) & 响应式流转换
     *
     * Sinks.Many 是 Project Reactor 的一个高级组件，用于以编程方式向响应式流中推送元素。
     * 在这里，它充当了一个桥梁，将来自 Kafka 的命令式、推送驱动的消息（consume 方法）
     * 适配为响应式的、拉取驱动的 Flux 流（getStream 方法）。
     *
     * .multicast() 创建了一个“热流”（Hot Stream）。与“冷流”不同，热流不为每个订阅者重新开始，
     * 而是将所有订阅者连接到同一个数据源。这意味着所有通过 getStream() 连接的客户端将实时接收到相同的 Kafka 消息。
     *
     * --- 健壮性设计：背压 (Backpressure) ---
     * .onBackpressureBuffer() 定义了背压策略。当消息的产生速度超过下游消费者的处理速度时，
     * 新消息将被缓存起来。这是一个简单有效的策略，但需要注意：如果生产者持续高速率发送，
     * 可能导致内存溢出（OutOfMemoryError）。在生产环境中，可能需要考虑更复杂的策略，
     * 如有界缓存（.onBackpressureBuffer(size)）、丢弃策略（.onBackpressureDrop()）或告警机制。
     */
    private final Sinks.Many<String> productSink = Sinks.many().multicast().onBackpressureBuffer();

    /**
     * 设计模式：消费者/监听器 (Consumer/Listener)
     * @KafkaListener 注解将此方法注册为 Kafka 主题 "products" 的消费者。
     *
     * --- 健壮性设计说明 ---
     * 1. 消息格式：方法直接消费原始的 String 类型的消息。这是一个健壮的选择，因为它避免了在监听器层面
     *    因 JSON 反序列化失败而导致整个消费组阻塞或进入无限重试循环。反序列化的任务被推迟到下游的订阅者中处理，
     *    使得错误处理更加灵活。
     * 2. 错误传播：productSink.tryEmitNext(message) 如果失败（例如 Sink 已被终止），会返回一个失败状态。
     *    当前代码没有检查这个状态。在生产代码中，应该检查返回值并记录错误。
     * 3. 异常处理：此方法没有 try-catch 块。任何未捕获的异常都将由 Spring Kafka 的默认错误处理器处理，
     *    这可能导致不符合预期的行为。健壮的消费者通常会包裹一个 try-catch 块来记录异常并决定如何响应。
     */
    @KafkaListener(topics = "products", groupId = "product-consumers")
    public void consume(String message) {
        System.out.println("Received Kafka message: " + message);
        // 将接收到的消息推送到 Sink 中，供响应式流的订阅者使用
        productSink.tryEmitNext(message);
    }

    /**
     * 将 Sink 暴露为一个可供订阅的 Flux 流。
     * 系统中的其他响应式组件（如 WebSocket 处理器）可以订阅这个 Flux，
     * 以非阻塞的方式接收来自 Kafka 的实时消息。
     * @return 一个代表实时 Kafka 消息的 Flux<String> 热流。
     */
    public Flux<String> getStream() {
        return productSink.asFlux();
    }
}
