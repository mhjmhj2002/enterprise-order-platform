package com.mercadoaurora.order.infrastructure.web;

public final class PaymentFakeContext {
    private static final ThreadLocal<Boolean> FAIL_CURRENT_REQUEST = new ThreadLocal<>();

    private PaymentFakeContext() {
    }

    public static boolean shouldFail() {
        return Boolean.TRUE.equals(FAIL_CURRENT_REQUEST.get());
    }

    static void setFailureRequested(boolean failureRequested) {
        FAIL_CURRENT_REQUEST.set(failureRequested);
    }

    static void clear() {
        FAIL_CURRENT_REQUEST.remove();
    }
}
