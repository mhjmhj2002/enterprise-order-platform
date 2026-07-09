package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
public class ListOrdersByCustomerUseCase {
    private final OrderRepositoryPort repositoryPort;
    public ListOrdersByCustomerUseCase(OrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }
    @Transactional(readOnly = true)
    public List<Order> execute(UUID customerId) {
        return repositoryPort.findByCustomerId(customerId);
    }
}
