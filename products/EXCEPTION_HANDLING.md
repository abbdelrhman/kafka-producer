# Exception Handling System

This project implements a comprehensive global exception handling system that provides consistent error responses across all endpoints.

## Architecture Overview

The system uses a **single parent class approach** where all custom exceptions extend `CustomException`. All error information (status, error code, message) comes from `ErrorEnum`, and `requestId` is extracted from `WebRequest` (mandatory for all controllers). No static values are written in the code.

## Core Principles

1. **All values from ErrorEnum**: Status, error code, and message are defined in `ErrorEnum`
2. **RequestId from WebRequest**: Extracted from request headers (mandatory for controllers)
3. **No static values**: No hardcoded error messages or codes in exception classes
4. **Single handler**: GlobalExceptionHandler only handles `CustomException`

## Components

### 1. GlobalExceptionHandler
Located at: `src/main/java/com/hendo/ws/products/exception/GlobalExceptionHandler.java`

This class handles all exceptions thrown in the application and converts them into standardized `ErrorMessage` responses.

**Key Features:**
- **Single Handler**: Only handles `CustomException` for all custom exceptions
- **RequestId Extraction**: Extracts requestId from WebRequest headers
- **Automatic Data Extraction**: Gets timestamp, message, and errorCode from the exception
- **Status Code Mapping**: Automatically maps error codes to appropriate HTTP status codes
- **Fallback Handlers**: Handles framework exceptions (validation, JSON parsing, etc.)

### 2. CustomException (Parent Class)
Located at: `src/main/java/com/hendo/ws/products/exception/CustomException.java`

**Core class that contains error information from ErrorEnum:**
- `timestamp` - When the error occurred (auto-generated)
- `errorCode` - Application-specific error code (from ErrorEnum)
- `message` - Human-readable error message (from ErrorEnum or custom)

**Constructors:**
```java
CustomException(String errorCode)                           // Uses ErrorEnum message
CustomException(String errorCode, Throwable cause)         // Uses ErrorEnum message
CustomException(String errorCode, String customMessage)    // Uses custom message
CustomException(String errorCode, String customMessage, Throwable cause)
```

### 3. ErrorEnum
Located at: `src/main/java/com/hendo/ws/products/exception/ErrorEnum.java`

**Central source of truth for all error information:**
- `status` - HTTP status code
- `code` - Application-specific error code
- `message` - Default error message

**Current Error Codes:**
- `E000001` - Validation Error (400)
- `E000002` - Invalid JSON Format (400)
- `E000003` - Illegal Argument (400)
- `E000004` - Runtime Error (500)
- `E000005` - Product Not Found (404)
- `E000006` - Product Already Exists (409)
- `E000086` - Kafka Producer Failed (500)

### 4. Specific Custom Exceptions

All custom exceptions extend `CustomException` and use ErrorEnum values.

#### KafkaProducerException
```java
// Basic usage (uses ErrorEnum message)
throw new KafkaProducerException();



## Usage

### In Controllers
Controllers must include `requestId` header (mandatory):

```java
@PostMapping
public ResponseEntity<CreateProductResponse> createProduct(@RequestHeader UUID requestId, @RequestBody CreateProductRequest request) {
    // No try-catch needed - exceptions are handled globally
    // requestId is automatically extracted and used in error responses
    CreateProductResponse response = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

### In Services
Use custom exceptions with ErrorEnum values:

```java
@Override
public CreateProductResponse createProduct(CreateProductRequest request) {
    try {
        // Business logic
        return new CreateProductResponse(productId);
    } catch (Exception e) {
        // Uses ErrorEnum.KAFKA_PRODUCER_FAILED.getCode() and default message
        throw new KafkaProducerException();
    }
}

### Creating New Custom Exceptions

**Step 1: Add Error Code to ErrorEnum**
```java
NEW_ERROR(HttpStatus.BAD_REQUEST, "E000007", "New error description")
```

**Step 2: Create Exception Class**
```java
public class MyCustomException extends CustomException {
    
    public MyCustomException() {
        super(ErrorEnum.NEW_ERROR.getCode(),ErrorEnum.NEW_ERROR.getMessage());
    }
    
    
}
```

**Step 3: Use in Code**
```java
// Uses ErrorEnum message
throw new MyCustomException();
```

**That's it!** The GlobalExceptionHandler automatically handles it - no additional configuration needed.

## Error Response Format

All error responses follow this format:

```json
{
  "requestId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T10:30:00.000Z",
  "message": "Error description from ErrorEnum or custom",
  "errorCode": "E000001"
}
```

## Request ID Handling

- **Mandatory**: All controllers must include `requestId` header
- **Automatic Extraction**: GlobalExceptionHandler extracts from WebRequest
- **Error Response**: Included in all error responses for tracking
- **Fallback**: Generates new UUID if header is missing or invalid

## Benefits

1. **Centralized Configuration**: All error information in ErrorEnum
2. **No Static Values**: No hardcoded messages or codes in exceptions
3. **Consistency**: All custom exceptions follow the same pattern
4. **Maintainability**: Easy to add new exception types
5. **Clean Code**: Controllers are free of try-catch blocks
6. **Type Safety**: Compile-time checking for error codes
7. **Request Tracking**: Mandatory requestId for all operations
8. **Extensibility**: Easy to add new error types without modifying the handler

## Migration Guide

When adding new exceptions:

1. **Add to ErrorEnum** - Define status, error code, and message
2. **Create Exception Class** - Extend CustomException with ErrorEnum values
3. **Add Static Factory Methods** - For common use cases (optional)
4. **Use in Code** - Throw the exception where needed

No changes to GlobalExceptionHandler are required! 