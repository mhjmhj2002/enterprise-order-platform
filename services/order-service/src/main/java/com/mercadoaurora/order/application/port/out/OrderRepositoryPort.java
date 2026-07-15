package com.mercadoaurora.order.application.port.out;
import com.mercadoaurora.order.domain.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID orderId);
    List<Order> findByCustomerId(UUID customerId);
}
