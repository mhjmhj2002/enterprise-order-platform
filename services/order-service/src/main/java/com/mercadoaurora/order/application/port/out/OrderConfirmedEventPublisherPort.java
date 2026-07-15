package com.mercadoaurora.order.application.port.out;

import com.mercadoaurora.order.domain.Order;

public interface OrderConfirmedEventPublisherPort {
    void publish(Order order);
}
