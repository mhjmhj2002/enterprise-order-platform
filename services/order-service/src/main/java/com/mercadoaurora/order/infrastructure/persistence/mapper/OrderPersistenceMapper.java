package com.mercadoaurora.order.infrastructure.persistence.mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
import com.mercadoaurora.order.domain.OrderStatus;
import com.mercadoaurora.order.domain.PaymentStatus;
import com.mercadoaurora.order.infrastructure.persistence.entity.OrderEntity;
import com.mercadoaurora.order.infrastructure.persistence.entity.OrderItemEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
public final class OrderPersistenceMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private OrderPersistenceMapper() {
    }
    public static OrderEntity toEntity(Order source, OrderEntity target) {
        target.setId(source.getId());
        target.setCustomerId(source.getCustomerId());
        target.setStatus(source.getStatus().name());
        target.setPaymentStatus(source.getPaymentStatus().name());
        target.setTotalAmount(source.getTotalAmount());
        target.setReservationRefs(joinReservationRefs(source.getReservationRefs()));
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setConfirmedAt(source.getConfirmedAt());
        target.setCancelledAt(source.getCancelledAt());
        target.getItems().clear();
        source.getItems().forEach(item -> {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setOrder(target);
            itemEntity.setSkuId(item.getSkuId());
            itemEntity.setProductNameSnapshot(item.getProductNameSnapshot());
            itemEntity.setSkuNameSnapshot(item.getSkuNameSnapshot());
            itemEntity.setAttributesSnapshot(writeAttributes(item.getAttributesSnapshot()));
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setUnitPriceSnapshot(item.getUnitPriceSnapshot());
            itemEntity.setDiscountSnapshot(item.getDiscountSnapshot());
            itemEntity.setLineTotalSnapshot(item.getLineTotalSnapshot());
            target.getItems().add(itemEntity);
        });
        return target;
    }
    public static Order toDomain(OrderEntity entity) {
        List<OrderItem> items = new ArrayList<>();
        entity.getItems().forEach(item -> items.add(OrderItem.create(
                item.getSkuId(),
                item.getProductNameSnapshot(),
                item.getSkuNameSnapshot(),
                readAttributes(item.getAttributesSnapshot()),
                item.getQuantity(),
                item.getUnitPriceSnapshot(),
                item.getDiscountSnapshot(),
                item.getLineTotalSnapshot()
        )));
        return Order.restore(
                entity.getId(),
                entity.getCustomerId(),
                items,
                splitReservationRefs(entity.getReservationRefs()),
                OrderStatus.valueOf(entity.getStatus()),
                PaymentStatus.valueOf(entity.getPaymentStatus()),
                entity.getTotalAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getConfirmedAt(),
                entity.getCancelledAt()
        );
    }
    private static String joinReservationRefs(List<UUID> refs) {
        return refs == null ? "" : refs.stream().map(UUID::toString).reduce((a, b) -> a + "," + b).orElse("");
    }
    private static List<UUID> splitReservationRefs(String refs) {
        if (refs == null || refs.isBlank()) {
            return List.of();
        }
        return Arrays.stream(refs.split(",")).map(String::trim).filter(text -> !text.isBlank()).map(UUID::fromString).toList();
    }
    private static String writeAttributes(Map<String, String> attributes) {
        try {
            return OBJECT_MAPPER.writeValueAsString(attributes == null ? Map.of() : attributes);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize order item attributes", exception);
        }
    }
    private static Map<String, String> readAttributes(String rawAttributes) {
        try {
            if (rawAttributes == null || rawAttributes.isBlank()) {
                return Map.of();
            }
            return OBJECT_MAPPER.readValue(rawAttributes, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to deserialize order item attributes", exception);
        }
    }
}
