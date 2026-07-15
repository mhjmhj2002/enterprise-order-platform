package com.mercadoaurora.inventory.application.usecase;

import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetOrderConfirmationProcessingUseCase {
    private final OrderConfirmationProcessingRepositoryPort processingRepository;

    public GetOrderConfirmationProcessingUseCase(OrderConfirmationProcessingRepositoryPort processingRepository) {
        this.processingRepository = processingRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderConfirmationProcessing> execute(UUID orderId) {
        return processingRepository.findByOrderId(orderId);
    }
}
