package com.mercadoaurora.inventory.api.dto;

import com.mercadoaurora.inventory.domain.OrderConfirmationProcessingLifecycle;

import java.time.Instant;

public record OrderConfirmationLifecycleResponse(OrderConfirmationProcessingLifecycle.Milestone milestone,
                                                 Instant occurredAt, String failureCategory) { }
