package com.mercadoaurora.inventory.infrastructure.persistence.repository;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationProcessingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringDataOrderConfirmationProcessingRepository extends JpaRepository<OrderConfirmationProcessingEntity, UUID> {
    @Modifying
    @Query(value = """
            INSERT INTO order_confirmation_processing
              (event_id, correlation_id, order_id, occurred_at, topic, partition_number, offset_value, status, attempt_count, created_at, updated_at)
            VALUES
              (:eventId, :correlationId, :orderId, :occurredAt, :topic, :partition, :offset, 'PENDING', 0, :createdAt, :updatedAt)
            ON CONFLICT (event_id) DO NOTHING
            """, nativeQuery = true)
    int insertPendingIfAbsent(
            @Param("eventId") UUID eventId,
            @Param("correlationId") UUID correlationId,
            @Param("orderId") UUID orderId,
            @Param("occurredAt") Instant occurredAt,
            @Param("topic") String topic,
            @Param("partition") int partition,
            @Param("offset") long offset,
            @Param("createdAt") Instant createdAt,
            @Param("updatedAt") Instant updatedAt
    );

    @Query("select p from OrderConfirmationProcessingEntity p where p.status = :status order by p.createdAt")
    List<OrderConfirmationProcessingEntity> findByStatus(@Param("status") OrderConfirmationProcessing.Status status, Pageable pageable);

    List<OrderConfirmationProcessingEntity> findByOrderIdOrderByCreatedAtAsc(UUID orderId);

    @Modifying
    @Query(value = """
            UPDATE order_confirmation_processing
            SET attempt_count = attempt_count + 1, updated_at = :now
            WHERE event_id = :eventId AND status = 'PENDING'
            """, nativeQuery = true)
    void markAttempted(@Param("eventId") UUID eventId, @Param("now") Instant now);

    @Modifying
    @Query(value = """
            UPDATE order_confirmation_processing
            SET status = 'COMPLETED', completed_at = :now, updated_at = :now
            WHERE event_id = :eventId AND status = 'PENDING'
            """, nativeQuery = true)
    void markCompleted(@Param("eventId") UUID eventId, @Param("now") Instant now);
}
