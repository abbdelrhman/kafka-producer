package com.hendo.ws.products.service;

import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;

public interface ProductService {
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest);
}
