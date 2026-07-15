package com.mercadoaurora.inventory.unit;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationEvidenceRepositoryPort;
import com.mercadoaurora.inventory.application.port.out.OrderConfirmationProcessingRepositoryPort;
import com.mercadoaurora.inventory.application.usecase.RecoverOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.RegisterOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.GetOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderConfirmationProcessingUseCasesTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-15T12:00:00Z"), ZoneOffset.UTC);
    @Mock private OrderConfirmationProcessingRepositoryPort processingRepository;
    @Mock private OrderConfirmationEvidenceRepositoryPort evidenceRepository;

    @Test
    void shouldDurablyRegisterPendingBeforeItCanBeAcknowledged() {
        RecognizeOrderConfirmationCommand command = command();
        when(processingRepository.savePendingIfAbsent(any())).thenReturn(true);

        assertTrue(new RegisterOrderConfirmationProcessingUseCase(processingRepository, clock).execute(command));

        ArgumentCaptor<OrderConfirmationProcessing> pending = ArgumentCaptor.forClass(OrderConfirmationProcessing.class);
        verify(processingRepository).savePendingIfAbsent(pending.capture());
        assertEquals(OrderConfirmationProcessing.Status.PENDING, pending.getValue().status());
        assertEquals(command.eventId(), pending.getValue().eventId());
    }

    @Test
    void shouldKeepExistingPendingIdempotentlyOnRedelivery() {
        when(processingRepository.savePendingIfAbsent(any())).thenReturn(false);

        assertFalse(new RegisterOrderConfirmationProcessingUseCase(processingRepository, clock).execute(command()));
        verify(processingRepository).savePendingIfAbsent(any());
    }

    @Test
    void shouldPersistEvidenceAndCompletePendingInOneLocalFlow() {
        OrderConfirmationProcessing pending = pending();
        when(processingRepository.findByEventId(pending.eventId())).thenReturn(Optional.of(pending));
        when(evidenceRepository.saveIfAbsent(any())).thenReturn(true);

        new RecoverOrderConfirmationProcessingUseCase(processingRepository, evidenceRepository, clock).execute(pending.eventId());

        verify(processingRepository).markAttempted(pending.eventId(), Instant.now(clock));
        verify(evidenceRepository).saveIfAbsent(any());
        verify(processingRepository).markCompleted(pending.eventId(), Instant.now(clock));
    }

    @Test
    void shouldNotRepeatCompletedProcessingDuringConcurrentOrLaterRecovery() {
        OrderConfirmationProcessing completed = new OrderConfirmationProcessing(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.parse("2026-07-15T11:00:00Z"), "topic", 0, 1,
                OrderConfirmationProcessing.Status.COMPLETED, 1, Instant.now(clock), Instant.now(clock), Instant.now(clock));
        when(processingRepository.findByEventId(completed.eventId())).thenReturn(Optional.of(completed));

        new RecoverOrderConfirmationProcessingUseCase(processingRepository, evidenceRepository, clock).execute(completed.eventId());

        verifyNoInteractions(evidenceRepository);
        verify(processingRepository, never()).markCompleted(any(), any());
    }

    @Test
    void shouldLeavePendingOpenWhenRecognitionTemporarilyFails() {
        OrderConfirmationProcessing pending = pending();
        when(processingRepository.findByEventId(pending.eventId())).thenReturn(Optional.of(pending));
        when(evidenceRepository.saveIfAbsent(any())).thenThrow(new IllegalStateException("database unavailable"));

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> new RecoverOrderConfirmationProcessingUseCase(processingRepository, evidenceRepository, clock).execute(pending.eventId()));

        verify(processingRepository, never()).markCompleted(any(), any());
    }

    @Test
    void shouldExposePendingProcessingForSupportedConsultation() {
        OrderConfirmationProcessing pending = pending();
        when(processingRepository.findByOrderId(pending.orderId())).thenReturn(List.of(pending));

        List<OrderConfirmationProcessing> result = new GetOrderConfirmationProcessingUseCase(processingRepository).execute(pending.orderId());

        assertEquals(OrderConfirmationProcessing.Status.PENDING, result.getFirst().status());
        verify(processingRepository).findByOrderId(pending.orderId());
    }

    private RecognizeOrderConfirmationCommand command() {
        return new RecognizeOrderConfirmationCommand(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                Instant.parse("2026-07-15T11:00:00Z"), "mercadoaurora.order.order-confirmed.v1", 0, 4);
    }

    private OrderConfirmationProcessing pending() {
        RecognizeOrderConfirmationCommand command = command();
        return new OrderConfirmationProcessing(command.eventId(), command.correlationId(), command.orderId(), command.occurredAt(),
                command.topic(), command.partition(), command.offset(), OrderConfirmationProcessing.Status.PENDING, 0,
                Instant.now(clock), Instant.now(clock), null);
    }
}
