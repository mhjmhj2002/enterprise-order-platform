package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.command.OrderActionCommand;
import com.mercadoaurora.order.application.exception.OrderConflictException;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.exception.PaymentProcessingException;
import com.mercadoaurora.order.application.port.out.InventoryReservationPort;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.application.port.out.PaymentGatewayPort;
import com.mercadoaurora.order.domain.DomainConflictException;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
@Service
public class StartPaymentUseCase {
    private final OrderRepositoryPort repositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;
    private final InventoryReservationPort inventoryReservationPort;
    private final Clock clock;
    public StartPaymentUseCase(
            OrderRepositoryPort repositoryPort,
            PaymentGatewayPort paymentGatewayPort,
            InventoryReservationPort inventoryReservationPort,
            Clock clock
    ) {
        this.repositoryPort = repositoryPort;
        this.paymentGatewayPort = paymentGatewayPort;
        this.inventoryReservationPort = inventoryReservationPort;
        this.clock = clock;
    }
    @Transactional(noRollbackFor = PaymentProcessingException.class)
    public Order execute(OrderActionCommand command) {
        Order order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));
        Instant now = Instant.now(clock);
        try {
            order.startPayment(now);
            paymentGatewayPort.startPayment(order);
        } catch (PaymentProcessingException exception) {
            releaseReservationsBestEffort(order);
            order.failPayment(now);
            order.cancel(now);
            repositoryPort.save(order);
            throw exception;
        } catch (DomainConflictException exception) {
            throw new OrderConflictException(exception.getMessage());
        }
        return repositoryPort.save(order);
    }

    private void releaseReservationsBestEffort(Order order) {
        try {
            inventoryReservationPort.releaseReservations(order);
        } catch (RuntimeException ignored) {
            // Payment remains the primary failure; cancellation must still be persisted.
        }
    }
}
