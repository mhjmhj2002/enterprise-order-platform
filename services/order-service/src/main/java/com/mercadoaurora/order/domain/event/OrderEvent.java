package com.mercadoaurora.order.domain.event;
import java.time.Instant;
import java.util.UUID;
public interface OrderEvent {
    UUID orderId();
    Instant occurredAt();
}
