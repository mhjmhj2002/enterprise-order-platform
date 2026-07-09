package com.mercadoaurora.order.domain.event;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record OrderStockReservedEvent(UUID orderId, List<UUID> reservationRefs, Instant occurredAt) implements OrderEvent {
}
