package com.mercadoaurora.inventory.api.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmationEvidenceResponse(
        UUID eventId,
        UUID correlationId,
        UUID orderId,
        Instant occurredAt,
        Instant recognizedAt,
        String topic,
        int partition,
        long offset
) {
}
