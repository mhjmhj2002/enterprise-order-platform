package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.command.OrderActionCommand;
import com.mercadoaurora.order.application.exception.OrderConflictException;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.DomainConflictException;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
@Service
public class StartPaymentUseCase {
    private final OrderRepositoryPort repositoryPort;
    private final Clock clock;
    public StartPaymentUseCase(OrderRepositoryPort repositoryPort, Clock clock) {
        this.repositoryPort = repositoryPort;
        this.clock = clock;
    }
    @Transactional
    public Order execute(OrderActionCommand command) {
        Order order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));
        Instant now = Instant.now(clock);
        try {
            order.startPayment(now);
        } catch (DomainConflictException exception) {
            throw new OrderConflictException(exception.getMessage());
        }
        return repositoryPort.save(order);
    }
}
