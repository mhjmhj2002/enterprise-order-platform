package com.mercadoaurora.order.infrastructure.event;

import com.mercadoaurora.order.application.port.out.OrderConfirmedEventPublisherPort;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.infrastructure.web.CorrelationIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@Profile("kafka")
public class KafkaOrderConfirmedEventPublisher implements OrderConfirmedEventPublisherPort {
    static final String TOPIC = "mercadoaurora.order.order-confirmed.v1";
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOrderConfirmedEventPublisher.class);

    private final KafkaTemplate<String, OrderConfirmedEventEnvelope> kafkaTemplate;

    public KafkaOrderConfirmedEventPublisher(KafkaTemplate<String, OrderConfirmedEventEnvelope> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Order order) {
        UUID correlationId = correlationId();
        OrderConfirmedEventEnvelope event = new OrderConfirmedEventEnvelope(
                UUID.randomUUID(),
                "OrderConfirmed",
                1,
                order.getConfirmedAt(),
                correlationId,
                new OrderConfirmedEventEnvelope.Data(order.getId())
        );

        try {
            SendResult<String, OrderConfirmedEventEnvelope> result = kafkaTemplate
                    .send(TOPIC, order.getId().toString(), event)
                    .get();
            LOGGER.info(
                    "OrderConfirmed published eventId={} correlationId={} orderId={} topic={} partition={} offset={}",
                    event.eventId(), event.correlationId(), order.getId(), TOPIC,
                    result.getRecordMetadata().partition(), result.getRecordMetadata().offset()
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            LOGGER.error(
                    "OrderConfirmed publication interrupted eventId={} correlationId={} orderId={} topic={}",
                    event.eventId(), event.correlationId(), order.getId(), TOPIC, exception
            );
            throw new OrderEventPublicationException("Interrupted while publishing OrderConfirmed", exception);
        } catch (ExecutionException exception) {
            LOGGER.error(
                    "OrderConfirmed publication failed eventId={} correlationId={} orderId={} topic={}",
                    event.eventId(), event.correlationId(), order.getId(), TOPIC, exception.getCause()
            );
            throw new OrderEventPublicationException("Failed to publish OrderConfirmed", exception.getCause());
        }
    }

    private UUID correlationId() {
        String correlationId = CorrelationIdContext.get();
        try {
            return correlationId == null ? UUID.randomUUID() : UUID.fromString(correlationId);
        } catch (IllegalArgumentException ignored) {
            return UUID.randomUUID();
        }
    }
}
