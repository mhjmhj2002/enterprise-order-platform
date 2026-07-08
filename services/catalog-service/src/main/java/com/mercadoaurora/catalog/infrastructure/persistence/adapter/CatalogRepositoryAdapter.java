package com.mercadoaurora.catalog.infrastructure.persistence.adapter;

import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.ProductStatus;
import com.mercadoaurora.catalog.domain.Sku;
import com.mercadoaurora.catalog.infrastructure.persistence.entity.ProductEntity;
import com.mercadoaurora.catalog.infrastructure.persistence.mapper.CatalogPersistenceMapper;
import com.mercadoaurora.catalog.infrastructure.persistence.repository.SpringDataProductRepository;
import com.mercadoaurora.catalog.infrastructure.persistence.repository.SpringDataSkuRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CatalogRepositoryAdapter implements CatalogRepositoryPort {

    private final SpringDataProductRepository productRepository;
    private final SpringDataSkuRepository skuRepository;

    public CatalogRepositoryAdapter(SpringDataProductRepository productRepository, SpringDataSkuRepository skuRepository) {
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = productRepository.findById(product.getId()).orElseGet(ProductEntity::new);
        ProductEntity saved = productRepository.save(CatalogPersistenceMapper.toEntity(product, entity));
        return CatalogPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(UUID productId) {
        return productRepository.findById(productId).map(CatalogPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sku> findSkuById(UUID skuId) {
        return skuRepository.findById(skuId).map(CatalogPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findActiveProducts(int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.max(1, Math.min(size, 100));
        return productRepository.findByStatus(ProductStatus.ACTIVE, PageRequest.of(safePage, safeSize))
                .stream()
                .map(CatalogPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsSellerCode(String sellerCode, UUID excludingSkuId) {
        if (sellerCode == null || sellerCode.isBlank()) {
            return false;
        }
        return excludingSkuId == null
                ? skuRepository.existsBySellerCodeIgnoreCase(sellerCode)
                : skuRepository.existsBySellerCodeIgnoreCaseAndIdNot(sellerCode, excludingSkuId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEan(String ean, UUID excludingSkuId) {
        if (ean == null || ean.isBlank()) {
            return false;
        }
        return excludingSkuId == null
                ? skuRepository.existsByEanIgnoreCase(ean)
                : skuRepository.existsByEanIgnoreCaseAndIdNot(ean, excludingSkuId);
    }
}
