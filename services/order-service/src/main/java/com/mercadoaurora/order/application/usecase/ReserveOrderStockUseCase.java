package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.command.ReserveOrderStockCommand;
import com.mercadoaurora.order.application.exception.OrderConflictException;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.port.out.InventoryReservationPort;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.DomainConflictException;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
@Service
public class ReserveOrderStockUseCase {
    private final OrderRepositoryPort repositoryPort;
    private final InventoryReservationPort inventoryReservationPort;
    private final Clock clock;
    public ReserveOrderStockUseCase(
            OrderRepositoryPort repositoryPort,
            InventoryReservationPort inventoryReservationPort,
            Clock clock
    ) {
        this.repositoryPort = repositoryPort;
        this.inventoryReservationPort = inventoryReservationPort;
        this.clock = clock;
    }
    @Transactional
    public Order execute(ReserveOrderStockCommand command) {
        Order order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));
        Instant now = Instant.now(clock);
        try {
            inventoryReservationPort.reserveStock(order, command.reservationRefs());
            order.reserveStock(command.reservationRefs(), now);
        } catch (DomainConflictException exception) {
            throw new OrderConflictException(exception.getMessage());
        }
        return repositoryPort.save(order);
    }
}
