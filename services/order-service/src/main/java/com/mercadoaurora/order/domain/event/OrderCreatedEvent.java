package com.mercadoaurora.order.domain.event;
import java.time.Instant;
import java.util.UUID;
public record OrderCreatedEvent(UUID orderId, UUID customerId, Instant occurredAt) implements OrderEvent {
}
