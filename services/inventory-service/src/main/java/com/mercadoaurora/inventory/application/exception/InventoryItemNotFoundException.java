package com.mercadoaurora.inventory.application.exception;

import java.util.UUID;

public class InventoryItemNotFoundException extends RuntimeException {

    public InventoryItemNotFoundException(UUID skuId, UUID warehouseId) {
        super("Inventory item not found for skuId=%s and warehouseId=%s".formatted(skuId, warehouseId));
    }
}
