
package com.example.webfluxspringboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    /**
     * 设计模式：工厂方法 (Factory Method) & 前端控制器 (Front Controller) 的一部分
     *
     * 这个 @Bean 方法创建了一个 HandlerMapping 实例，它是 Spring Web 框架中前端控制器模式的核心组件之一。
     * 它负责定义 WebSocket 连接的 URL（例如 "/ws/products"）与对应的处理器（WebSocketHandler）之间的映射关系。
     *
     * 健壮性考虑：
     * 1. 集中管理路由：将所有 WebSocket 路由规则集中在此处，便于维护和避免冲突。
     * 2. 优先级（Order）：通过 mapping.setOrder(1) 设置较高的优先级，确保该路由在其他默认路由之前被匹配，
     *    这在复杂的应用中可以避免意外的路由覆盖问题。
     *
     * @param productKafkaWebSocketHandler 注入我们自定义的 WebSocket 处理器。
     * @return 配置好的 HandlerMapping 实例。
     */
    @Bean
    public HandlerMapping handlerMapping(ProductKafkaWebSocketHandler productKafkaWebSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        // 将 "/ws/products"路径映射到 productKafkaWebSocketHandler 处理器
        map.put("/ws/products", productKafkaWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        // 设置处理顺序，数字越小优先级越高
        mapping.setOrder(1);
        return mapping;
    }
}
