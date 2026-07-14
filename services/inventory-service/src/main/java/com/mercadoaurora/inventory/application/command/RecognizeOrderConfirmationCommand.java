package com.mercadoaurora.inventory.application.command;

import java.time.Instant;
import java.util.UUID;

public record RecognizeOrderConfirmationCommand(
        UUID eventId,
        UUID correlationId,
        UUID orderId,
        Instant occurredAt,
        String topic,
        int partition,
        long offset
) {
}
