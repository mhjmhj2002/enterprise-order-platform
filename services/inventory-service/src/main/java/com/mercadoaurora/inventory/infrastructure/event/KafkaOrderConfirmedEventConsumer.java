package com.mercadoaurora.inventory.infrastructure.event;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.usecase.RecoverOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.RegisterOrderConfirmationProcessingUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class KafkaOrderConfirmedEventConsumer {
    static final String TOPIC = "mercadoaurora.order.order-confirmed.v1";
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOrderConfirmedEventConsumer.class);
    private final RegisterOrderConfirmationProcessingUseCase registerProcessing;
    private final RecoverOrderConfirmationProcessingUseCase recoverProcessing;

    public KafkaOrderConfirmedEventConsumer(RegisterOrderConfirmationProcessingUseCase registerProcessing,
                                             RecoverOrderConfirmationProcessingUseCase recoverProcessing) {
        this.registerProcessing = registerProcessing;
        this.recoverProcessing = recoverProcessing;
    }

    @KafkaListener(topics = TOPIC, containerFactory = "orderConfirmedKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, OrderConfirmedEventEnvelope> record) {
        OrderConfirmedEventEnvelope event = record.value();
        validate(event);
        RecognizeOrderConfirmationCommand command = new RecognizeOrderConfirmationCommand(
                event.eventId(), event.correlationId(), event.data().orderId(), event.occurredAt(),
                record.topic(), record.partition(), record.offset());
        boolean registered = registerProcessing.execute(command);
        recoverProcessing.execute(event.eventId());
        LOGGER.info("OrderConfirmed processing completed eventId={} correlationId={} orderId={} topic={} partition={} offset={} registered={}",
                event.eventId(), event.correlationId(), event.data().orderId(),
                record.topic(), record.partition(), record.offset(), registered);
    }

    private void validate(OrderConfirmedEventEnvelope event) {
        if (event == null || event.eventId() == null || event.correlationId() == null || event.occurredAt() == null
                || event.data() == null || event.data().orderId() == null
                || !"OrderConfirmed".equals(event.eventType()) || event.eventVersion() != 1) {
            throw new IllegalArgumentException("Invalid OrderConfirmed v1 event received");
        }
    }
}
