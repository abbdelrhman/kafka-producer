package com.hendo.ws.products.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object for product creation operation")
public class CreateProductResponse {

    @Schema(description = "Unique identifier of the created product")
    private String productId;
}
