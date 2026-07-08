package com.mercadoaurora.inventory.unit;

import com.mercadoaurora.inventory.application.command.AdjustPhysicalStockCommand;
import com.mercadoaurora.inventory.application.command.CommitReservationCommand;
import com.mercadoaurora.inventory.application.command.CreateInventoryItemCommand;
import com.mercadoaurora.inventory.application.command.ReleaseReservationCommand;
import com.mercadoaurora.inventory.application.command.ReserveStockCommand;
import com.mercadoaurora.inventory.application.exception.InventoryConflictException;
import com.mercadoaurora.inventory.application.exception.InventoryItemNotFoundException;
import com.mercadoaurora.inventory.application.exception.ReservationNotFoundException;
import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.application.usecase.AdjustPhysicalStockUseCase;
import com.mercadoaurora.inventory.application.usecase.CommitReservationUseCase;
import com.mercadoaurora.inventory.application.usecase.CreateInventoryItemUseCase;
import com.mercadoaurora.inventory.application.usecase.GetInventoryBySkuAndWarehouseUseCase;
import com.mercadoaurora.inventory.application.usecase.GetInventoryBySkuUseCase;
import com.mercadoaurora.inventory.application.usecase.ReleaseReservationUseCase;
import com.mercadoaurora.inventory.application.usecase.ReserveStockUseCase;
import com.mercadoaurora.inventory.domain.InventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryUseCasesTest {

    @Mock
    private InventoryRepositoryPort repositoryPort;

    private Clock clock;

    @BeforeEach
    void setupClock() {
        clock = Clock.fixed(Instant.parse("2026-07-08T10:00:00Z"), ZoneOffset.UTC);
    }

    @Test
    void shouldCreateInventoryItem() {
        CreateInventoryItemUseCase useCase = new CreateInventoryItemUseCase(repositoryPort, clock);
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        when(repositoryPort.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        InventoryItem created = useCase.execute(new CreateInventoryItemCommand(skuId, warehouseId, 20));

        assertEquals(skuId, created.getSkuId());
        assertEquals(warehouseId, created.getWarehouseId());
        assertEquals(20, created.getPhysicalQuantity());
        verify(repositoryPort).save(any(InventoryItem.class));
    }

    @Test
    void shouldAdjustPhysicalStock() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, Instant.now(clock));
        AdjustPhysicalStockUseCase useCase = new AdjustPhysicalStockUseCase(repositoryPort, clock);

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));
        when(repositoryPort.save(item)).thenReturn(item);

        InventoryItem updated = useCase.execute(new AdjustPhysicalStockCommand(item.getSkuId(), item.getWarehouseId(), 5));
        assertEquals(15, updated.getPhysicalQuantity());
    }

    @Test
    void shouldReserveStock() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, Instant.now(clock));
        ReserveStockUseCase useCase = new ReserveStockUseCase(repositoryPort, clock);
        UUID reservationId = UUID.randomUUID();

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));
        when(repositoryPort.save(item)).thenReturn(item);

        InventoryItem updated = useCase.execute(new ReserveStockCommand(
                item.getSkuId(),
                item.getWarehouseId(),
                reservationId,
                4
        ));
        assertEquals(4, updated.getReservedQuantity());
        assertEquals(6, updated.getAvailableQuantity());
    }

    @Test
    void shouldCommitReservation() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, Instant.now(clock));
        UUID reservationId = UUID.randomUUID();
        item.reserve(reservationId, 3, Instant.now(clock));
        CommitReservationUseCase useCase = new CommitReservationUseCase(repositoryPort, clock);

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));
        when(repositoryPort.save(item)).thenReturn(item);

        InventoryItem updated = useCase.execute(new CommitReservationCommand(item.getSkuId(), item.getWarehouseId(), reservationId));
        assertEquals(7, updated.getPhysicalQuantity());
        assertEquals(0, updated.getReservedQuantity());
    }

    @Test
    void shouldReleaseReservation() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, Instant.now(clock));
        UUID reservationId = UUID.randomUUID();
        item.reserve(reservationId, 3, Instant.now(clock));
        ReleaseReservationUseCase useCase = new ReleaseReservationUseCase(repositoryPort, clock);

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));
        when(repositoryPort.save(item)).thenReturn(item);

        InventoryItem updated = useCase.execute(new ReleaseReservationCommand(item.getSkuId(), item.getWarehouseId(), reservationId));
        assertEquals(10, updated.getPhysicalQuantity());
        assertEquals(0, updated.getReservedQuantity());
    }

    @Test
    void shouldGetInventoryBySku() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 7, Instant.now(clock));
        GetInventoryBySkuUseCase useCase = new GetInventoryBySkuUseCase(repositoryPort);

        when(repositoryPort.findBySku(item.getSkuId())).thenReturn(List.of(item));
        List<InventoryItem> inventoryItems = useCase.execute(item.getSkuId());

        assertEquals(1, inventoryItems.size());
        assertEquals(item.getSkuId(), inventoryItems.getFirst().getSkuId());
    }

    @Test
    void shouldGetInventoryBySkuAndWarehouse() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 7, Instant.now(clock));
        GetInventoryBySkuAndWarehouseUseCase useCase = new GetInventoryBySkuAndWarehouseUseCase(repositoryPort);

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));
        InventoryItem loaded = useCase.execute(item.getSkuId(), item.getWarehouseId());

        assertEquals(item.getWarehouseId(), loaded.getWarehouseId());
    }

    @Test
    void shouldFailWhenInventoryItemDoesNotExist() {
        AdjustPhysicalStockUseCase useCase = new AdjustPhysicalStockUseCase(repositoryPort, clock);
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        when(repositoryPort.findBySkuAndWarehouse(skuId, warehouseId)).thenReturn(Optional.empty());

        assertThrows(InventoryItemNotFoundException.class,
                () -> useCase.execute(new AdjustPhysicalStockCommand(skuId, warehouseId, 2)));
    }

    @Test
    void shouldFailWhenCreatingDuplicatedInventoryItem() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        InventoryItem existing = InventoryItem.create(skuId, warehouseId, 4, Instant.now(clock));
        CreateInventoryItemUseCase useCase = new CreateInventoryItemUseCase(repositoryPort, clock);
        when(repositoryPort.findBySkuAndWarehouse(skuId, warehouseId)).thenReturn(Optional.of(existing));

        assertThrows(InventoryConflictException.class,
                () -> useCase.execute(new CreateInventoryItemCommand(skuId, warehouseId, 5)));
    }

    @Test
    void shouldFailWhenReservationDoesNotExist() {
        InventoryItem item = InventoryItem.create(UUID.randomUUID(), UUID.randomUUID(), 10, Instant.now(clock));
        UUID unknownReservationId = UUID.randomUUID();
        CommitReservationUseCase useCase = new CommitReservationUseCase(repositoryPort, clock);

        when(repositoryPort.findBySkuAndWarehouse(item.getSkuId(), item.getWarehouseId())).thenReturn(Optional.of(item));

        assertThrows(ReservationNotFoundException.class,
                () -> useCase.execute(new CommitReservationCommand(
                        item.getSkuId(),
                        item.getWarehouseId(),
                        unknownReservationId
                )));
    }
}
