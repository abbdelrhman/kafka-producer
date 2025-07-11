package com.hendo.ws.products.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// test // test
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for creating a new product")
public class CreateProductRequest {

    @Schema(description = "Product title", example = "iPhone 15 Pro", required = true)
    private String title;
    
    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;
    
    @Schema(description = "Available quantity", example = "10", required = true)
    private int quantity;
}
