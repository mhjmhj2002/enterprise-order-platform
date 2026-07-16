package com.mercadoaurora.inventory.application.port.out;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;

import java.util.List;
import java.util.UUID;

public interface OrderConfirmationProcessingLifecycleRepositoryPort {
    boolean saveIfAbsent(OrderConfirmationProcessingLifecycle lifecycle);

    List<OrderConfirmationProcessingLifecycle> findByEventId(UUID eventId);
}
