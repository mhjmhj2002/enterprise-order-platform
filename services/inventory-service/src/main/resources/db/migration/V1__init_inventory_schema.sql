CREATE TABLE inventory_items (
    sku_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    physical_quantity INTEGER NOT NULL,
    reserved_quantity INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (sku_id, warehouse_id),
    CONSTRAINT ck_inventory_items_physical_non_negative CHECK (physical_quantity >= 0),
    CONSTRAINT ck_inventory_items_reserved_non_negative CHECK (reserved_quantity >= 0),
    CONSTRAINT ck_inventory_items_reserved_lte_physical CHECK (reserved_quantity <= physical_quantity)
);

CREATE TABLE inventory_reservations (
    id UUID PRIMARY KEY,
    sku_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_inventory_reservation_item
        FOREIGN KEY (sku_id, warehouse_id) REFERENCES inventory_items (sku_id, warehouse_id) ON DELETE CASCADE,
    CONSTRAINT ck_inventory_reservations_quantity_positive CHECK (quantity > 0)
);

CREATE INDEX ix_inventory_items_sku_id ON inventory_items (sku_id);
CREATE INDEX ix_inventory_reservations_item ON inventory_reservations (sku_id, warehouse_id);
