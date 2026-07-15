CREATE TABLE order_confirmation_processing (
    event_id UUID PRIMARY KEY,
    correlation_id UUID NOT NULL,
    order_id UUID NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    topic VARCHAR(255) NOT NULL,
    partition_number INTEGER NOT NULL,
    offset_value BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempt_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ NULL,
    CONSTRAINT ck_order_confirmation_processing_status CHECK (status IN ('PENDING', 'COMPLETED'))
);

CREATE INDEX ix_order_confirmation_processing_pending ON order_confirmation_processing (status, created_at);
