package com.hendo.ws.products.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class CustomException extends RuntimeException {

    private final Date timestamp;
    private final String errorCode;
    private final String message;

    public CustomException(String errorCode) {
        super(getErrorMessage(errorCode));
        this.timestamp = new Date();
        this.errorCode = errorCode;
        this.message = getErrorMessage(errorCode);
    }

    public CustomException(String errorCode, Throwable cause) {
        super(getErrorMessage(errorCode), cause);
        this.timestamp = new Date();
        this.errorCode = errorCode;
        this.message = getErrorMessage(errorCode);
    }

    public CustomException(String errorCode, String customMessage) {
        super(customMessage);
        this.timestamp = new Date();
        this.errorCode = errorCode;
        this.message = customMessage;
    }

    public CustomException(String errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.timestamp = new Date();
        this.errorCode = errorCode;
        this.message = customMessage;
    }

    private static String getErrorMessage(String errorCode) {
        for (ErrorEnum errorEnum : ErrorEnum.values()) {
            if (errorEnum.getCode().equals(errorCode)) {
                return errorEnum.getMessage();
            }
        }
        return "Unknown error occurred";
    }
}