package com.mercadoaurora.inventory.api.mapper;

import com.mercadoaurora.inventory.api.dto.InventoryItemResponse;
import com.mercadoaurora.inventory.api.dto.InventoryReservationResponse;
import com.mercadoaurora.inventory.domain.InventoryItem;
import com.mercadoaurora.inventory.domain.Reservation;

public final class InventoryApiMapper {

    private InventoryApiMapper() {
    }

    public static InventoryItemResponse toResponse(InventoryItem inventoryItem) {
        return new InventoryItemResponse(
                inventoryItem.getSkuId(),
                inventoryItem.getWarehouseId(),
                inventoryItem.getPhysicalQuantity(),
                inventoryItem.getReservedQuantity(),
                inventoryItem.getAvailableQuantity(),
                inventoryItem.getReservations().stream().map(InventoryApiMapper::toResponse).toList(),
                inventoryItem.getCreatedAt(),
                inventoryItem.getUpdatedAt()
        );
    }

    public static InventoryReservationResponse toResponse(Reservation reservation) {
        return new InventoryReservationResponse(
                reservation.getId(),
                reservation.getQuantity(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
