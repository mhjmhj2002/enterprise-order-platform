package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.command.CreateProductCommand;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.ProductStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateProductUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;
    private final Clock clock;

    public CreateProductUseCase(CatalogRepositoryPort catalogRepositoryPort, Clock clock) {
        this.catalogRepositoryPort = catalogRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public Product execute(CreateProductCommand command) {
        Instant now = Instant.now(clock);
        Product product = Product.create(
                UUID.randomUUID(),
                command.name(),
                command.description(),
                command.status() == null ? ProductStatus.INACTIVE : command.status(),
                command.brandId(),
                command.categoryId(),
                command.specifications(),
                command.images(),
                now
        );
        return catalogRepositoryPort.save(product);
    }
}
