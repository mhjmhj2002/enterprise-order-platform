package com.mercadoaurora.catalog.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDomainTest {

    @Test
    void shouldNotAllowActiveProductWithoutActiveSku() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");

        Product product = Product.create(
                UUID.randomUUID(),
                "Notebook",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of("ram", "16gb"),
                java.util.List.of("https://img/1"),
                now
        );

        assertThrows(DomainValidationException.class, () -> product.changeStatus(ProductStatus.ACTIVE, now));
    }

    @Test
    void shouldCreateProduct() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        UUID productId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Product product = Product.create(
                productId,
                "Camiseta",
                "Algodao",
                ProductStatus.INACTIVE,
                brandId,
                categoryId,
                Map.of("material", "cotton"),
                java.util.List.of("https://img/1"),
                now
        );

        assertEquals(productId, product.getId());
        assertEquals("Camiseta", product.getName());
        assertEquals(ProductStatus.INACTIVE, product.getStatus());
        assertEquals(0, product.getSkus().size());
    }

    @Test
    void shouldAddSku() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        Product product = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                java.util.List.of(),
                now
        );

        Sku sku = Sku.create(
                UUID.randomUUID(),
                product.getId(),
                "SELLER-001",
                "789123",
                Map.of("color", "blue"),
                SkuStatus.ACTIVE,
                now
        );

        assertDoesNotThrow(() -> product.addSku(sku, now));
        assertEquals(1, product.getSkus().size());
    }

    @Test
    void shouldChangeStatusWhenAtLeastOneActiveSkuExists() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        Product product = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                java.util.List.of(),
                now
        );

        product.addSku(Sku.create(
                UUID.randomUUID(),
                product.getId(),
                "SELLER-001",
                "789123",
                Map.of(),
                SkuStatus.ACTIVE,
                now
        ), now);

        assertDoesNotThrow(() -> product.changeStatus(ProductStatus.ACTIVE, now));
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    void shouldRequireSellerCode() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        UUID productId = UUID.randomUUID();

        assertThrows(DomainValidationException.class, () -> Sku.create(
                UUID.randomUUID(),
                productId,
                " ",
                "789123",
                Map.of(),
                SkuStatus.ACTIVE,
                now
        ));
    }

    @Test
    void shouldRejectDuplicateSellerCodeWithinProduct() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        Product product = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                java.util.List.of(),
                now
        );

        product.addSku(Sku.create(
                UUID.randomUUID(),
                product.getId(),
                "SELLER-001",
                "789123",
                Map.of(),
                SkuStatus.ACTIVE,
                now
        ), now);

        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> product.addSku(
                Sku.create(
                        UUID.randomUUID(),
                        product.getId(),
                        "seller-001",
                        "789124",
                        Map.of(),
                        SkuStatus.ACTIVE,
                        now
                ), now));

        assertEquals("sellerCode already exists for this product", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateEanWithinProduct() {
        Instant now = Instant.parse("2026-07-08T10:00:00Z");
        Product product = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                java.util.List.of(),
                now
        );

        product.addSku(Sku.create(
                UUID.randomUUID(),
                product.getId(),
                "SELLER-001",
                "789123",
                Map.of(),
                SkuStatus.ACTIVE,
                now
        ), now);

        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> product.addSku(
                Sku.create(
                        UUID.randomUUID(),
                        product.getId(),
                        "SELLER-002",
                        "789123",
                        Map.of(),
                        SkuStatus.ACTIVE,
                        now
                ), now));

        assertEquals("ean already exists for this product", exception.getMessage());
    }
}
