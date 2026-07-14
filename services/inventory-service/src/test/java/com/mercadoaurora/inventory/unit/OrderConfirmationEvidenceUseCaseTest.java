package com.mercadoaurora.inventory.unit;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.application.usecase.RecognizeOrderConfirmationUseCase;
import com.mercadoaurora.inventory.domain.OrderConfirmationEvidence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderConfirmationEvidenceUseCaseTest {
    @Mock
    private OrderConfirmationEvidenceRepositoryPort repository;

    @Test
    void shouldPersistRecognitionEvidenceWithoutChangingInventory() {
        Clock clock = Clock.fixed(Instant.parse("2026-07-14T12:00:00Z"), ZoneOffset.UTC);
        RecognizeOrderConfirmationUseCase useCase = new RecognizeOrderConfirmationUseCase(repository, clock);
        RecognizeOrderConfirmationCommand command = new RecognizeOrderConfirmationCommand(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.parse("2026-07-14T11:59:00Z"),
                "mercadoaurora.order.order-confirmed.v1", 0, 7);
        when(repository.saveIfAbsent(any())).thenReturn(true);

        assertTrue(useCase.execute(command));

        ArgumentCaptor<OrderConfirmationEvidence> evidence = ArgumentCaptor.forClass(OrderConfirmationEvidence.class);
        verify(repository).saveIfAbsent(evidence.capture());
        assertEquals(command.eventId(), evidence.getValue().eventId());
        assertEquals(command.orderId(), evidence.getValue().orderId());
        assertEquals(Instant.now(clock), evidence.getValue().recognizedAt());
    }
}
