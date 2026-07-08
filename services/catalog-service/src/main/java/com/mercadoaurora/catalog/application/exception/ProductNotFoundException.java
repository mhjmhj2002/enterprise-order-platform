package com.mercadoaurora.catalog.application.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID productId) {
        super("Product %s not found".formatted(productId));
    }
}
