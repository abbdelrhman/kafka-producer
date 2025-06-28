package com.hendo.ws.products.service;

import com.hendo.ws.products.dto.req.CreateProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    public ResponseEntity<Object> createProduct(CreateProductRequest createProductRequest);
}
