package com.hendo.ws.products.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .config("min.insync.replicas","2")
                .build();
    }
}
