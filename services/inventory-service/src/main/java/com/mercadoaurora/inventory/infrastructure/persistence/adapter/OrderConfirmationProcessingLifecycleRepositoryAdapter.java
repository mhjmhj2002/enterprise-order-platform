package com.mercadoaurora.inventory.infrastructure.persistence.adapter;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingLifecycleRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationProcessingLifecycleEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataOrderConfirmationProcessingLifecycleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderConfirmationProcessingLifecycleRepositoryAdapter implements OrderConfirmationProcessingLifecycleRepositoryPort {
    private final SpringDataOrderConfirmationProcessingLifecycleRepository repository;

    public OrderConfirmationProcessingLifecycleRepositoryAdapter(SpringDataOrderConfirmationProcessingLifecycleRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean saveIfAbsent(OrderConfirmationProcessingLifecycle lifecycle) {
        return repository.insertIfAbsent(lifecycle.eventId(), lifecycle.milestone().name(), lifecycle.occurredAt(), lifecycle.failureCategory()) == 1;
    }

    @Override
    public List<OrderConfirmationProcessingLifecycle> findByEventId(UUID eventId) {
        return repository.findByEventIdOrderByOccurredAtAscIdAsc(eventId).stream().map(this::toDomain).toList();
    }

    private OrderConfirmationProcessingLifecycle toDomain(OrderConfirmationProcessingLifecycleEntity entity) {
        return new OrderConfirmationProcessingLifecycle(entity.getEventId(), entity.getMilestone(), entity.getOccurredAt(), entity.getFailureCategory());
    }
}
