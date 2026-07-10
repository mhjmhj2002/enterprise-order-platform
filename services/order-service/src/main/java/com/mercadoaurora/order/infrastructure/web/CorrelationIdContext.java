package com.mercadoaurora.order.infrastructure.web;

public final class CorrelationIdContext {
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private CorrelationIdContext() {
    }

    public static String get() {
        return CURRENT.get();
    }

    static void set(String correlationId) {
        CURRENT.set(correlationId);
    }

    static void clear() {
        CURRENT.remove();
    }
}
