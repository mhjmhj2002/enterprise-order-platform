package com.mercadoaurora.inventory.application.port.out;

import com.mercadoaurora.inventory.domain.InventoryItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepositoryPort {

    InventoryItem save(InventoryItem inventoryItem);

    Optional<InventoryItem> findBySkuAndWarehouse(UUID skuId, UUID warehouseId);

    List<InventoryItem> findBySku(UUID skuId);
}
