package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingLifecycleRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class RecoverOrderConfirmationProcessingUseCase {
    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final OrderConfirmationEvidenceRepositoryPort evidenceRepository;
    private final OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository;
    private final Clock clock;

    public RecoverOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository,
                                                      OrderConfirmationEvidenceRepositoryPort evidenceRepository, Clock clock) {
        this(processingRepository, evidenceRepository, null, clock);
    }

    @Autowired
    public RecoverOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository,
                                                      OrderConfirmationEvidenceRepositoryPort evidenceRepository,
                                                      OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository, Clock clock) {
        this.processingRepository = processingRepository;
        this.evidenceRepository = evidenceRepository;
        this.lifecycleRepository = lifecycleRepository;
        this.clock = clock;
    }

    @Transactional
    public void execute(UUID eventId) {
        OrderConfirmationProcessing processing = processingRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalStateException("Order confirmation processing was not registered"));
        if (processing.status() == OrderConfirmationProcessing.Status.COMPLETED) {
            return;
        }

        Instant now = Instant.now(clock);
        processingRepository.markAttempted(eventId, now);
        evidenceRepository.saveIfAbsent(new OrderConfirmationEvidence(
                processing.eventId(), processing.correlationId(), processing.orderId(), processing.occurredAt(), now,
                processing.topic(), processing.partition(), processing.offset()));
        processingRepository.markCompleted(eventId, now);
        if (lifecycleRepository != null) {
            lifecycleRepository.saveIfAbsent(new OrderConfirmationProcessingLifecycle(eventId,
                    OrderConfirmationProcessingLifecycle.Milestone.REGISTERED, processing.createdAt(), null));
            lifecycleRepository.saveIfAbsent(new OrderConfirmationProcessingLifecycle(eventId,
                    OrderConfirmationProcessingLifecycle.Milestone.COMPLETED, now, null));
        }
    }
}
