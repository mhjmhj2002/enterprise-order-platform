package com.mercadoaurora.order.domain;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
public class OrderItem {
    private final UUID skuId;
    private final String productNameSnapshot;
    private final String skuNameSnapshot;
    private final Map<String, String> attributesSnapshot;
    private final int quantity;
    private final BigDecimal unitPriceSnapshot;
    private final BigDecimal discountSnapshot;
    private final BigDecimal lineTotalSnapshot;
    private OrderItem(
            UUID skuId,
            String productNameSnapshot,
            String skuNameSnapshot,
            Map<String, String> attributesSnapshot,
            int quantity,
            BigDecimal unitPriceSnapshot,
            BigDecimal discountSnapshot,
            BigDecimal lineTotalSnapshot
    ) {
        this.skuId = requireId(skuId, "skuId");
        this.productNameSnapshot = requireText(productNameSnapshot, "productNameSnapshot");
        this.skuNameSnapshot = requireText(skuNameSnapshot, "skuNameSnapshot");
        this.attributesSnapshot = attributesSnapshot == null ? Map.of() : Map.copyOf(new LinkedHashMap<>(attributesSnapshot));
        this.quantity = requirePositive(quantity, "quantity");
        this.unitPriceSnapshot = requireMoney(unitPriceSnapshot, "unitPriceSnapshot");
        this.discountSnapshot = requireMoney(discountSnapshot, "discountSnapshot");
        this.lineTotalSnapshot = requireMoney(lineTotalSnapshot, "lineTotalSnapshot");
        validateTotals();
    }
    public static OrderItem create(
            UUID skuId,
            String productNameSnapshot,
            String skuNameSnapshot,
            Map<String, String> attributesSnapshot,
            int quantity,
            BigDecimal unitPriceSnapshot,
            BigDecimal discountSnapshot,
            BigDecimal lineTotalSnapshot
    ) {
        return new OrderItem(
                skuId,
                productNameSnapshot,
                skuNameSnapshot,
                attributesSnapshot,
                quantity,
                unitPriceSnapshot,
                discountSnapshot,
                lineTotalSnapshot
        );
    }
    private void validateTotals() {
        if (discountSnapshot.compareTo(unitPriceSnapshot) > 0) {
            throw new DomainValidationException("discountSnapshot must be less than or equal to unitPriceSnapshot");
        }
        BigDecimal expectedLine = unitPriceSnapshot.subtract(discountSnapshot).multiply(BigDecimal.valueOf(quantity));
        if (lineTotalSnapshot.compareTo(expectedLine) != 0) {
            throw new DomainValidationException("lineTotalSnapshot is inconsistent with unitPriceSnapshot, discountSnapshot and quantity");
        }
    }
    public UUID getSkuId() {
        return skuId;
    }
    public String getProductNameSnapshot() {
        return productNameSnapshot;
    }
    public String getSkuNameSnapshot() {
        return skuNameSnapshot;
    }
    public Map<String, String> getAttributesSnapshot() {
        return attributesSnapshot;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }
    public BigDecimal getDiscountSnapshot() {
        return discountSnapshot;
    }
    public BigDecimal getLineTotalSnapshot() {
        return lineTotalSnapshot;
    }
    private static UUID requireId(UUID value, String field) {
        if (value == null) {
            throw new DomainValidationException(field + " is required");
        }
        return value;
    }
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(field + " is required");
        }
        return value;
    }
    private static int requirePositive(int value, String field) {
        if (value <= 0) {
            throw new DomainValidationException(field + " must be greater than zero");
        }
        return value;
    }
    private static BigDecimal requireMoney(BigDecimal value, String field) {
        if (value == null) {
            throw new DomainValidationException(field + " is required");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException(field + " must be greater than or equal to zero");
        }
        return value.stripTrailingZeros();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem orderItem)) {
            return false;
        }
        return Objects.equals(skuId, orderItem.skuId)
                && Objects.equals(productNameSnapshot, orderItem.productNameSnapshot)
                && Objects.equals(skuNameSnapshot, orderItem.skuNameSnapshot);
    }
    @Override
    public int hashCode() {
        return Objects.hash(skuId, productNameSnapshot, skuNameSnapshot);
    }
}
