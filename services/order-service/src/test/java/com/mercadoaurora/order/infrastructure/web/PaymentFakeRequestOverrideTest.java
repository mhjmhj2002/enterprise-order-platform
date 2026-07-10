package com.mercadoaurora.order.infrastructure.web;

import com.mercadoaurora.order.application.exception.PaymentProcessingException;
import com.mercadoaurora.order.infrastructure.payment.PaymentFakeAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentFakeRequestOverrideTest {
    @Test
    void shouldFailPaymentOnlyForRequestWithFailureHeader() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationIdFilter.PAYMENT_FAKE_OUTCOME_HEADER, "FAILED");
        AtomicBoolean failedInsideRequest = new AtomicBoolean();

        filter.doFilter(request, new MockHttpServletResponse(), (ignoredRequest, ignoredResponse) ->
                failedInsideRequest.set(assertThrows(
                        PaymentProcessingException.class,
                        () -> new PaymentFakeAdapter(false).startPayment(null)
                ) != null)
        );

        assertTrue(failedInsideRequest.get());
        assertFalse(PaymentFakeContext.shouldFail());
    }
}
