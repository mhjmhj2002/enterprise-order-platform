package com.mercadoaurora.order.infrastructure.event;

import com.mercadoaurora.order.application.port.out.OrderConfirmedEventPublisherPort;
import com.mercadoaurora.order.domain.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka")
public class NoOpOrderConfirmedEventPublisher implements OrderConfirmedEventPublisherPort {
    @Override
    public void publish(Order order) {
        // Kafka publication is explicitly opt-in to preserve the Sprint 1 flow.
    }
}
