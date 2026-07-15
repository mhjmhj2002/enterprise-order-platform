package com.mercadoaurora.inventory.application.port.out;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderConfirmationProcessingRepositoryPort {
    boolean savePendingIfAbsent(OrderConfirmationProcessing processing);

    Optional<OrderConfirmationProcessing> findByEventId(UUID eventId);

    List<OrderConfirmationProcessing> findPending(int limit);

    List<OrderConfirmationProcessing> findByOrderId(UUID orderId);

    void markAttempted(UUID eventId, Instant attemptedAt);

    void markCompleted(UUID eventId, Instant completedAt);
}
