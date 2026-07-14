package com.mercadoaurora.inventory.infrastructure.persistence.repository;

import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringDataOrderConfirmationEvidenceRepository extends JpaRepository<OrderConfirmationEvidenceEntity, UUID> {
    @Modifying
    @Query(value = """
            INSERT INTO order_confirmation_evidence (event_id, correlation_id, order_id, occurred_at, recognized_at, topic, partition_number, offset_value)
            VALUES (:eventId, :correlationId, :orderId, :occurredAt, :recognizedAt, :topic, :partition, :offset)
            ON CONFLICT (event_id) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(
            @Param("eventId") UUID eventId,
            @Param("correlationId") UUID correlationId,
            @Param("orderId") UUID orderId,
            @Param("occurredAt") Instant occurredAt,
            @Param("recognizedAt") Instant recognizedAt,
            @Param("topic") String topic,
            @Param("partition") int partition,
            @Param("offset") long offset
    );

    List<OrderConfirmationEvidenceEntity> findByOrderIdOrderByRecognizedAtAsc(UUID orderId);
}
