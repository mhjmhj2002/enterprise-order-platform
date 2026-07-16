package com.mercadoaurora.inventory.infrastructure.persistence.repository;

import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationProcessingLifecycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringDataOrderConfirmationProcessingLifecycleRepository extends JpaRepository<OrderConfirmationProcessingLifecycleEntity, Long> {
    @Modifying
    @Query(value = """
            INSERT INTO order_confirmation_processing_lifecycle (event_id, milestone, occurred_at, failure_category)
            VALUES (:eventId, :milestone, :occurredAt, :failureCategory)
            ON CONFLICT (event_id, milestone) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(@Param("eventId") UUID eventId, @Param("milestone") String milestone,
                       @Param("occurredAt") Instant occurredAt, @Param("failureCategory") String failureCategory);

    List<OrderConfirmationProcessingLifecycleEntity> findByEventIdOrderByOccurredAtAscIdAsc(UUID eventId);
}
