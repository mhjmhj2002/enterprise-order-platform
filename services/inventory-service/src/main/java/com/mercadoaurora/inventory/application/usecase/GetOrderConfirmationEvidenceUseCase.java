package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetOrderConfirmationEvidenceUseCase {
    private final OrderConfirmationEvidenceRepositoryPort evidenceRepository;

    public GetOrderConfirmationEvidenceUseCase(OrderConfirmationEvidenceRepositoryPort evidenceRepository) {
        this.evidenceRepository = evidenceRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderConfirmationEvidence> execute(UUID orderId) {
        return evidenceRepository.findByOrderId(orderId);
    }
}
