package com.mercadoaurora.inventory.api.dto;

import jakarta.validation.constraints.NotNull;

public record AdjustPhysicalStockRequest(
        @NotNull Integer quantityDelta
) {
}
