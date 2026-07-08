package com.mercadoaurora.inventory.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventoryItemResponse(
        UUID skuId,
        UUID warehouseId,
        int physicalQuantity,
        int reservedQuantity,
        int availableQuantity,
        List<InventoryReservationResponse> reservations,
        Instant createdAt,
        Instant updatedAt
) {
}
