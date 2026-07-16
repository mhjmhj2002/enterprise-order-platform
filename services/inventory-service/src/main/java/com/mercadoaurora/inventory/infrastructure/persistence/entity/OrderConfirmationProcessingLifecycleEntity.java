package com.mercadoaurora.inventory.infrastructure.persistence.entity;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_confirmation_processing_lifecycle")
public class OrderConfirmationProcessingLifecycleEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private UUID eventId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderConfirmationProcessingLifecycle.Milestone milestone;
    @Column(nullable = false) private Instant occurredAt;
    private String failureCategory;

    public Long getId() { return id; }
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public OrderConfirmationProcessingLifecycle.Milestone getMilestone() { return milestone; }
    public void setMilestone(OrderConfirmationProcessingLifecycle.Milestone milestone) { this.milestone = milestone; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public String getFailureCategory() { return failureCategory; }
    public void setFailureCategory(String failureCategory) { this.failureCategory = failureCategory; }
}
