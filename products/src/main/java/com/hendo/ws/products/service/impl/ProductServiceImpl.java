package com.hendo.ws.products.service.impl;

import com.hendo.ws.products.dto.event.ProductCreatedEvent;
import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;
import com.hendo.ws.products.exception.ErrorEnum;
import com.hendo.ws.products.exception.KafkaProducerException;
import com.hendo.ws.products.mapper.ProductMapper;
import com.hendo.ws.products.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        ProductCreatedEvent productCreatedEvent = productMapper.toProductCreatedEvent(createProductRequest,
                productId);

        // TODO: Add actual product creation logic here
        // TODO: Publish the ProductCreatedEvent to Kafka
        // kafkaTemplate.send takes the following -> topicName, key, value

        try {
            SendResult<String, ProductCreatedEvent> resultCompletableFuture = kafkaTemplate
                    .send(productCreatedTopicName, productId, productCreatedEvent).get();
            log.info("Product created successfully with ID: {}", productId);
        } catch (Exception e) {
            log.error("Failed to create product: ", e);
            throw new KafkaProducerException();
        }
        return new CreateProductResponse(productId);

    }
}
