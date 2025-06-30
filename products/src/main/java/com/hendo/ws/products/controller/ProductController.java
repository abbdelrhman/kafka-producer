package com.hendo.ws.products.controller;

import com.hendo.ws.products.dto.req.CreateProductRequest;
import com.hendo.ws.products.dto.res.CreateProductResponse;
import com.hendo.ws.products.exception.ErrorEnum;
import com.hendo.ws.products.exception.ErrorMessage;
import com.hendo.ws.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Tag(name = "Product API", description = "Manage products")
@RestController
@RequestMapping("v1/products")
@AllArgsConstructor
public class ProductController {

    ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details and returns the product ID. The product creation event will be published to Kafka for downstream processing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = CreateProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data or validation error", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Kafka producer failed or unexpected error", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(@RequestHeader(name = "x-request-id") UUID requestId,
            @RequestBody CreateProductRequest createProductRequest) {
        CreateProductResponse response = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
