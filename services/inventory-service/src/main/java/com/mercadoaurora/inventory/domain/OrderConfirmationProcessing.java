package com.mercadoaurora.inventory.domain;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmationProcessing(
        UUID eventId,
        UUID correlationId,
        UUID orderId,
        Instant occurredAt,
        String topic,
        int partition,
        long offset,
        Status status,
        int attemptCount,
        Instant createdAt,
        Instant updatedAt,
        Instant completedAt
) {
    public enum Status { PENDING, COMPLETED }

    public OrderConfirmationProcessing {
        if (eventId == null || correlationId == null || orderId == null || occurredAt == null || topic == null || topic.isBlank()
                || partition < 0 || offset < 0 || status == null || attemptCount < 0 || createdAt == null || updatedAt == null) {
            throw new DomainValidationException("Order confirmation processing must contain valid traceability data");
        }
    }
}
