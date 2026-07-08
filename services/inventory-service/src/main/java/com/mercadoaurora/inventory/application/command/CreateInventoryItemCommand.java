package com.mercadoaurora.inventory.application.command;

import java.util.UUID;

public record CreateInventoryItemCommand(
        UUID skuId,
        UUID warehouseId,
        int physicalQuantity
) {
}
