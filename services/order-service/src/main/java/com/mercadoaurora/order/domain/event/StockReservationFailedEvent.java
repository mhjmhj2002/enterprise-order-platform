package com.mercadoaurora.order.domain.event;

import java.time.Instant;
import java.util.UUID;

public record StockReservationFailedEvent(UUID orderId, Instant occurredAt) implements OrderEvent {
}
