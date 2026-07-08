package com.mercadoaurora.inventory.application.exception;

import java.util.UUID;

public class InventoryConflictException extends RuntimeException {

    public InventoryConflictException(UUID skuId, UUID warehouseId) {
        super("Inventory item already exists for skuId=%s and warehouseId=%s".formatted(skuId, warehouseId));
    }
}
