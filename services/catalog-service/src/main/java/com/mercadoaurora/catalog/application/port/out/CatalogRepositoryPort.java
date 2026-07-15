package com.mercadoaurora.catalog.application.port.out;

import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.Sku;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogRepositoryPort {

    Product save(Product product);

    Optional<Product> findProductById(UUID productId);

    Optional<Sku> findSkuById(UUID skuId);

    List<Product> findActiveProducts(int page, int size);

    boolean existsSellerCode(String sellerCode, UUID excludingSkuId);

    boolean existsEan(String ean, UUID excludingSkuId);
}
