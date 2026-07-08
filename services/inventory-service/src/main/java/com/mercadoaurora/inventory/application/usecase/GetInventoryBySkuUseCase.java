package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetInventoryBySkuUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public GetInventoryBySkuUseCase(InventoryRepositoryPort inventoryRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    public List<InventoryItem> execute(UUID skuId) {
        return inventoryRepositoryPort.findBySku(skuId);
    }
}
