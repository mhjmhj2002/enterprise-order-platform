package com.mercadoaurora.inventory.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventory_items")
public class InventoryItemEntity {

    @EmbeddedId
    private InventoryItemKey id;

    @Column(name = "physical_quantity", nullable = false)
    private int physicalQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @OneToMany(mappedBy = "inventoryItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationEntity> reservations = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public InventoryItemKey getId() {
        return id;
    }

    public void setId(InventoryItemKey id) {
        this.id = id;
    }

    public int getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(int physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
