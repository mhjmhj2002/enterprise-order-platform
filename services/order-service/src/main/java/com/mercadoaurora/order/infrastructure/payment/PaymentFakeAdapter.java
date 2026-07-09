package com.mercadoaurora.order.infrastructure.payment;

import com.mercadoaurora.order.application.port.out.PaymentGatewayPort;
import com.mercadoaurora.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class PaymentFakeAdapter implements PaymentGatewayPort {
    @Override
    public void startPayment(Order order) {
        // Sprint 1 keeps payment externalization as a port without real provider integration.
    }
}
