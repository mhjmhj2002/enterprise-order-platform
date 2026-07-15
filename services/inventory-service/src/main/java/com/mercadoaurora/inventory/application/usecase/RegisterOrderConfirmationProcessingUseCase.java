package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class RegisterOrderConfirmationProcessingUseCase {
    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final Clock clock;

    public RegisterOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository, Clock clock) {
        this.processingRepository = processingRepository;
        this.clock = clock;
    }

    @Transactional
    public boolean execute(RecognizeOrderConfirmationCommand command) {
        Instant now = Instant.now(clock);
        return processingRepository.savePendingIfAbsent(new OrderConfirmationProcessing(
                command.eventId(), command.correlationId(), command.orderId(), command.occurredAt(), command.topic(),
                command.partition(), command.offset(), OrderConfirmationProcessing.Status.PENDING, 0, now, now, null));
    }
}
