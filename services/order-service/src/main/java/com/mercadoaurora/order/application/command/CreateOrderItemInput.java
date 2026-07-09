package com.mercadoaurora.order.application.command;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
public record CreateOrderItemInput(
        UUID skuId,
        String productNameSnapshot,
        String skuNameSnapshot,
        Map<String, String> attributesSnapshot,
        int quantity,
        BigDecimal unitPriceSnapshot,
        BigDecimal discountSnapshot,
        BigDecimal lineTotalSnapshot
) {
}
