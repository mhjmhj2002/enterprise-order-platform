package com.mercadoaurora.order.application.command;
import java.util.List;
import java.util.UUID;
public record CreateOrderCommand(UUID customerId, List<CreateOrderItemInput> items) {
}
