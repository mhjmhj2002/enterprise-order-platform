package com.mercadoaurora.catalog.api.dto;

import com.mercadoaurora.catalog.domain.SkuStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record UpdateSkuRequest(
        @NotBlank String sellerCode,
        String ean,
        Map<String, String> attributes,
        SkuStatus status
) {
}
