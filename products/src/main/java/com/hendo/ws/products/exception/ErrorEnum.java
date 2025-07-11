package com.hendo.ws.products.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    KAFKA_PRODUCER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E000086", "Kafka producer failed to send message sync"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "E000001", "Validation failed for the provided data"),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "E000002", "Invalid JSON format in request body"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "E000003", "Invalid argument provided"),
    RUNTIME_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E000004", "A runtime error occurred"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "E000005", "Product not found"),
    PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "E000006", "Product already exists");

    private HttpStatus status;
    private String code;
    private String message;

}
