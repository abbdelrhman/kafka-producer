package com.hendo.ws.products.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
    @Schema(description = "Product title", example = "iPhone 15 Pro", required = true)
    private String titler;

    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;

    @Schema(description = "Available quantity", example = "10", required = true)
    private int quantity;
}
