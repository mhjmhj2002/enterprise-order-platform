package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.command.AddSkuCommand;
import com.mercadoaurora.catalog.application.exception.CatalogConflictException;
import com.mercadoaurora.catalog.application.exception.ProductNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.Sku;
import com.mercadoaurora.catalog.domain.SkuStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class AddSkuUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;
    private final Clock clock;

    public AddSkuUseCase(CatalogRepositoryPort catalogRepositoryPort, Clock clock) {
        this.catalogRepositoryPort = catalogRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public Sku execute(AddSkuCommand command) {
        Product product = catalogRepositoryPort.findProductById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        if (catalogRepositoryPort.existsSellerCode(command.sellerCode(), null)) {
            throw new CatalogConflictException("sellerCode already exists in catalog");
        }
        if (command.ean() != null && catalogRepositoryPort.existsEan(command.ean(), null)) {
            throw new CatalogConflictException("ean already exists in catalog");
        }

        Instant now = Instant.now(clock);
        Sku sku = Sku.create(
                UUID.randomUUID(),
                product.getId(),
                command.sellerCode(),
                command.ean(),
                command.attributes(),
                command.status() == null ? SkuStatus.INACTIVE : command.status(),
                now
        );

        product.addSku(sku, now);
        Product saved = catalogRepositoryPort.save(product);
        return saved.getSkuById(sku.getId()).orElseThrow();
    }
}
