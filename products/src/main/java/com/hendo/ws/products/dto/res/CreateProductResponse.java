package com.hendo.ws.products.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "the response of product creation")
public class CreateProductResponse {

    @Schema(description = "the unique identifier of the created product")
    private String productId;
}
