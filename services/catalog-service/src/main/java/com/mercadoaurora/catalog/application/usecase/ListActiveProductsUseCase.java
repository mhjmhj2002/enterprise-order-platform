package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListActiveProductsUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;

    public ListActiveProductsUseCase(CatalogRepositoryPort catalogRepositoryPort) {
        this.catalogRepositoryPort = catalogRepositoryPort;
    }

    public List<Product> execute(int page, int size) {
        return catalogRepositoryPort.findActiveProducts(page, size);
    }
}
