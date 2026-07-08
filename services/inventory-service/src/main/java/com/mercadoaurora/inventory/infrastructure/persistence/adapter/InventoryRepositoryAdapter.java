package com.mercadoaurora.inventory.infrastructure.persistence.adapter;

import com.mercadoaurora.inventory.application.port.out.InventoryRepositoryPort;
import com.mercadoaurora.inventory.domain.InventoryItem;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.InventoryItemKey;
import com.mercadoaurora.inventory.infrastructure.persistence.mapper.InventoryPersistenceMapper;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataInventoryItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class InventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final SpringDataInventoryItemRepository inventoryItemRepository;

    public InventoryRepositoryAdapter(SpringDataInventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    @Transactional
    public InventoryItem save(InventoryItem inventoryItem) {
        InventoryItemKey key = new InventoryItemKey(inventoryItem.getSkuId(), inventoryItem.getWarehouseId());
        InventoryItemEntity entity = inventoryItemRepository.findById(key).orElseGet(InventoryItemEntity::new);
        InventoryItemEntity saved = inventoryItemRepository.save(InventoryPersistenceMapper.toEntity(inventoryItem, entity));
        return InventoryPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findBySkuAndWarehouse(UUID skuId, UUID warehouseId) {
        return inventoryItemRepository.findById(new InventoryItemKey(skuId, warehouseId))
                .map(InventoryPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItem> findBySku(UUID skuId) {
        return inventoryItemRepository.findByIdSkuId(skuId).stream()
                .map(InventoryPersistenceMapper::toDomain)
                .toList();
    }
}
