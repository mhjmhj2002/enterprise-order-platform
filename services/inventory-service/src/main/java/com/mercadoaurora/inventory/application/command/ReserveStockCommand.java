package com.mercadoaurora.inventory.application.command;

import java.util.UUID;

public record ReserveStockCommand(
        UUID skuId,
        UUID warehouseId,
        UUID reservationId,
        int quantity
) {
}
