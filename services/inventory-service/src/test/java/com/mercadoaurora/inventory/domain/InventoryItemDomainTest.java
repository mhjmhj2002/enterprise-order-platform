package com.mercadoaurora.inventory.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryItemDomainTest {

    @Test
    void shouldReserveStockWhenQuantityIsAvailable() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, now);

        inventoryItem.reserve(UUID.randomUUID(), 4, now);

        assertEquals(10, inventoryItem.getPhysicalQuantity());
        assertEquals(4, inventoryItem.getReservedQuantity());
        assertEquals(6, inventoryItem.getAvailableQuantity());
    }

    @Test
    void shouldNotReserveWhenQuantityExceedsAvailable() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 5, now);

        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> inventoryItem.reserve(UUID.randomUUID(), 6, now));

        assertEquals("Reservation quantity exceeds available quantity", exception.getMessage());
    }

    @Test
    void shouldCommitReservationAndDecreasePhysicalAndReserved() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 8, now);
        UUID reservationId = UUID.randomUUID();
        inventoryItem.reserve(reservationId, 3, now);

        inventoryItem.commitReservation(reservationId, now.plusSeconds(60));

        assertEquals(5, inventoryItem.getPhysicalQuantity());
        assertEquals(0, inventoryItem.getReservedQuantity());
        assertEquals(5, inventoryItem.getAvailableQuantity());
        assertEquals(ReservationStatus.COMMITTED, inventoryItem.findReservationById(reservationId).orElseThrow().getStatus());
    }

    @Test
    void shouldReleaseReservationAndRestoreAvailability() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 8, now);
        UUID reservationId = UUID.randomUUID();
        inventoryItem.reserve(reservationId, 3, now);

        inventoryItem.releaseReservation(reservationId, now.plusSeconds(60));

        assertEquals(8, inventoryItem.getPhysicalQuantity());
        assertEquals(0, inventoryItem.getReservedQuantity());
        assertEquals(8, inventoryItem.getAvailableQuantity());
        assertEquals(ReservationStatus.RELEASED, inventoryItem.findReservationById(reservationId).orElseThrow().getStatus());
    }

    @Test
    void shouldAlwaysDeriveAvailableQuantityFromPhysicalAndReserved() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 20, now);
        UUID reservationId = UUID.randomUUID();
        inventoryItem.reserve(reservationId, 5, now);

        assertEquals(inventoryItem.getPhysicalQuantity() - inventoryItem.getReservedQuantity(), inventoryItem.getAvailableQuantity());

        inventoryItem.adjustPhysicalStock(3, now.plusSeconds(60));
        assertEquals(inventoryItem.getPhysicalQuantity() - inventoryItem.getReservedQuantity(), inventoryItem.getAvailableQuantity());
    }

    @Test
    void shouldRejectInvalidReservationStateTransitions() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        InventoryItem inventoryItem = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 8, now);
        UUID reservationId = UUID.randomUUID();
        inventoryItem.reserve(reservationId, 2, now);
        inventoryItem.commitReservation(reservationId, now.plusSeconds(60));

        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> inventoryItem.releaseReservation(reservationId, now.plusSeconds(120)));

        assertEquals("Reservation is finalized and cannot change state", exception.getMessage());
    }
}
