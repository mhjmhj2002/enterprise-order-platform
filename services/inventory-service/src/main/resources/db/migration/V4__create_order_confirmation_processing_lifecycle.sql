CREATE TABLE order_confirmation_processing_lifecycle (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL,
    milestone VARCHAR(32) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    failure_category VARCHAR(64) NULL,
    CONSTRAINT fk_order_confirmation_processing_lifecycle_event
        FOREIGN KEY (event_id) REFERENCES order_confirmation_processing (event_id),
    CONSTRAINT ck_order_confirmation_processing_lifecycle_milestone
        CHECK (milestone IN ('REGISTERED', 'TEMPORARY_FAILURE', 'COMPLETED')),
    CONSTRAINT ck_order_confirmation_processing_lifecycle_failure_category
        CHECK ((milestone = 'TEMPORARY_FAILURE' AND failure_category IS NOT NULL)
            OR (milestone <> 'TEMPORARY_FAILURE' AND failure_category IS NULL)),
    CONSTRAINT uk_order_confirmation_processing_lifecycle_event_milestone UNIQUE (event_id, milestone)
);

CREATE INDEX ix_order_confirmation_processing_lifecycle_event ON order_confirmation_processing_lifecycle (event_id, occurred_at, id);
