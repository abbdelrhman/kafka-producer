package com.hendo.ws.products.exception;

public class KafkaProducerException extends CustomException {
    public KafkaProducerException() {
        super(ErrorEnum.KAFKA_PRODUCER_FAILED.getCode(), ErrorEnum.KAFKA_PRODUCER_FAILED.getMessage());
    }
}