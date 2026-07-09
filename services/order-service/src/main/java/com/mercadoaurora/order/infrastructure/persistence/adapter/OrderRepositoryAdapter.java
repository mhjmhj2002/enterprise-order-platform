package com.mercadoaurora.order.infrastructure.persistence.adapter;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.infrastructure.persistence.entity.OrderEntity;
import com.mercadoaurora.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import com.mercadoaurora.order.infrastructure.persistence.repository.SpringDataOrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {
    private final SpringDataOrderRepository repository;
    public OrderRepositoryAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }
    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity entity = repository.findById(order.getId()).orElseGet(OrderEntity::new);
        OrderEntity saved = repository.save(OrderPersistenceMapper.toEntity(order, entity));
        return OrderPersistenceMapper.toDomain(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID orderId) {
        return repository.findById(orderId).map(OrderPersistenceMapper::toDomain);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(UUID customerId) {
        return repository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(OrderPersistenceMapper::toDomain)
                .toList();
    }
}
