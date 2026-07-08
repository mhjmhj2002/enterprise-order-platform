package com.mercadoaurora.inventory.application.command;

import java.util.UUID;

public record AdjustPhysicalStockCommand(
        UUID skuId,
        UUID warehouseId,
        int quantityDelta
) {
}
