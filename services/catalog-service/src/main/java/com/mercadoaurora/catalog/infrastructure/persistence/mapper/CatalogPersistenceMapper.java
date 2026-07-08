package com.mercadoaurora.catalog.infrastructure.persistence.mapper;

import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.Sku;
import com.mercadoaurora.catalog.infrastructure.persistence.entity.ProductEntity;
import com.mercadoaurora.catalog.infrastructure.persistence.entity.SkuEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class CatalogPersistenceMapper {

    private CatalogPersistenceMapper() {
    }

    public static Product toDomain(ProductEntity entity) {
        return Product.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getBrandId(),
                entity.getCategoryId(),
                entity.getSpecifications(),
                entity.getImages(),
                entity.getSkus().stream().map(CatalogPersistenceMapper::toDomain).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Sku toDomain(SkuEntity entity) {
        return Sku.restore(
                entity.getId(),
                entity.getSellerCode(),
                entity.getEan(),
                entity.getAttributes(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static ProductEntity toEntity(Product product, ProductEntity entity) {
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setStatus(product.getStatus());
        entity.setBrandId(product.getBrandId());
        entity.setCategoryId(product.getCategoryId());
        entity.setSpecifications(product.getSpecifications());
        entity.setImages(product.getImages());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());

        List<SkuEntity> skuEntities = product.getSkus().stream()
                .map(sku -> toEntity(sku, entity))
                .collect(Collectors.toCollection(java.util.ArrayList::new));
        entity.getSkus().clear();
        entity.getSkus().addAll(skuEntities);
        return entity;
    }

    private static SkuEntity toEntity(Sku sku, ProductEntity product) {
        SkuEntity entity = new SkuEntity();
        entity.setId(sku.getId());
        entity.setProduct(product);
        entity.setSellerCode(sku.getSellerCode());
        entity.setEan(sku.getEan());
        entity.setAttributes(sku.getAttributes());
        entity.setStatus(sku.getStatus());
        entity.setCreatedAt(sku.getCreatedAt());
        entity.setUpdatedAt(sku.getUpdatedAt());
        return entity;
    }
}
