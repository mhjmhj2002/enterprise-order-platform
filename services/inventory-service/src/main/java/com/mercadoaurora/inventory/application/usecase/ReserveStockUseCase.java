package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.ReserveStockCommand;
import com.mercadoaurora.inventory.application.exception.InventoryItemNotFoundException;
import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class ReserveStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final Clock clock;

    public ReserveStockUseCase(InventoryRepositoryPort inventoryRepositoryPort, Clock clock) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public InventoryItem execute(ReserveStockCommand command) {
        InventoryItem inventoryItem = inventoryRepositoryPort.findBySkuAndWarehouse(command.skuId(), command.warehouseId())
                .orElseThrow(() -> new InventoryItemNotFoundException(command.skuId(), command.warehouseId()));

        UUID reservationId = command.reservationId() == null ? UUID.randomUUID() : command.reservationId();
        inventoryItem.reserve(reservationId, command.quantity(), Instant.now(clock));
        return inventoryRepositoryPort.save(inventoryItem);
    }
}
