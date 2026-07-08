package com.mercadoaurora.inventory.domain;

import com.mercadoaurora.inventory.domain.event.DomainEvent;
import com.mercadoaurora.inventory.domain.event.InventoryItemCreatedEvent;
import com.mercadoaurora.inventory.domain.event.PhysicalStockAdjustedEvent;
import com.mercadoaurora.inventory.domain.event.ReservationCommittedEvent;
import com.mercadoaurora.inventory.domain.event.ReservationExpiredEvent;
import com.mercadoaurora.inventory.domain.event.ReservationReleasedEvent;
import com.mercadoaurora.inventory.domain.event.StockReservedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class InventoryItem {

    private final UUID skuId;
    private final UUID warehouseId;
    private int physicalQuantity;
    private int reservedQuantity;
    private final List<Reservation> reservations;
    private final Instant createdAt;
    private Instant updatedAt;
    private final List<DomainEvent> domainEvents;

    private InventoryItem(
            UUID skuId,
            UUID warehouseId,
            int physicalQuantity,
            int reservedQuantity,
            List<Reservation> reservations,
            Instant createdAt,
            Instant updatedAt,
            List<DomainEvent> domainEvents
    ) {
        this.skuId = requireId(skuId, "skuId");
        this.warehouseId = requireId(warehouseId, "warehouseId");
        this.physicalQuantity = requireNonNegative(physicalQuantity, "physicalQuantity");
        this.reservedQuantity = requireNonNegative(reservedQuantity, "reservedQuantity");
        this.reservations = reservations == null ? new ArrayList<>() : new ArrayList<>(reservations);
        this.createdAt = requireInstant(createdAt, "createdAt");
        this.updatedAt = requireInstant(updatedAt, "updatedAt");
        this.domainEvents = domainEvents == null ? new ArrayList<>() : new ArrayList<>(domainEvents);
        validateInvariants();
    }

    public static InventoryItem create(UUID skuId, UUID warehouseId, int physicalQuantity, Instant now) {
        Instant timestamp = requireInstant(now, "now");
        InventoryItem item = new InventoryItem(
                skuId,
                warehouseId,
                physicalQuantity,
                0,
                List.of(),
                timestamp,
                timestamp,
                new ArrayList<>()
        );
        item.registerEvent(new InventoryItemCreatedEvent(
                item.skuId,
                item.warehouseId,
                item.physicalQuantity,
                item.reservedQuantity,
                timestamp
        ));
        return item;
    }

    public static InventoryItem restore(
            UUID skuId,
            UUID warehouseId,
            int physicalQuantity,
            int reservedQuantity,
            List<Reservation> reservations,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new InventoryItem(
                skuId,
                warehouseId,
                physicalQuantity,
                reservedQuantity,
                reservations,
                createdAt,
                updatedAt,
                new ArrayList<>()
        );
    }

    public void adjustPhysicalStock(int quantityDelta, Instant now) {
        if (quantityDelta == 0) {
            throw new DomainValidationException("quantityDelta must not be zero");
        }

        this.physicalQuantity = safeAdd(physicalQuantity, quantityDelta, "physicalQuantity");
        this.updatedAt = requireInstant(now, "now");
        validateInvariants();
        registerEvent(new PhysicalStockAdjustedEvent(
                skuId,
                warehouseId,
                quantityDelta,
                physicalQuantity,
                reservedQuantity,
                this.updatedAt
        ));
    }

    public Reservation reserve(UUID reservationId, int quantity, Instant now) {
        if (quantity <= 0) {
            throw new DomainValidationException("quantity must be greater than zero");
        }
        if (quantity > getAvailableQuantity()) {
            throw new DomainValidationException("Reservation quantity exceeds available quantity");
        }
        if (findReservationById(reservationId).isPresent()) {
            throw new DomainValidationException("Reservation already exists for this inventory item");
        }

        Reservation reservation = Reservation.create(reservationId, quantity, now);
        reservations.add(reservation);
        reservedQuantity = safeAdd(reservedQuantity, quantity, "reservedQuantity");
        updatedAt = requireInstant(now, "now");
        validateInvariants();
        registerEvent(new StockReservedEvent(
                skuId,
                warehouseId,
                reservation.getId(),
                reservation.getQuantity(),
                getAvailableQuantity(),
                updatedAt
        ));
        return reservation;
    }

    public Reservation commitReservation(UUID reservationId, Instant now) {
        Reservation reservation = getReservationRequired(reservationId);
        reservation.commit(now);
        reservedQuantity -= reservation.getQuantity();
        physicalQuantity -= reservation.getQuantity();
        updatedAt = requireInstant(now, "now");
        validateInvariants();
        registerEvent(new ReservationCommittedEvent(
                skuId,
                warehouseId,
                reservation.getId(),
                reservation.getQuantity(),
                physicalQuantity,
                reservedQuantity,
                updatedAt
        ));
        return reservation;
    }

    public Reservation releaseReservation(UUID reservationId, Instant now) {
        Reservation reservation = getReservationRequired(reservationId);
        reservation.release(now);
        reservedQuantity -= reservation.getQuantity();
        updatedAt = requireInstant(now, "now");
        validateInvariants();
        registerEvent(new ReservationReleasedEvent(
                skuId,
                warehouseId,
                reservation.getId(),
                reservation.getQuantity(),
                getAvailableQuantity(),
                updatedAt
        ));
        return reservation;
    }

    public Reservation expireReservation(UUID reservationId, Instant now) {
        Reservation reservation = getReservationRequired(reservationId);
        reservation.expire(now);
        reservedQuantity -= reservation.getQuantity();
        updatedAt = requireInstant(now, "now");
        validateInvariants();
        registerEvent(new ReservationExpiredEvent(
                skuId,
                warehouseId,
                reservation.getId(),
                reservation.getQuantity(),
                getAvailableQuantity(),
                updatedAt
        ));
        return reservation;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> snapshot = List.copyOf(domainEvents);
        domainEvents.clear();
        return snapshot;
    }

    public Optional<Reservation> findReservationById(UUID reservationId) {
        if (reservationId == null) {
            return Optional.empty();
        }
        return reservations.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst();
    }

    public int getAvailableQuantity() {
        return physicalQuantity - reservedQuantity;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public int getPhysicalQuantity() {
        return physicalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public List<Reservation> getReservations() {
        return List.copyOf(reservations);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private Reservation getReservationRequired(UUID reservationId) {
        return findReservationById(reservationId)
                .orElseThrow(() -> new DomainValidationException("Reservation not found for this inventory item"));
    }

    private void validateInvariants() {
        if (physicalQuantity < 0) {
            throw new DomainValidationException("physicalQuantity must be greater than or equal to zero");
        }
        if (reservedQuantity < 0) {
            throw new DomainValidationException("reservedQuantity must be greater than or equal to zero");
        }
        if (reservedQuantity > physicalQuantity) {
            throw new DomainValidationException("reservedQuantity must be less than or equal to physicalQuantity");
        }

        int openReservationsQuantity = reservations.stream()
                .filter(Reservation::isOpen)
                .mapToInt(Reservation::getQuantity)
                .sum();

        if (openReservationsQuantity != reservedQuantity) {
            throw new DomainValidationException("reservedQuantity must match sum of open reservations");
        }
    }

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    private static UUID requireId(UUID id, String field) {
        if (id == null) {
            throw new DomainValidationException(field + " is required");
        }
        return id;
    }

    private static int requireNonNegative(int value, String field) {
        if (value < 0) {
            throw new DomainValidationException(field + " must be greater than or equal to zero");
        }
        return value;
    }

    private static Instant requireInstant(Instant instant, String field) {
        if (instant == null) {
            throw new DomainValidationException(field + " is required");
        }
        return instant;
    }

    private static int safeAdd(int currentValue, int delta, String field) {
        try {
            return Math.addExact(currentValue, delta);
        } catch (ArithmeticException exception) {
            throw new DomainValidationException(field + " overflow");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItem that)) {
            return false;
        }
        return Objects.equals(skuId, that.skuId) && Objects.equals(warehouseId, that.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId, warehouseId);
    }
}
