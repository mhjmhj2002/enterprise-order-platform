package com.mercadoaurora.order.api.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
public record CreateOrderItemRequest(
        @NotNull UUID skuId,
        @NotBlank String productNameSnapshot,
        @NotBlank String skuNameSnapshot,
        Map<String, String> attributesSnapshot,
        @Min(1) int quantity,
        @NotNull @DecimalMin(value = "0.0") BigDecimal unitPriceSnapshot,
        @NotNull @DecimalMin(value = "0.0") BigDecimal discountSnapshot,
        @NotNull @DecimalMin(value = "0.0") BigDecimal lineTotalSnapshot
) {
}
