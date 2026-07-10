package com.mercadoaurora.order.infrastructure.payment;

import com.mercadoaurora.order.application.exception.PaymentProcessingException;
import com.mercadoaurora.order.application.port.out.PaymentGatewayPort;
import com.mercadoaurora.order.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentFakeAdapter implements PaymentGatewayPort {
    private final boolean fail;

    public PaymentFakeAdapter(@Value("${order.payment.fake.fail:false}") boolean fail) {
        this.fail = fail;
    }

    @Override
    public void startPayment(Order order) {
        if (fail) {
            throw new PaymentProcessingException("Payment provider unavailable");
        }
    }
}
