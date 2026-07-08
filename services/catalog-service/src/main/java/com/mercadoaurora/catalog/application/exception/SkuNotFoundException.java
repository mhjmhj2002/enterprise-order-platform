package com.mercadoaurora.catalog.application.exception;

import java.util.UUID;

public class SkuNotFoundException extends RuntimeException {

    public SkuNotFoundException(UUID skuId) {
        super("SKU %s not found".formatted(skuId));
    }
}
