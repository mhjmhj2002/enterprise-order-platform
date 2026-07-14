package com.mercadoaurora.inventory.infrastructure.persistence.adapter;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationEvidenceEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataOrderConfirmationEvidenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderConfirmationEvidenceRepositoryAdapter implements OrderConfirmationEvidenceRepositoryPort {
    private final SpringDataOrderConfirmationEvidenceRepository repository;

    public OrderConfirmationEvidenceRepositoryAdapter(SpringDataOrderConfirmationEvidenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean saveIfAbsent(OrderConfirmationEvidence evidence) {
        return repository.insertIfAbsent(
                evidence.eventId(), evidence.correlationId(), evidence.orderId(), evidence.occurredAt(), evidence.recognizedAt(),
                evidence.topic(), evidence.partition(), evidence.offset()
        ) == 1;
    }

    @Override
    public List<OrderConfirmationEvidence> findByOrderId(UUID orderId) {
        return repository.findByOrderIdOrderByRecognizedAtAsc(orderId).stream().map(this::toDomain).toList();
    }

    private OrderConfirmationEvidence toDomain(OrderConfirmationEvidenceEntity entity) {
        return new OrderConfirmationEvidence(
                entity.getEventId(), entity.getCorrelationId(), entity.getOrderId(), entity.getOccurredAt(), entity.getRecognizedAt(),
                entity.getTopic(), entity.getPartition(), entity.getOffset()
        );
    }
}
