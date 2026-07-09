CREATE TABLE orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    payment_status VARCHAR(32) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    reservation_refs TEXT NOT NULL DEFAULT '',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT chk_orders_status CHECK (status IN ('CREATED', 'STOCK_RESERVED', 'PAYMENT_PENDING', 'PAID', 'CONFIRMED', 'CANCELLED')),
    CONSTRAINT chk_orders_payment_status CHECK (payment_status IN ('NOT_STARTED', 'PENDING', 'PAID', 'FAILED')),
    CONSTRAINT chk_orders_total_amount_non_negative CHECK (total_amount >= 0)
);
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    sku_id UUID NOT NULL,
    product_name_snapshot VARCHAR(255) NOT NULL,
    sku_name_snapshot VARCHAR(255) NOT NULL,
    attributes_snapshot TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price_snapshot NUMERIC(19, 2) NOT NULL,
    discount_snapshot NUMERIC(19, 2) NOT NULL,
    line_total_snapshot NUMERIC(19, 2) NOT NULL,
    CONSTRAINT chk_order_items_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_order_items_unit_price_non_negative CHECK (unit_price_snapshot >= 0),
    CONSTRAINT chk_order_items_discount_non_negative CHECK (discount_snapshot >= 0),
    CONSTRAINT chk_order_items_discount_not_greater_than_price CHECK (discount_snapshot <= unit_price_snapshot),
    CONSTRAINT chk_order_items_line_total_non_negative CHECK (line_total_snapshot >= 0)
);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
