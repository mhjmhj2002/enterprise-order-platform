package com.mercadoaurora.order.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentApprovedEvent(UUID orderId, Instant occurredAt) implements OrderEvent {
}
