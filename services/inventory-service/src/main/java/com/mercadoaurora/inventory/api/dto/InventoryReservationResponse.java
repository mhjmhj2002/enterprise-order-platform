package com.mercadoaurora.inventory.api.dto;

import com.mercadoaurora.inventory.domain.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservationResponse(
        UUID reservationId,
        int quantity,
        ReservationStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
