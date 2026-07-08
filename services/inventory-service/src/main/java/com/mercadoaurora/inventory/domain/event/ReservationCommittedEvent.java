package com.mercadoaurora.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ReservationCommittedEvent(
        UUID skuId,
        UUID warehouseId,
        UUID reservationId,
        int quantity,
        int physicalQuantity,
        int reservedQuantity,
        Instant occurredAt
) implements DomainEvent {
    @Override
    public String eventType() {
        return "inventory.reservation.committed";
    }
}
