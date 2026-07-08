package com.mercadoaurora.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public record InventoryItemCreatedEvent(
        UUID skuId,
        UUID warehouseId,
        int physicalQuantity,
        int reservedQuantity,
        Instant occurredAt
) implements DomainEvent {
    @Override
    public String eventType() {
        return "inventory.item.created";
    }
}
