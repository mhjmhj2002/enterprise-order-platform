package com.mercadoaurora.order.infrastructure.persistence.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
    @Column(name = "sku_id", nullable = false)
    private UUID skuId;
    @Column(name = "product_name_snapshot", nullable = false, length = 255)
    private String productNameSnapshot;
    @Column(name = "sku_name_snapshot", nullable = false, length = 255)
    private String skuNameSnapshot;
    @Column(name = "attributes_snapshot", nullable = false, columnDefinition = "TEXT")
    private String attributesSnapshot;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "unit_price_snapshot", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPriceSnapshot;
    @Column(name = "discount_snapshot", nullable = false, precision = 19, scale = 2)
    private BigDecimal discountSnapshot;
    @Column(name = "line_total_snapshot", nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotalSnapshot;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public OrderEntity getOrder() {
        return order;
    }
    public void setOrder(OrderEntity order) {
        this.order = order;
    }
    public UUID getSkuId() {
        return skuId;
    }
    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }
    public String getProductNameSnapshot() {
        return productNameSnapshot;
    }
    public void setProductNameSnapshot(String productNameSnapshot) {
        this.productNameSnapshot = productNameSnapshot;
    }
    public String getSkuNameSnapshot() {
        return skuNameSnapshot;
    }
    public void setSkuNameSnapshot(String skuNameSnapshot) {
        this.skuNameSnapshot = skuNameSnapshot;
    }
    public String getAttributesSnapshot() {
        return attributesSnapshot;
    }
    public void setAttributesSnapshot(String attributesSnapshot) {
        this.attributesSnapshot = attributesSnapshot;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }
    public void setUnitPriceSnapshot(BigDecimal unitPriceSnapshot) {
        this.unitPriceSnapshot = unitPriceSnapshot;
    }
    public BigDecimal getDiscountSnapshot() {
        return discountSnapshot;
    }
    public void setDiscountSnapshot(BigDecimal discountSnapshot) {
        this.discountSnapshot = discountSnapshot;
    }
    public BigDecimal getLineTotalSnapshot() {
        return lineTotalSnapshot;
    }
    public void setLineTotalSnapshot(BigDecimal lineTotalSnapshot) {
        this.lineTotalSnapshot = lineTotalSnapshot;
    }
}
