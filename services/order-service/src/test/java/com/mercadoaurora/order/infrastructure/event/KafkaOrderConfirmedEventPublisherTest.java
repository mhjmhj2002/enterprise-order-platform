package com.mercadoaurora.order.infrastructure.event;

import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaOrderConfirmedEventPublisherTest {
    @Mock
    private KafkaTemplate<String, OrderConfirmedEventEnvelope> kafkaTemplate;
    @Mock
    private SendResult<String, OrderConfirmedEventEnvelope> sendResult;

    @Test
    void shouldPublishApprovedEnvelopeUsingOrderIdAsKey() {
        Order order = confirmedOrder();
        when(sendResult.getRecordMetadata()).thenReturn(new RecordMetadata(
                new TopicPartition(KafkaOrderConfirmedEventPublisher.TOPIC, 0), 4, 0, 0, 0, 0
        ));
        when(kafkaTemplate.send(eq(KafkaOrderConfirmedEventPublisher.TOPIC), eq(order.getId().toString()), any()))
                .thenReturn(CompletableFuture.completedFuture(sendResult));

        new KafkaOrderConfirmedEventPublisher(kafkaTemplate).publish(order);

        ArgumentCaptor<OrderConfirmedEventEnvelope> eventCaptor =
                ArgumentCaptor.forClass(OrderConfirmedEventEnvelope.class);
        verify(kafkaTemplate).send(
                eq(KafkaOrderConfirmedEventPublisher.TOPIC),
                eq(order.getId().toString()),
                eventCaptor.capture()
        );
        OrderConfirmedEventEnvelope event = eventCaptor.getValue();
        assertNotNull(event.eventId());
        assertNotNull(event.correlationId());
        assertEquals("OrderConfirmed", event.eventType());
        assertEquals(1, event.eventVersion());
        assertEquals(order.getConfirmedAt(), event.occurredAt());
        assertEquals(order.getId(), event.data().orderId());
    }

    @Test
    void shouldSerializeEnvelopeAsJson() throws Exception {
        OrderConfirmedEventEnvelope event = new OrderConfirmedEventEnvelope(
                UUID.randomUUID(), "OrderConfirmed", 1, Instant.parse("2026-07-14T12:00:00Z"),
                UUID.randomUUID(), new OrderConfirmedEventEnvelope.Data(UUID.randomUUID())
        );

        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        byte[] serialized = new JsonSerializer<OrderConfirmedEventEnvelope>(objectMapper).serialize(
                KafkaOrderConfirmedEventPublisher.TOPIC, event
        );
        JsonNode payload = new ObjectMapper().readTree(serialized);

        assertTrue(payload.hasNonNull("eventId"));
        assertEquals("OrderConfirmed", payload.get("eventType").asText());
        assertEquals(1, payload.get("eventVersion").asInt());
        assertEquals("2026-07-14T12:00:00Z", payload.get("occurredAt").asText());
        assertTrue(payload.hasNonNull("correlationId"));
        assertTrue(payload.path("data").hasNonNull("orderId"));
    }

    private Order confirmedOrder() {
        Instant now = Instant.parse("2026-07-14T12:00:00Z");
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(
                OrderItem.create(
                        UUID.randomUUID(), "Produto", "SKU", Map.of(), 1,
                        new BigDecimal("20.00"), BigDecimal.ZERO, new BigDecimal("20.00")
                )
        ), now);
        order.reserveStock(List.of(UUID.randomUUID()), now.plusSeconds(1));
        order.startPayment(now.plusSeconds(2));
        order.markPaid(now.plusSeconds(3));
        order.confirm(now.plusSeconds(4));
        return order;
    }
}
