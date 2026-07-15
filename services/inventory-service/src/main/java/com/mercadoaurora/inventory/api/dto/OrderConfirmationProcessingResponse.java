package com.mercadoaurora.inventory.api.dto;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmationProcessingResponse(
        UUID eventId,
        UUID correlationId,
        UUID orderId,
        Instant occurredAt,
        String topic,
        int partition,
        long offset,
        OrderConfirmationProcessing.Status status,
        int attemptCount,
        Instant createdAt,
        Instant updatedAt,
        Instant completedAt
) {
}
