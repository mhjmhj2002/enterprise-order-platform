package com.mercadoaurora.inventory.application.command;

import java.util.UUID;

public record CommitReservationCommand(
        UUID skuId,
        UUID warehouseId,
        UUID reservationId
) {
}
