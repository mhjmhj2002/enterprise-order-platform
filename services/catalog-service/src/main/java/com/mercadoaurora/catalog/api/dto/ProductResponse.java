package com.mercadoaurora.catalog.api.dto;

import com.mercadoaurora.catalog.domain.ProductStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        ProductStatus status,
        UUID brandId,
        UUID categoryId,
        Map<String, String> specifications,
        List<String> images,
        List<SkuResponse> skus,
        Instant createdAt,
        Instant updatedAt
) {
}
