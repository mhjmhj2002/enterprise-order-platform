package com.mercadoaurora.order.infrastructure.event;

public class OrderEventPublicationException extends RuntimeException {
    public OrderEventPublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
