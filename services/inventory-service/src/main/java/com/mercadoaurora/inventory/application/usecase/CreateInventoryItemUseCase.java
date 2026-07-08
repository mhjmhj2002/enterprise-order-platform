package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.CreateInventoryItemCommand;
import com.mercadoaurora.inventory.application.exception.InventoryConflictException;
import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class CreateInventoryItemUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final Clock clock;

    public CreateInventoryItemUseCase(InventoryRepositoryPort inventoryRepositoryPort, Clock clock) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public InventoryItem execute(CreateInventoryItemCommand command) {
        inventoryRepositoryPort.findBySkuAndWarehouse(command.skuId(), command.warehouseId())
                .ifPresent(existing -> {
                    throw new InventoryConflictException(command.skuId(), command.warehouseId());
                });

        Instant now = Instant.now(clock);
        InventoryItem inventoryItem = InventoryItem.create(
                command.skuId(),
                command.warehouseId(),
                command.physicalQuantity(),
                now
        );
        return inventoryRepositoryPort.save(inventoryItem);
    }
}
