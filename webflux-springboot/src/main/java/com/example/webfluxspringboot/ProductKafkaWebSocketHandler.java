
package com.example.webfluxspringboot;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ProductKafkaWebSocketHandler implements WebSocketHandler {

    private final KafkaProductConsumer kafkaConsumer;

    public ProductKafkaWebSocketHandler(KafkaProductConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    /**
     * 设计模式：策略模式 (Strategy) & 观察者模式 (Observer)
     *
     * 1. 策略模式：本类实现了 WebSocketHandler 接口，作为一个具体的处理“策略”。Spring 框架在接收到
     *    WebSocket 连接请求后，会根据路由配置（见 WebSocketConfig）将请求委托给这个策略进行处理。
     * 2. 观察者模式：此处理器订阅（观察）了来自 KafkaProductConsumer 的消息流（Subject）。
     *    每当有新消息时，它就会被触发，并将消息推送给客户端。
     *
     * @param session 代表一个独立的 WebSocket 连接会话。
     * @return Mono<Void> 表示此方法处理的是一个持续的、双向的交互，而不是一次性的请求-响应。
     *         当流处理完成（或连接关闭）时，这个 Mono 才会终止。
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // session.send() 是启动“服务器到客户端”消息推送的关键。
        // 它需要一个 Publisher<WebSocketMessage> 作为参数。
        return session.send(
                // 1. 从 Kafka 消费者获取实时消息流 (Flux<String>)
                kafkaConsumer.getStream()
                        // 2. 将每个字符串消息转换为 WebSocketMessage 对象
                        .map(session::textMessage)
        );

        /*
         * --- 健壮性设计：全自动的响应式流管理 ---
         * 这段代码虽然简洁，但其健壮性由 Project Reactor 和 Spring WebFlux 框架在底层保证：
         *
         * 1. 生命周期管理：当 WebSocket 连接关闭时（无论是客户端主动关闭还是网络异常），
         *    框架会自动取消（cancel）对 kafkaConsumer.getStream() 的订阅。这意味着资源
         *    （如 Kafka 的连接和内存中的流）会被自动清理，有效防止了资源泄漏。
         *
         * 2. 错误传播：如果 kafkaConsumer.getStream() 内部产生任何错误（例如，Sink 被异常关闭），
         *    错误信号会沿着响应式链传播。session.send() 捕获到这个错误后，会以适当的错误码
         *    优雅地关闭 WebSocket 连接，而不是让程序崩溃。
         *
         * 3. 背压（Backpressure）：整个流是支持背压的。如果 WebSocket 客户端处理消息的速度跟不上
         *    Kafka 产生消息的速度，框架会自动向上游（最终到 KafkaProductConsumer 的缓冲区）传递
         *    背压信号，减缓消息的发送速率，防止因客户端性能不足而导致服务器内存溢出。
         */
    }
}
