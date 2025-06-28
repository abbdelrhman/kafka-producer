package com.hendo.ws.products.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.product-created.name}")
    private String productCreatedTopicName;

    @Value("${kafka.topic.product-created.partitions}")
    private Integer partitions;

    @Value("${kafka.topic.product-created.replicas}")
    private Integer replicas;

    @Value("${kafka.topic.product-created.min-insync-replicas}")
    private String minInsyncReplicas;

    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name(productCreatedTopicName)
                .partitions(partitions)
                .replicas(replicas)
                .config("min.insync.replicas", minInsyncReplicas)
                .build();
    }
}
