package com.mercadoaurora.catalog.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Product {

    private final UUID id;
    private String name;
    private String description;
    private ProductStatus status;
    private UUID brandId;
    private UUID categoryId;
    private Map<String, String> specifications;
    private List<String> images;
    private final List<Sku> skus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Product(
            UUID id,
            String name,
            String description,
            ProductStatus status,
            UUID brandId,
            UUID categoryId,
            Map<String, String> specifications,
            List<String> images,
            List<Sku> skus,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = requireId(id, "Product id");
        this.name = requireText(name, "name");
        this.description = normalizeNullable(description);
        this.status = status == null ? ProductStatus.INACTIVE : status;
        this.brandId = requireId(brandId, "brandId");
        this.categoryId = requireId(categoryId, "categoryId");
        this.specifications = specifications == null ? Map.of() : Map.copyOf(specifications);
        this.images = images == null ? List.of() : List.copyOf(images);
        this.skus = skus == null ? new ArrayList<>() : new ArrayList<>(skus);
        this.createdAt = requireInstant(createdAt, "createdAt");
        this.updatedAt = requireInstant(updatedAt, "updatedAt");
        validateInvariantForActiveProduct();
        validateExistingSkuUniqueness(this.skus);
    }

    public static Product create(
            UUID id,
            String name,
            String description,
            ProductStatus status,
            UUID brandId,
            UUID categoryId,
            Map<String, String> specifications,
            List<String> images,
            Instant now
    ) {
        Instant timestamp = requireInstant(now, "now");
        return new Product(
                id,
                name,
                description,
                status == null ? ProductStatus.INACTIVE : status,
                brandId,
                categoryId,
                specifications,
                images,
                List.of(),
                timestamp,
                timestamp
        );
    }

    public static Product restore(
            UUID id,
            String name,
            String description,
            ProductStatus status,
            UUID brandId,
            UUID categoryId,
            Map<String, String> specifications,
            List<String> images,
            List<Sku> skus,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Product(id, name, description, status, brandId, categoryId, specifications, images, skus, createdAt, updatedAt);
    }

    public void updateDetails(
            String name,
            String description,
            UUID brandId,
            UUID categoryId,
            Map<String, String> specifications,
            List<String> images,
            Instant now
    ) {
        this.name = requireText(name, "name");
        this.description = normalizeNullable(description);
        this.brandId = requireId(brandId, "brandId");
        this.categoryId = requireId(categoryId, "categoryId");
        this.specifications = specifications == null ? Map.of() : Map.copyOf(specifications);
        this.images = images == null ? List.of() : List.copyOf(images);
        this.updatedAt = requireInstant(now, "now");
    }

    public void changeStatus(ProductStatus newStatus, Instant now) {
        this.status = newStatus == null ? ProductStatus.INACTIVE : newStatus;
        this.updatedAt = requireInstant(now, "now");
        validateInvariantForActiveProduct();
    }

    public void addSku(Sku sku, Instant now) {
        if (sku == null) {
            throw new DomainValidationException("SKU is required");
        }
        validateUniqueSkuData(this.skus, sku.getSellerCode(), sku.getEan());
        this.skus.add(sku);
        this.updatedAt = requireInstant(now, "now");
        validateInvariantForActiveProduct();
    }

    public void updateSku(
            UUID skuId,
            String sellerCode,
            String ean,
            Map<String, String> attributes,
            SkuStatus status,
            Instant now
    ) {
        Sku sku = getSkuById(skuId).orElseThrow(() -> new DomainValidationException("SKU not found in product"));

        validateUniqueSkuData(
                this.skus.stream().filter(current -> !current.getId().equals(skuId)).toList(),
                sellerCode,
                ean
        );

        sku.update(sellerCode, ean, attributes, status, now);
        this.updatedAt = requireInstant(now, "now");
        validateInvariantForActiveProduct();
    }

    public Optional<Sku> getSkuById(UUID skuId) {
        if (skuId == null) {
            return Optional.empty();
        }
        return skus.stream().filter(sku -> sku.getId().equals(skuId)).findFirst();
    }

    public boolean hasActiveSku() {
        return skus.stream().anyMatch(Sku::isActive);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public UUID getBrandId() {
        return brandId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public List<String> getImages() {
        return images;
    }

    public List<Sku> getSkus() {
        return List.copyOf(skus);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void validateInvariantForActiveProduct() {
        if (status == ProductStatus.ACTIVE && !hasActiveSku()) {
            throw new DomainValidationException("Active product requires at least one active SKU");
        }
    }

    private void validateUniqueSkuData(List<Sku> existingSkus, String sellerCode, String ean) {
        String normalizedSellerCode = normalizeNullable(sellerCode);
        String normalizedEan = normalizeNullable(ean);

        if (normalizedSellerCode == null) {
            throw new DomainValidationException("sellerCode is required");
        }

        boolean duplicatedSellerCode = existingSkus.stream()
                .map(Sku::getSellerCode)
                .filter(Objects::nonNull)
                .anyMatch(existing -> existing.equalsIgnoreCase(normalizedSellerCode));

        if (duplicatedSellerCode) {
            throw new DomainValidationException("sellerCode already exists for this product");
        }

        if (normalizedEan != null) {
            boolean duplicatedEan = existingSkus.stream()
                    .map(Sku::getEan)
                    .filter(Objects::nonNull)
                    .anyMatch(existing -> existing.equalsIgnoreCase(normalizedEan));

            if (duplicatedEan) {
                throw new DomainValidationException("ean already exists for this product");
            }
        }
    }

    private void validateExistingSkuUniqueness(List<Sku> existingSkus) {
        for (Sku sku : existingSkus) {
            validateUniqueSkuData(
                    existingSkus.stream().filter(current -> !current.getId().equals(sku.getId())).toList(),
                    sku.getSellerCode(),
                    sku.getEan()
            );
        }
    }

    private static String requireText(String value, String field) {
        String normalized = normalizeNullable(value);
        if (normalized == null) {
            throw new DomainValidationException(field + " is required");
        }
        return normalized;
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

    private static String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
