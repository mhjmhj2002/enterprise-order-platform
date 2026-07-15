package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class RecoverOrderConfirmationProcessingUseCase {
    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final OrderConfirmationEvidenceRepositoryPort evidenceRepository;
    private final Clock clock;

    public RecoverOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository,
                                                      OrderConfirmationEvidenceRepositoryPort evidenceRepository, Clock clock) {
        this.processingRepository = processingRepository;
        this.evidenceRepository = evidenceRepository;
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
    }
}
