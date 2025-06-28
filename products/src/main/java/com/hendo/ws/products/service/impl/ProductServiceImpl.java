package com.hendo.ws.products.service.impl;

import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;
import com.hendo.ws.products.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        // Generate a unique product ID
        String productId = UUID.randomUUID().toString();
        
        // TODO: Add actual product creation logic here
        // For now, just return the response with generated ID
        
        return new CreateProductResponse(productId);
    }
}
