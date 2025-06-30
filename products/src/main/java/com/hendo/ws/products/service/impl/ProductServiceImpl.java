package com.hendo.ws.products.service.impl;

import com.hendo.ws.products.dto.event.ProductCreatedEvent;
import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;
import com.hendo.ws.products.mapper.ProductMapper;
import com.hendo.ws.products.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Autowired
    @Value("${kafka.topic.product-created.name}")
    private String productCreatedTopicName;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        // Generate a unique product ID
        String productId = UUID.randomUUID().toString();

        // Map CreateProductRequest to ProductCreatedEvent using MapStruct
        ProductCreatedEvent productCreatedEvent = productMapper.toProductCreatedEvent(createProductRequest, productId);

        // TODO: Add actual product creation logic here
        // TODO: Publish the ProductCreatedEvent to Kafka
        //kafkaTemplate.send takes the following -> topicName, key, value
        CompletableFuture<SendResult<String, ProductCreatedEvent>> resultCompletableFuture = kafkaTemplate.send(productCreatedTopicName, productId, productCreatedEvent);
        resultCompletableFuture.whenComplete((result, exception) -> {
            if (exception != null) {
                //fail
                log.error("message failed with exception");
            } else {
                //success
                log.info("message is sent successfully with metadata {}", result.getRecordMetadata());
            }
        });

        /**
         * if you need kafka call to be async just return the result
         * if you need kafka call to by sync you can write the following line
         */

        /**
         * this line blocks the current thread until the Kafka message is either successfully sent or an exception is thrown.
         */
        resultCompletableFuture.join();

        log.info("***************** returning product id");
        return new CreateProductResponse(productId);
    }
}
