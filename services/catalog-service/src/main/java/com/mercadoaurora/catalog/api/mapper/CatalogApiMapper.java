package com.mercadoaurora.catalog.api.mapper;

import com.mercadoaurora.catalog.api.dto.ProductResponse;
import com.mercadoaurora.catalog.api.dto.SkuResponse;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.Sku;

public final class CatalogApiMapper {

    private CatalogApiMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStatus(),
                product.getBrandId(),
                product.getCategoryId(),
                product.getSpecifications(),
                product.getImages(),
                product.getSkus().stream().map(CatalogApiMapper::toResponse).toList(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static SkuResponse toResponse(Sku sku) {
        return new SkuResponse(
                sku.getId(),
                sku.getSellerCode(),
                sku.getEan(),
                sku.getAttributes(),
                sku.getStatus(),
                sku.getCreatedAt(),
                sku.getUpdatedAt()
        );
    }
}
