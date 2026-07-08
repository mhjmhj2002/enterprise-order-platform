package com.mercadoaurora.inventory.domain.event;

import java.time.Instant;

public interface DomainEvent {

    String eventType();

    Instant occurredAt();
}
