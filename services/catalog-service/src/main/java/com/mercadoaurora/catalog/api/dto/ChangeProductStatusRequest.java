package com.mercadoaurora.catalog.api.dto;

import com.mercadoaurora.catalog.domain.ProductStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeProductStatusRequest(@NotNull ProductStatus status) {
}
