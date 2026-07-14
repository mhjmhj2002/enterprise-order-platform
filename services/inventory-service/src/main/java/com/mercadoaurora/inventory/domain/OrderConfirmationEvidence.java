package com.mercadoaurora.inventory.domain;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmationEvidence(
        UUID eventId,
        UUID correlationId,
        UUID orderId,
        Instant occurredAt,
        Instant recognizedAt,
        String topic,
        int partition,
        long offset
) {
    public OrderConfirmationEvidence {
        if (eventId == null || correlationId == null || orderId == null || occurredAt == null || recognizedAt == null
                || topic == null || topic.isBlank() || partition < 0 || offset < 0) {
            throw new DomainValidationException("Order confirmation evidence must contain valid event traceability data");
        }
    }
}
