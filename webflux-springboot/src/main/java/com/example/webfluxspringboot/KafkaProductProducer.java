
package com.example.webfluxspringboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // 用于对象和 JSON 字符串之间的转换

    public KafkaProductProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 设计模式：生产者 (Producer)
     * 负责将 Product 对象序列化为 JSON 字符串，并发送到 Kafka 的 "products" 主题。
     *
     * @param product 要发送的产品对象。
     */
    public void sendProduct(Product product) {
        try {
            // 步骤 1: 将 Product 对象转换为 JSON 字符串
            String message = objectMapper.writeValueAsString(product);
            // 步骤 2: 发送消息到 Kafka
            kafkaTemplate.send("products", message);

        /*
         * --- 健壮性设计说明 ---
         * 当前的实现存在两个主要的健壮性问题：
         *
         * 1. 异步发送未处理：
         *    kafkaTemplate.send() 方法在默认情况下是异步的。它会立即返回，而不会等待消息是否成功送达 Kafka Broker。
         *    当前 void 返回类型导致调用者无法知道发送结果。在生产环境中，这可能导致消息丢失而系统却毫无察觉。
         *    【改进建议】：send 方法返回一个 CompletableFuture。我们应该使用它来添加回调，处理成功和失败的情况。
         *    例如：
         *    kafkaTemplate.send("products", message).whenComplete((result, ex) -> {
         *        if (ex != null) {
         *            log.error("Failed to send message to Kafka", ex);
         *        } else {
         *            log.info("Message sent successfully to partition {} with offset {}", 
         *                     result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
         *        }
         *    });
         *
         * 2. 异常处理不规范：
         *    catch (JsonProcessingException e) { e.printStackTrace(); } 是一种不推荐的实践。
         *    它只是将错误打印到标准错误流，很难在生产环境中追踪和管理。
         *    【改进建议】：应该使用专业的日志框架（如 SLF4J）来记录错误，并考虑向上抛出一个自定义的运行时异常，
         *    或者返回一个包含操作状态的结果对象，让调用者能响应该错误。
         *    例如：
         *    catch (JsonProcessingException e) {
         *        log.error("Error serializing product {} to JSON", product, e);
         *        throw new MessagingException("Failed to serialize product", e);
         *    }
         */
        } catch (JsonProcessingException e) {
            // 在生产环境中，这里应该被替换为日志记录和异常处理逻辑
            e.printStackTrace();
        }
    }
}
