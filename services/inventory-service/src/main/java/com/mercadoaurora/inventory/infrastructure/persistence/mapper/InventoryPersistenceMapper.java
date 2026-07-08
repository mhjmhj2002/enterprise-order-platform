package com.mercadoaurora.inventory.infrastructure.persistence.mapper;

import com.mercadoaurora.inventory.domain.InventoryItem;
import com.mercadoaurora.inventory.domain.Reservation;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemKey;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.ReservationEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class InventoryPersistenceMapper {

    private InventoryPersistenceMapper() {
    }

    public static InventoryItem toDomain(InventoryItemEntity entity) {
        return InventoryItem.restore(
                entity.getId().getSkuId(),
                entity.getId().getWarehouseId(),
                entity.getPhysicalQuantity(),
                entity.getReservedQuantity(),
                entity.getReservations().stream().map(InventoryPersistenceMapper::toDomain).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static InventoryItemEntity toEntity(InventoryItem inventoryItem, InventoryItemEntity entity) {
        entity.setId(new InventoryItemKey(inventoryItem.getSkuId(), inventoryItem.getWarehouseId()));
        entity.setPhysicalQuantity(inventoryItem.getPhysicalQuantity());
        entity.setReservedQuantity(inventoryItem.getReservedQuantity());
        entity.setCreatedAt(inventoryItem.getCreatedAt());
        entity.setUpdatedAt(inventoryItem.getUpdatedAt());

        List<ReservationEntity> reservationEntities = inventoryItem.getReservations().stream()
                .map(reservation -> toEntity(reservation, entity))
                .collect(Collectors.toCollection(java.util.ArrayList::new));

        entity.getReservations().clear();
        entity.getReservations().addAll(reservationEntities);
        return entity;
    }

    private static Reservation toDomain(ReservationEntity entity) {
        return Reservation.restore(
                entity.getId(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private static ReservationEntity toEntity(Reservation reservation, InventoryItemEntity inventoryItemEntity) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(reservation.getId());
        entity.setInventoryItem(inventoryItemEntity);
        entity.setQuantity(reservation.getQuantity());
        entity.setStatus(reservation.getStatus());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        return entity;
    }
}
