package com.mercadoaurora.inventory.application.command;

import java.util.UUID;

public record ReleaseReservationCommand(
        UUID skuId,
        UUID warehouseId,
        UUID reservationId
) {
}
