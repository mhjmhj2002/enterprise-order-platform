package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.ReleaseReservationCommand;
import com.mercadoaurora.inventory.application.exception.InventoryItemNotFoundException;
import com.mercadoaurora.inventory.application.exception.ReservationNotFoundException;
import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class ReleaseReservationUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final Clock clock;

    public ReleaseReservationUseCase(InventoryRepositoryPort inventoryRepositoryPort, Clock clock) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public InventoryItem execute(ReleaseReservationCommand command) {
        InventoryItem inventoryItem = inventoryRepositoryPort.findBySkuAndWarehouse(command.skuId(), command.warehouseId())
                .orElseThrow(() -> new InventoryItemNotFoundException(command.skuId(), command.warehouseId()));

        inventoryItem.findReservationById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(
                        command.reservationId(),
                        command.skuId(),
                        command.warehouseId()
                ));

        inventoryItem.releaseReservation(command.reservationId(), Instant.now(clock));
        return inventoryRepositoryPort.save(inventoryItem);
    }
}
