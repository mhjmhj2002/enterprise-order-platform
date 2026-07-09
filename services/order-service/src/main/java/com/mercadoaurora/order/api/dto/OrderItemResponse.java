package com.mercadoaurora.order.api.dto;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
public record OrderItemResponse(
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
