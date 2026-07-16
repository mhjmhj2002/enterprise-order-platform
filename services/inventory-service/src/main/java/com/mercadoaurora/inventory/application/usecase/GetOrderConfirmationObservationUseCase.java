package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingLifecycleRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetOrderConfirmationObservationUseCase {
    public record Observation(OrderConfirmationProcessing processing, OrderConfirmationEvidence evidence,
                              List<OrderConfirmationProcessingLifecycle> lifecycle) { }

    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final OrderConfirmationEvidenceRepositoryPort evidenceRepository;
    private final OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository;

    public GetOrderConfirmationObservationUseCase(OrderConfirmationProcessingRepositoryPort processingRepository,
                                                   OrderConfirmationEvidenceRepositoryPort evidenceRepository,
                                                   OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository) {
        this.processingRepository = processingRepository;
        this.evidenceRepository = evidenceRepository;
        this.lifecycleRepository = lifecycleRepository;
    }

    @Transactional(readOnly = true)
    public List<Observation> execute(UUID orderId) {
        List<OrderConfirmationEvidence> evidence = evidenceRepository.findByOrderId(orderId);
        return processingRepository.findByOrderId(orderId).stream().map(processing -> new Observation(processing,
                evidence.stream().filter(item -> item.eventId().equals(processing.eventId())).findFirst().orElse(null),
                lifecycleRepository.findByEventId(processing.eventId()))).toList();
    }
}
