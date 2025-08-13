
package com.example.webfluxspringboot;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SseController {

    private final KafkaProductConsumer kafkaConsumer;

    public SseController(KafkaProductConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    /**
     * 设计模式：响应式端点 (Reactive Endpoint)
     *
     * 这个方法创建了一个 Server-Sent Events (SSE) 端点。SSE 是一种允许服务器单向推送数据到客户端的 Web 技术。
     * @GetMapping 的 produces = MediaType.TEXT_EVENT_STREAM_VALUE 属性是关键，它告诉 Spring MVC 和客户端，
     * 响应体是一个事件流，而不是一个单次的 JSON 对象。
     *
     * @return 一个 Flux<String>。Spring WebFlux 会自动订阅这个流，并将流中的每个元素
     *         格式化为 SSE 事件发送给客户端。
     */
    @GetMapping(value = "/sse/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> kafkaStream() {
        return kafkaConsumer.getStream();

        /*
         * --- 健壮性设计说明 ---
         * 1. 自动生命周期管理：与 WebSocket 类似，当客户端断开 SSE 连接时，Spring WebFlux 框架
         *    会自动取消对 kafkaConsumer.getStream() 的订阅，确保没有资源泄漏。
         *
         * 2. 错误传播：来自 Kafka 流的任何错误都会被正确传播，并导致 SSE 连接的终止。
         *
         * 3. 连接保持 (Heartbeat)：长时间处于空闲状态（没有新消息）的 SSE 连接可能会被网络中的
         *    代理或负载均衡器主动关闭。为了防止这种情况，一个健壮的 SSE 端点通常需要定期发送“心跳”
         *    信号（例如一个 SSE 注释行）。可以通过将主数据流与一个定时发出的心跳流合并（merge）来实现。
         *    例如：
         *    Flux<String> heartbeat = Flux.interval(Duration.ofSeconds(15)).map(i -> ":heartbeat");
         *    return Flux.merge(kafkaConsumer.getStream(), heartbeat);
         *    这将每15秒发送一个注释行，以保持连接活跃。
         */
    }
}
