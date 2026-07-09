package com.mercadoaurora.order.domain.event;
import java.time.Instant;
import java.util.UUID;
public record OrderPaidEvent(UUID orderId, Instant occurredAt) implements OrderEvent {
}
