package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.exception.ProductNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProductUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;

    public GetProductUseCase(CatalogRepositoryPort catalogRepositoryPort) {
        this.catalogRepositoryPort = catalogRepositoryPort;
    }

    public Product execute(UUID productId) {
        return catalogRepositoryPort.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
