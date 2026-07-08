package com.mercadoaurora.catalog.application.command;

import com.mercadoaurora.catalog.domain.ProductStatus;

import java.util.UUID;

public record ChangeProductStatusCommand(UUID productId, ProductStatus status) {
}
