package com.mercadoaurora.catalog.infrastructure.persistence.repository;

import com.mercadoaurora.catalog.domain.ProductStatus;
import com.mercadoaurora.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> {

    @EntityGraph(attributePaths = "skus")
    Optional<ProductEntity> findById(UUID id);

    Page<ProductEntity> findByStatus(ProductStatus status, Pageable pageable);
}
