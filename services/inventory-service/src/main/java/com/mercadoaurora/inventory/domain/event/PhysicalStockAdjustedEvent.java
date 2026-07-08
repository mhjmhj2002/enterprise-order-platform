package com.mercadoaurora.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PhysicalStockAdjustedEvent(
        UUID skuId,
        UUID warehouseId,
        int quantityDelta,
        int physicalQuantity,
        int reservedQuantity,
        Instant occurredAt
) implements DomainEvent {
    @Override
    public String eventType() {
        return "inventory.physical-stock.adjusted";
    }
}
