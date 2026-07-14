package com.mercadoaurora.order.infrastructure.event;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmedEventEnvelope(
        UUID eventId,
        String eventType,
        int eventVersion,
        Instant occurredAt,
        UUID correlationId,
        Data data
) {
    public record Data(UUID orderId) {
    }
}
