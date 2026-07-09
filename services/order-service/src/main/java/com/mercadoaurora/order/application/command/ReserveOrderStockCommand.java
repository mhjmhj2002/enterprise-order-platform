package com.mercadoaurora.order.application.command;
import java.util.List;
import java.util.UUID;
public record ReserveOrderStockCommand(UUID orderId, List<UUID> reservationRefs) {
}
