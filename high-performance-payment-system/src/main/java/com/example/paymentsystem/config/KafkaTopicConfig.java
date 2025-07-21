package com.example.paymentsystem.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka 主题配置。
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * 创建一个名为 payment-events 的主题，包含3个分区。
     *
     * @return NewTopic 对象。
     */
    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name("payment-events")
                .partitions(3)      // 设置分区数为3，以提高并行度
                .replicas(1)        // 设置副本数为1（在生产环境中建议设置为更高的值，例如3）
                .build();
    }
}