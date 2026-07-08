package com.mercadoaurora.catalog.application.command;

import com.mercadoaurora.catalog.domain.ProductStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateProductCommand(
        String name,
        String description,
        ProductStatus status,
        UUID brandId,
        UUID categoryId,
        Map<String, String> specifications,
        List<String> images
) {
}
