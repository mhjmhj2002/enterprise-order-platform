package com.mercadoaurora.catalog.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Sku {

    private final UUID id;
    private String sellerCode;
    private String ean;
    private Map<String, String> attributes;
    private SkuStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Sku(
            UUID id,
            String sellerCode,
            String ean,
            Map<String, String> attributes,
            SkuStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = requireId(id, "SKU id");
        this.sellerCode = validateSellerCode(sellerCode);
        this.ean = normalizeNullable(ean);
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        this.status = status == null ? SkuStatus.INACTIVE : status;
        this.createdAt = requireInstant(createdAt, "createdAt");
        this.updatedAt = requireInstant(updatedAt, "updatedAt");
    }

    public static Sku create(
            UUID id,
            String sellerCode,
            String ean,
            Map<String, String> attributes,
            SkuStatus status,
            Instant now
    ) {
        Instant timestamp = requireInstant(now, "now");
        return new Sku(id, sellerCode, ean, attributes, status, timestamp, timestamp);
    }

    public static Sku restore(
            UUID id,
            String sellerCode,
            String ean,
            Map<String, String> attributes,
            SkuStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Sku(id, sellerCode, ean, attributes, status, createdAt, updatedAt);
    }

    public void update(String sellerCode, String ean, Map<String, String> attributes, SkuStatus status, Instant now) {
        this.sellerCode = validateSellerCode(sellerCode);
        this.ean = normalizeNullable(ean);
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        this.status = status == null ? SkuStatus.INACTIVE : status;
        this.updatedAt = requireInstant(now, "now");
    }

    public UUID getId() {
        return id;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public String getEan() {
        return ean;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public SkuStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return status == SkuStatus.ACTIVE;
    }

    private static UUID requireId(UUID id, String field) {
        if (id == null) {
            throw new DomainValidationException(field + " is required");
        }
        return id;
    }

    private static Instant requireInstant(Instant instant, String field) {
        if (instant == null) {
            throw new DomainValidationException(field + " is required");
        }
        return instant;
    }

    private static String validateSellerCode(String value) {
        String normalized = normalizeNullable(value);
        if (normalized == null) {
            throw new DomainValidationException("sellerCode is required");
        }
        return normalized;
    }

    private static String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sku sku)) {
            return false;
        }
        return Objects.equals(id, sku.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
