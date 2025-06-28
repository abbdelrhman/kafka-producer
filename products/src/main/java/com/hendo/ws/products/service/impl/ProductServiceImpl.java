package com.hendo.ws.products.service.impl;

import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ResponseEntity<Object> createProduct(CreateProductRequest createProductRequest) {
        return null;
    }
}
