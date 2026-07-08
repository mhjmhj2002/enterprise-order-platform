package com.mercadoaurora.inventory.application.exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(UUID reservationId, UUID skuId, UUID warehouseId) {
        super("Reservation not found for reservationId=%s, skuId=%s, warehouseId=%s"
                .formatted(reservationId, skuId, warehouseId));
    }
}
