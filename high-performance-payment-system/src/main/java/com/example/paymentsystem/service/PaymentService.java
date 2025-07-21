package com.example.paymentsystem.service;

import com.example.paymentsystem.dto.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用于处理支付业务逻辑的服务类。
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final String PAYMENT_TOPIC = "payment-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public PaymentService(KafkaTemplate<String, String> kafkaTemplate, StringRedisTemplate redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 处理支付请求，将其发送到 Kafka 主题，并在 Redis 中缓存初始状态。
     *
     * @param paymentRequest 要处理的支付请求。
     */
    public void processPayment(PaymentRequest paymentRequest) {
        // 为简单起见，我们将支付请求序列化为字符串。
        // 在实际应用中，您可能会使用更健壮的序列化格式，如 JSON 或 Avro。
        String paymentEvent = paymentRequest.toString();

        // 将支付事件发送到 Kafka 主题。
        logger.info("向 Kafka 发送支付事件: {}", paymentEvent);
        kafkaTemplate.send(PAYMENT_TOPIC, paymentRequest.getOrderId(), paymentEvent);

        // 使用 Redis Streams 存储支付的初始状态。
        // 这允许快速查找，并可用于事件溯源。
        redisTemplate.opsForStream().add(paymentRequest.getOrderId(), Map.of("status", "PENDING", "request", paymentEvent));
        logger.info("支付请求 {} 已在 Redis 中存储，状态为 PENDING。", paymentRequest.getOrderId());
    }
}