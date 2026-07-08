package com.mercadoaurora.catalog.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UpdateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull UUID brandId,
        @NotNull UUID categoryId,
        Map<String, String> specifications,
        List<String> images
) {
}
