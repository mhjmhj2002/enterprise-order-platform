package com.mercadoaurora.catalog.application.usecase;

import com.mercadoaurora.catalog.application.exception.SkuNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Sku;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetSkuUseCase {

    private final CatalogRepositoryPort catalogRepositoryPort;

    public GetSkuUseCase(CatalogRepositoryPort catalogRepositoryPort) {
        this.catalogRepositoryPort = catalogRepositoryPort;
    }

    public Sku execute(UUID skuId) {
        return catalogRepositoryPort.findSkuById(skuId)
                .orElseThrow(() -> new SkuNotFoundException(skuId));
    }
}
