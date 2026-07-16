package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingLifecycleRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class RegisterOrderConfirmationProcessingUseCase {
    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository;
    private final Clock clock;

    public RegisterOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository,
                                                      OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository, Clock clock) {
        this.processingRepository = processingRepository;
        this.lifecycleRepository = lifecycleRepository;
        this.clock = clock;
    }

    @Transactional
    public boolean execute(RecognizeOrderConfirmationCommand command) {
        Instant now = Instant.now(clock);
        boolean registered = processingRepository.savePendingIfAbsent(new OrderConfirmationProcessing(
                command.eventId(), command.correlationId(), command.orderId(), command.occurredAt(), command.topic(),
                command.partition(), command.offset(), OrderConfirmationProcessing.Status.PENDING, 0, now, now, null));
        if (registered) {
            lifecycleRepository.saveIfAbsent(new OrderConfirmationProcessingLifecycle(command.eventId(),
                    OrderConfirmationProcessingLifecycle.Milestone.REGISTERED, now, null));
        }
        return registered;
    }
}
