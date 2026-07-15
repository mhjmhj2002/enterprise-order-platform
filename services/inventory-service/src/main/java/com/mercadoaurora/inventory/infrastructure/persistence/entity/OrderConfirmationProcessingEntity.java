package com.mercadoaurora.inventory.infrastructure.persistence.entity;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_confirmation_processing")
public class OrderConfirmationProcessingEntity {
    @Id private UUID eventId;
    @Column(nullable = false) private UUID correlationId;
    @Column(nullable = false) private UUID orderId;
    @Column(nullable = false) private Instant occurredAt;
    @Column(nullable = false) private String topic;
    @Column(name = "partition_number", nullable = false) private int partition;
    @Column(name = "offset_value", nullable = false) private long offset;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderConfirmationProcessing.Status status;
    @Column(nullable = false) private int attemptCount;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    private Instant completedAt;

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public int getPartition() { return partition; }
    public void setPartition(int partition) { this.partition = partition; }
    public long getOffset() { return offset; }
    public void setOffset(long offset) { this.offset = offset; }
    public OrderConfirmationProcessing.Status getStatus() { return status; }
    public void setStatus(OrderConfirmationProcessing.Status status) { this.status = status; }
    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
