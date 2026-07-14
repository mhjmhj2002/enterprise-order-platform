package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.in.OrderConfirmationRecognizer;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class RecognizeOrderConfirmationUseCase implements OrderConfirmationRecognizer {
    private final OrderConfirmationEvidenceRepositoryPort evidenceRepository;
    private final Clock clock;

    public RecognizeOrderConfirmationUseCase(OrderConfirmationEvidenceRepositoryPort evidenceRepository, Clock clock) {
        this.evidenceRepository = evidenceRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public boolean execute(RecognizeOrderConfirmationCommand command) {
        return evidenceRepository.saveIfAbsent(new OrderConfirmationEvidence(
                command.eventId(), command.correlationId(), command.orderId(), command.occurredAt(), Instant.now(clock),
                command.topic(), command.partition(), command.offset()
        ));
    }
}
