package com.mercadoaurora.order.api.mapper;
import com.mercadoaurora.order.api.dto.CreateOrderRequest;
import com.mercadoaurora.order.api.dto.OrderItemResponse;
import com.mercadoaurora.order.api.dto.OrderResponse;
import com.mercadoaurora.order.application.command.CreateOrderCommand;
import com.mercadoaurora.order.application.command.CreateOrderItemInput;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
public final class OrderApiMapper {
    private OrderApiMapper() {
    }
    public static CreateOrderCommand toCommand(CreateOrderRequest request) {
        return new CreateOrderCommand(
                request.customerId(),
                request.items().stream().map(item -> new CreateOrderItemInput(
                        item.skuId(),
                        item.productNameSnapshot(),
                        item.skuNameSnapshot(),
                        item.attributesSnapshot(),
                        item.quantity(),
                        item.unitPriceSnapshot(),
                        item.discountSnapshot(),
                        item.lineTotalSnapshot()
                )).toList()
        );
    }
    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getItems().stream().map(OrderApiMapper::toItemResponse).toList(),
                order.getTotalAmount(),
                order.getReservationRefs(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getConfirmedAt(),
                order.getCancelledAt()
        );
    }
    private static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getSkuId(),
                item.getProductNameSnapshot(),
                item.getSkuNameSnapshot(),
                item.getAttributesSnapshot(),
                item.getQuantity(),
                item.getUnitPriceSnapshot(),
                item.getDiscountSnapshot(),
                item.getLineTotalSnapshot()
        );
    }
}
