package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Service
public class GetOrderUseCase {
    private final OrderRepositoryPort repositoryPort;
    public GetOrderUseCase(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }
    @Transactional(readOnly = true)
    public Order execute(UUID orderId) {
        return repositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
