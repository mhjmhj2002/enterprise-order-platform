CREATE TABLE order_confirmation_evidence (
    event_id UUID PRIMARY KEY,
    correlation_id UUID NOT NULL,
    order_id UUID NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    recognized_at TIMESTAMPTZ NOT NULL,
    topic VARCHAR(255) NOT NULL,
    partition_number INTEGER NOT NULL,
    offset_value BIGINT NOT NULL
);

CREATE INDEX ix_order_confirmation_evidence_order_id ON order_confirmation_evidence (order_id);
