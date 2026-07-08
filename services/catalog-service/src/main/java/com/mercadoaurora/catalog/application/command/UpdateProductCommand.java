package com.mercadoaurora.catalog.application.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        String name,
        String description,
        UUID brandId,
        UUID categoryId,
        Map<String, String> specifications,
        List<String> images
) {
}
