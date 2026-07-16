package com.mercadoaurora.inventory.domain;

import java.time.Instant;
import java.util.UUID;

public record OrderConfirmationProcessingLifecycle(UUID eventId, Milestone milestone, Instant occurredAt, String failureCategory) {
    public enum Milestone { REGISTERED, TEMPORARY_FAILURE, COMPLETED }

    public OrderConfirmationProcessingLifecycle {
        if (eventId == null || milestone == null || occurredAt == null
                || (milestone == Milestone.TEMPORARY_FAILURE && (failureCategory == null || failureCategory.isBlank()))
                || (milestone != Milestone.TEMPORARY_FAILURE && failureCategory != null)) {
            throw new DomainValidationException("Order confirmation lifecycle must contain valid operational data");
        }
    }
}
