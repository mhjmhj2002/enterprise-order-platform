package com.mercadoaurora.inventory.infrastructure.persistence.repository;

import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemKey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataInventoryItemRepository extends JpaRepository<InventoryItemEntity, InventoryItemKey> {

    @EntityGraph(attributePaths = "reservations")
    Optional<InventoryItemEntity> findById(InventoryItemKey key);

    @EntityGraph(attributePaths = "reservations")
    List<InventoryItemEntity> findByIdSkuId(UUID skuId);
}
