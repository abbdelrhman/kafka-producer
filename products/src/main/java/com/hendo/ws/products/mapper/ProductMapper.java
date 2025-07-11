package com.hendo.ws.products.mapper;

import com.hendo.ws.products.dto.event.ProductCreatedEvent;
import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    /**
     * Maps CreateProductRequest to ProductCreatedEvent
     * 
     * @param createProductRequest the source object
     * @return ProductCreatedEvent
     */
    ProductCreatedEvent toProductCreatedEvent(CreateProductRequest createProductRequest, String productId);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    Product toProductEntity(CreateProductRequest createProductRequest);
}