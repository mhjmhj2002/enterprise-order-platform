package com.mercadoaurora.inventory.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateInventoryItemRequest(
        @NotNull UUID skuId,
        @NotNull UUID warehouseId,
        @NotNull @Min(0) Integer physicalQuantity
) {
}
