package com.hendo.ws.products.service.impl;

import com.hendo.ws.products.dto.event.ProductCreatedEvent;
import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;
import com.hendo.ws.products.entity.Product;
import com.hendo.ws.products.exception.ErrorEnum;
import com.hendo.ws.products.exception.KafkaProducerException;
import com.hendo.ws.products.mapper.ProductMapper;
import com.hendo.ws.products.repository.ProductRepository;
import com.hendo.ws.products.service.ProductService;
import jakarta.transaction.Transactional;
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
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Autowired
    @Value("${kafka.topic.product-created.name}")
    private String productCreatedTopicName;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {

        // Create and save the product entity (ID will be auto-generated)
        Product product = ProductMapper.INSTANCE.toProductEntity(createProductRequest);
        Product savedProduct = productRepository.saveAndFlush(product);

        // Get the auto-generated UUID from the saved entity
        String productId = savedProduct.getId().toString();

        // Map CreateProductRequest to ProductCreatedEvent using MapStruct
        ProductCreatedEvent productCreatedEvent = ProductMapper.INSTANCE.toProductCreatedEvent(createProductRequest,
                productId);

        // Publish the ProductCreatedEvent to Kafka
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
