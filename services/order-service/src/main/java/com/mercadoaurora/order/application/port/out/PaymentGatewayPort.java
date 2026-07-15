package com.mercadoaurora.order.application.port.out;

import com.mercadoaurora.order.domain.Order;

public interface PaymentGatewayPort {
    void startPayment(Order order);
}
