package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.command.UpdateSkuCommand;
import com.mercadoaurora.catalog.application.exception.CatalogConflictException;
import com.mercadoaurora.catalog.application.exception.ProductNotFoundException;
import com.mercadoaurora.catalog.application.exception.SkuNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.Sku;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class UpdateSkuUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;
    private final Clock clock;

    public UpdateSkuUseCase(CatalogRepositoryPort catalogRepositoryPort, Clock clock) {
        this.catalogRepositoryPort = catalogRepositoryPort;
        this.clock = clock;
    }

    @Transactional
    public Sku execute(UpdateSkuCommand command) {
        Product product = catalogRepositoryPort.findProductById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        Sku existingSku = product.getSkuById(command.skuId())
                .orElseThrow(() -> new SkuNotFoundException(command.skuId()));

        if (catalogRepositoryPort.existsSellerCode(command.sellerCode(), existingSku.getId())) {
            throw new CatalogConflictException("sellerCode already exists in catalog");
        }
        if (command.ean() != null && catalogRepositoryPort.existsEan(command.ean(), existingSku.getId())) {
            throw new CatalogConflictException("ean already exists in catalog");
        }

        product.updateSku(
                command.skuId(),
                command.sellerCode(),
                command.ean(),
                command.attributes(),
                command.status(),
                Instant.now(clock)
        );

        Product saved = catalogRepositoryPort.save(product);
        return saved.getSkuById(command.skuId())
                .orElseThrow(() -> new SkuNotFoundException(command.skuId()));
    }
}
