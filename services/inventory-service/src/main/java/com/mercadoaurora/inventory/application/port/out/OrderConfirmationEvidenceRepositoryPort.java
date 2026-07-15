package com.mercadoaurora.inventory.application.port.out;

import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;

import java.util.List;
import java.util.UUID;

public interface OrderConfirmationEvidenceRepositoryPort {
    boolean saveIfAbsent(OrderConfirmationEvidence evidence);

    List<OrderConfirmationEvidence> findByOrderId(UUID orderId);
}
