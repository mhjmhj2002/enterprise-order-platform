package com.mercadoaurora.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_confirmation_evidence")
public class OrderConfirmationEvidenceEntity {
    @Id
    private UUID eventId;
    @Column(nullable = false)
    private UUID correlationId;
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private Instant occurredAt;
    @Column(nullable = false)
    private Instant recognizedAt;
    @Column(nullable = false)
    private String topic;
    @Column(name = "partition_number", nullable = false)
    private int partition;
    @Column(name = "offset_value", nullable = false)
    private long offset;

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public Instant getRecognizedAt() { return recognizedAt; }
    public void setRecognizedAt(Instant recognizedAt) { this.recognizedAt = recognizedAt; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public int getPartition() { return partition; }
    public void setPartition(int partition) { this.partition = partition; }
    public long getOffset() { return offset; }
    public void setOffset(long offset) { this.offset = offset; }
}
