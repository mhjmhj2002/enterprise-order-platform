package com.mercadoaurora.inventory.infrastructure.event;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.usecase.RecoverOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.RegisterOrderConfirmationProcessingUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaOrderConfirmedEventConsumerTest {
    @Mock
    private RegisterOrderConfirmationProcessingUseCase registerProcessing;
    @Mock
    private RecoverOrderConfirmationProcessingUseCase recoverProcessing;

    @Test
    void shouldRecognizeApprovedEnvelopeWithKafkaTraceability() {
        UUID eventId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        when(registerProcessing.execute(any())).thenReturn(true);
        ConsumerRecord<String, OrderConfirmedEventEnvelope> record = new ConsumerRecord<>(
                KafkaOrderConfirmedEventConsumer.TOPIC, 0, 11L, orderId.toString(),
                new OrderConfirmedEventEnvelope(eventId, "OrderConfirmed", 1, Instant.parse("2026-07-14T12:00:00Z"),
                        correlationId, new OrderConfirmedEventEnvelope.Data(orderId)));

        new KafkaOrderConfirmedEventConsumer(registerProcessing, recoverProcessing).consume(record);

        ArgumentCaptor<RecognizeOrderConfirmationCommand> command = ArgumentCaptor.forClass(RecognizeOrderConfirmationCommand.class);
        verify(registerProcessing).execute(command.capture());
        verify(recoverProcessing).execute(eventId);
        assertEquals(eventId, command.getValue().eventId());
        assertEquals(orderId, command.getValue().orderId());
        assertEquals(11L, command.getValue().offset());
    }

    @Test
    void shouldRejectInvalidEnvelopeBeforePersistingEvidence() {
        ConsumerRecord<String, OrderConfirmedEventEnvelope> record = new ConsumerRecord<>(
                KafkaOrderConfirmedEventConsumer.TOPIC, 0, 0L, "key",
                new OrderConfirmedEventEnvelope(UUID.randomUUID(), "OrderCreated", 1, Instant.now(), UUID.randomUUID(),
                        new OrderConfirmedEventEnvelope.Data(UUID.randomUUID())));

        assertThrows(IllegalArgumentException.class, () -> new KafkaOrderConfirmedEventConsumer(registerProcessing, recoverProcessing).consume(record));
    }

    @Test
    void shouldPropagateFailureBeforeDurableRegistrationSoKafkaCanRedeliver() {
        UUID eventId = UUID.randomUUID();
        ConsumerRecord<String, OrderConfirmedEventEnvelope> record = new ConsumerRecord<>(
                KafkaOrderConfirmedEventConsumer.TOPIC, 0, 12L, "key",
                new OrderConfirmedEventEnvelope(eventId, "OrderConfirmed", 1, Instant.now(), UUID.randomUUID(),
                        new OrderConfirmedEventEnvelope.Data(UUID.randomUUID())));
        when(registerProcessing.execute(any())).thenThrow(new IllegalStateException("database unavailable"));

        assertThrows(IllegalStateException.class, () -> new KafkaOrderConfirmedEventConsumer(registerProcessing, recoverProcessing).consume(record));

        verifyNoInteractions(recoverProcessing);
    }
}
