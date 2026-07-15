package com.mercadoaurora.inventory.infrastructure.persistence.adapter;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationProcessingEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataOrderConfirmationProcessingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderConfirmationProcessingRepositoryAdapter implements OrderConfirmationProcessingRepositoryPort {
    private final SpringDataOrderConfirmationProcessingRepository repository;

    public OrderConfirmationProcessingRepositoryAdapter(SpringDataOrderConfirmationProcessingRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savePendingIfAbsent(OrderConfirmationProcessing processing) {
        return repository.insertPendingIfAbsent(
                processing.eventId(), processing.correlationId(), processing.orderId(), processing.occurredAt(), processing.topic(),
                processing.partition(), processing.offset(), processing.createdAt(), processing.updatedAt()) == 1;
    }

    @Override public Optional<OrderConfirmationProcessing> findByEventId(UUID eventId) { return repository.findById(eventId).map(this::toDomain); }
    @Override public List<OrderConfirmationProcessing> findPending(int limit) {
        return repository.findByStatus(OrderConfirmationProcessing.Status.PENDING, PageRequest.of(0, limit)).stream().map(this::toDomain).toList();
    }
    @Override public List<OrderConfirmationProcessing> findByOrderId(UUID orderId) {
        return repository.findByOrderIdOrderByCreatedAtAsc(orderId).stream().map(this::toDomain).toList();
    }
    @Override public void markAttempted(UUID eventId, Instant attemptedAt) { repository.markAttempted(eventId, attemptedAt); }
    @Override public void markCompleted(UUID eventId, Instant completedAt) { repository.markCompleted(eventId, completedAt); }

    private OrderConfirmationProcessing toDomain(OrderConfirmationProcessingEntity e) {
        return new OrderConfirmationProcessing(e.getEventId(), e.getCorrelationId(), e.getOrderId(), e.getOccurredAt(), e.getTopic(),
                e.getPartition(), e.getOffset(), e.getStatus(), e.getAttemptCount(), e.getCreatedAt(), e.getUpdatedAt(), e.getCompletedAt());
    }
}
