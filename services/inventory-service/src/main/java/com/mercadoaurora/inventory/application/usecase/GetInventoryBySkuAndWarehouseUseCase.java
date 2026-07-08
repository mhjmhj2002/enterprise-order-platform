package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.exception.InventoryItemNotFoundException;
import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetInventoryBySkuAndWarehouseUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public GetInventoryBySkuAndWarehouseUseCase(InventoryRepositoryPort inventoryRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    public InventoryItem execute(UUID skuId, UUID warehouseId) {
        return inventoryRepositoryPort.findBySkuAndWarehouse(skuId, warehouseId)
                .orElseThrow(() -> new InventoryItemNotFoundException(skuId, warehouseId));
    }
}
