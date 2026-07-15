package com.mercadoaurora.order.application.port.out;

import com.mercadoaurora.order.domain.Order;

import java.util.List;
import java.util.UUID;

public interface InventoryReservationPort {
    void reserveStock(Order order, List<UUID> reservationRefs);

    void commitReservations(Order order);

    void releaseReservations(Order order);
}
