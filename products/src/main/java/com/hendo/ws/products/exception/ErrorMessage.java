package com.hendo.ws.products.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorMessage {

    @Schema(description = "Unique request identifier for tracking")
    private UUID requestId;

    @Schema(description = "Timestamp when the error occurred")
    private Date timestamp;

    @Schema(description = "Human-readable error message")
    private String message;

    @Schema(description = "Application-specific error code")
    private String errorCode;

}
