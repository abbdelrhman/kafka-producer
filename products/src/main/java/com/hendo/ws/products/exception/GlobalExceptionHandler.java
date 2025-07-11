package com.hendo.ws.products.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> handleCustomException(CustomException ex, WebRequest request) {
        log.error("Custom exception occurred: ", ex);

        // Extract requestId from WebRequest (mandatory for controllers)
        UUID requestId = extractRequestId(request);

        // Use the values from the CustomException directly
        ErrorMessage error = new ErrorMessage(
                requestId,
                ex.getTimestamp(),
                ex.getMessage(),
                ex.getErrorCode());

        // Try to find matching error enum for status code
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default
        for (ErrorEnum errorEnum : ErrorEnum.values()) {
            if (errorEnum.getCode().equals(ex.getErrorCode())) {
                status = errorEnum.getStatus();
                break;
            }
        }

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex,
            WebRequest request) {
        log.error("Validation error: ", ex);

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage()
                : ErrorEnum.VALIDATION_ERROR.getMessage();

        ErrorMessage error = new ErrorMessage(
                extractRequestId(request),
                new Date(),
                errorMessage,
                ErrorEnum.VALIDATION_ERROR.getCode());

        return ResponseEntity
                .status(ErrorEnum.VALIDATION_ERROR.getStatus())
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            WebRequest request) {
        log.error("Invalid JSON format: ", ex);

        ErrorMessage error = new ErrorMessage(
                extractRequestId(request),
                new Date(),
                ErrorEnum.INVALID_JSON_FORMAT.getMessage(),
                ErrorEnum.INVALID_JSON_FORMAT.getCode());

        return ResponseEntity
                .status(ErrorEnum.INVALID_JSON_FORMAT.getStatus())
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        log.error("Illegal argument: ", ex);

        ErrorMessage error = new ErrorMessage(
                extractRequestId(request),
                new Date(),
                ex.getMessage(),
                ErrorEnum.ILLEGAL_ARGUMENT.getCode());

        return ResponseEntity
                .status(ErrorEnum.ILLEGAL_ARGUMENT.getStatus())
                .body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime error: ", ex);

        ErrorMessage error = new ErrorMessage(
                extractRequestId(request),
                new Date(),
                ErrorEnum.RUNTIME_ERROR.getMessage(),
                ErrorEnum.RUNTIME_ERROR.getCode());

        return ResponseEntity
                .status(ErrorEnum.RUNTIME_ERROR.getStatus())
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ErrorMessage errorMessage = new ErrorMessage(
                extractRequestId(request),
                new Date(),
                "An unexpected error occurred. Please try again later.",
                ErrorEnum.KAFKA_PRODUCER_FAILED.getCode());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessage);
    }

    private UUID extractRequestId(WebRequest request) {
        try {
            String requestIdHeader = request.getHeader("requestId");
            return requestIdHeader != null ? UUID.fromString(requestIdHeader) : UUID.randomUUID();
        } catch (Exception e) {
            log.warn("Could not extract request ID from header, generating new one", e);
            return UUID.randomUUID();
        }
    }
}