package com.mercadoaurora.catalog.api.dto;

import com.mercadoaurora.catalog.domain.SkuStatus;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SkuResponse(
        UUID id,
        UUID productId,
        String sellerCode,
        String ean,
        Map<String, String> attributes,
        SkuStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
