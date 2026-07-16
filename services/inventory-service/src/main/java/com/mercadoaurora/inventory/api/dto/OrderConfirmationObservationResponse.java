package com.mercadoaurora.inventory.api.dto;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderConfirmationObservationResponse(
        UUID orderId, UUID eventId, UUID correlationId, String eventType, int eventVersion, Instant occurredAt,
        String topic, int partition, long offset, OrderConfirmationProcessing.Status status, int attemptCount,
        Instant registeredAt, Instant updatedAt, Instant completedAt, Instant recognizedAt,
        boolean uniqueFunctionalResult, List<OrderConfirmationLifecycleResponse> lifecycle) { }
