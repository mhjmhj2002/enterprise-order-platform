package com.mercadoaurora.inventory.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Reservation {

    private final UUID id;
    private final int quantity;
    private ReservationStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Reservation(UUID id, int quantity, ReservationStatus status, Instant createdAt, Instant updatedAt) {
        this.id = requireId(id, "reservationId");
        this.quantity = requirePositive(quantity, "quantity");
        this.status = status == null ? ReservationStatus.CREATED : status;
        this.createdAt = requireInstant(createdAt, "createdAt");
        this.updatedAt = requireInstant(updatedAt, "updatedAt");
    }

    public static Reservation create(UUID reservationId, int quantity, Instant now) {
        Instant timestamp = requireInstant(now, "now");
        return new Reservation(reservationId, quantity, ReservationStatus.CREATED, timestamp, timestamp);
    }

    public static Reservation restore(
            UUID reservationId,
            int quantity,
            ReservationStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Reservation(reservationId, quantity, status, createdAt, updatedAt);
    }

    public void commit(Instant now) {
        transitionTo(ReservationStatus.COMMITTED, now);
    }

    public void release(Instant now) {
        transitionTo(ReservationStatus.RELEASED, now);
    }

    public void expire(Instant now) {
        transitionTo(ReservationStatus.EXPIRED, now);
    }

    private void transitionTo(ReservationStatus targetStatus, Instant now) {
        requireInstant(now, "now");

        if (status != ReservationStatus.CREATED) {
            throw new DomainValidationException("Reservation is finalized and cannot change state");
        }

        if (targetStatus == ReservationStatus.CREATED) {
            throw new DomainValidationException("Invalid reservation state transition");
        }

        this.status = targetStatus;
        this.updatedAt = now;
    }

    public UUID getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isOpen() {
        return status == ReservationStatus.CREATED;
    }

    private static UUID requireId(UUID id, String field) {
        if (id == null) {
            throw new DomainValidationException(field + " is required");
        }
        return id;
    }

    private static int requirePositive(int value, String field) {
        if (value <= 0) {
            throw new DomainValidationException(field + " must be greater than zero");
        }
        return value;
    }

    private static Instant requireInstant(Instant instant, String field) {
        if (instant == null) {
            throw new DomainValidationException(field + " is required");
        }
        return instant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
