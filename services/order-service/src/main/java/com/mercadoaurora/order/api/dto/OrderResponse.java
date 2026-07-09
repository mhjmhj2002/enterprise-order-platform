package com.mercadoaurora.order.api.dto;
import com.mercadoaurora.order.domain.OrderStatus;
import com.mercadoaurora.order.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        PaymentStatus paymentStatus,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        List<UUID> reservationRefs,
        Instant createdAt,
        Instant updatedAt,
        Instant confirmedAt,
        Instant cancelledAt
) {
}
