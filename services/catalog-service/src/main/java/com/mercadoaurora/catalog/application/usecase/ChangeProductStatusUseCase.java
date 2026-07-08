package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.command.ChangeProductStatusCommand;
import com.mercadoaurora.catalog.application.exception.ProductNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class ChangeProductStatusUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;
    private final Clock clock;

    public ChangeProductStatusUseCase(CatalogRepositoryPort catalogRepositoryPort, Clock clock) {
        this.catalogRepositoryPort = catalogRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public Product execute(ChangeProductStatusCommand command) {
        Product product = catalogRepositoryPort.findProductById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        product.changeStatus(command.status(), Instant.now(clock));
        return catalogRepositoryPort.save(product);
    }
}
