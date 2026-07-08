package com.mercadoaurora.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public record StockReservedEvent(
        UUID skuId,
        UUID warehouseId,
        UUID reservationId,
        int quantity,
        int availableQuantity,
        Instant occurredAt
) implements DomainEvent {
    @Override
    public String eventType() {
        return "inventory.stock.reserved";
    }
}
