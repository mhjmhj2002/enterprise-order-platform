package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingLifecycleRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class RecordOrderConfirmationTemporaryFailureUseCase {
    private static final String SAFE_FAILURE_CATEGORY = "TEMPORARY_PROCESSING_FAILURE";
    private final OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository;
    private final Clock clock;

    public RecordOrderConfirmationTemporaryFailureUseCase(OrderConfirmationProcessingLifecycleRepositoryPort lifecycleRepository, Clock clock) {
        this.lifecycleRepository = lifecycleRepository;
        this.clock = clock;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(UUID eventId) {
        lifecycleRepository.saveIfAbsent(new OrderConfirmationProcessingLifecycle(eventId,
                OrderConfirmationProcessingLifecycle.Milestone.REGISTERED, Instant.now(clock), null));
        lifecycleRepository.saveIfAbsent(new OrderConfirmationProcessingLifecycle(eventId,
                OrderConfirmationProcessingLifecycle.Milestone.TEMPORARY_FAILURE, Instant.now(clock), SAFE_FAILURE_CATEGORY));
    }
}
