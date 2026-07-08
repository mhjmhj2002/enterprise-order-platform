package com.mercadoaurora.catalog.infrastructure.persistence.repository;

import com.mercadoaurora.catalog.infrastructure.persistence.entity.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSkuRepository extends JpaRepository<SkuEntity, UUID> {

    Optional<SkuEntity> findById(UUID id);

    boolean existsBySellerCodeIgnoreCase(String sellerCode);

    boolean existsBySellerCodeIgnoreCaseAndIdNot(String sellerCode, UUID id);

    boolean existsByEanIgnoreCase(String ean);

    boolean existsByEanIgnoreCaseAndIdNot(String ean, UUID id);
}
