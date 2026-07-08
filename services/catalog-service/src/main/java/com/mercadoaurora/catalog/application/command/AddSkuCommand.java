package com.mercadoaurora.catalog.application.command;

import com.mercadoaurora.catalog.domain.SkuStatus;

import java.util.Map;
import java.util.UUID;

public record AddSkuCommand(
        UUID productId,
        String sellerCode,
        String ean,
        Map<String, String> attributes,
        SkuStatus status
) {
}
