CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(32) NOT NULL,
    brand_id UUID NOT NULL,
    category_id UUID NOT NULL,
    specifications JSONB NOT NULL DEFAULT '{}'::jsonb,
    images JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE skus (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    seller_code VARCHAR(255) NOT NULL,
    ean VARCHAR(128),
    attributes JSONB NOT NULL DEFAULT '{}'::jsonb,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX ux_skus_seller_code ON skus (LOWER(seller_code));
CREATE UNIQUE INDEX ux_skus_ean ON skus (LOWER(ean)) WHERE ean IS NOT NULL;
CREATE INDEX ix_products_status ON products (status);
CREATE INDEX ix_skus_product_id ON skus (product_id);
