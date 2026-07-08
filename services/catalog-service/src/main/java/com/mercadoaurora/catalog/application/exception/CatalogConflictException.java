package com.mercadoaurora.catalog.application.exception;

public class CatalogConflictException extends RuntimeException {

    public CatalogConflictException(String message) {
        super(message);
    }
}
