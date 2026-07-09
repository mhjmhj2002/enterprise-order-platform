package com.mercadoaurora.order.application.command;
import java.util.UUID;
public record OrderActionCommand(UUID orderId) {
}
