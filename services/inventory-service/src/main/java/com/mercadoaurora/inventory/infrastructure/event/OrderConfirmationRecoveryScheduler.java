package com.mercadoaurora.inventory.infrastructure.event;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.application.usecase.RecoverOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class OrderConfirmationRecoveryScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConfirmationRecoveryScheduler.class);
    private final OrderConfirmationProcessingRepositoryPort processingRepository;
    private final RecoverOrderConfirmationProcessingUseCase recoverProcessing;
    private final int batchSize;

    public OrderConfirmationRecoveryScheduler(OrderConfirmationProcessingRepositoryPort processingRepository,
                                              RecoverOrderConfirmationProcessingUseCase recoverProcessing,
                                              @org.springframework.beans.factory.annotation.Value("${app.order-confirmation-recovery.batch-size:25}") int batchSize) {
        this.processingRepository = processingRepository;
        this.recoverProcessing = recoverProcessing;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${app.order-confirmation-recovery.fixed-delay-ms:5000}")
    public void recoverPending() {
        for (OrderConfirmationProcessing processing : processingRepository.findPending(batchSize)) {
            try {
                recoverProcessing.execute(processing.eventId());
                LOGGER.info("OrderConfirmed recovery completed eventId={} correlationId={} orderId={}",
                        processing.eventId(), processing.correlationId(), processing.orderId());
            } catch (RuntimeException exception) {
                LOGGER.warn("OrderConfirmed recovery pending eventId={} correlationId={} orderId={}",
                        processing.eventId(), processing.correlationId(), processing.orderId(), exception);
            }
        }
    }
}
