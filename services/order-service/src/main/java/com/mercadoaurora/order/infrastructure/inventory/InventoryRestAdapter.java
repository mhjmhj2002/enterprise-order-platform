package com.mercadoaurora.order.infrastructure.inventory;

import com.mercadoaurora.order.application.exception.OrderIntegrationException;
import com.mercadoaurora.order.application.port.out.InventoryReservationPort;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryRestAdapter implements InventoryReservationPort {
    private final RestClient restClient;
    private final UUID defaultWarehouseId;

    public InventoryRestAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${order.integrations.inventory.base-url}") String inventoryBaseUrl,
            @Value("${order.integrations.inventory.default-warehouse-id}") UUID defaultWarehouseId
    ) {
        this.restClient = restClientBuilder.baseUrl(inventoryBaseUrl).build();
        this.defaultWarehouseId = defaultWarehouseId;
    }

    @Override
    public void reserveStock(Order order, List<UUID> reservationRefs) {
        List<OrderItem> items = order.getItems();
        for (int index = 0; index < items.size(); index++) {
            OrderItem item = items.get(index);
            UUID reservationRef = reservationRefs.get(index);
            post(
                    "/api/v1/inventory/{skuId}/{warehouseId}/reservations",
                    new ReserveStockRequest(reservationRef, item.getQuantity()),
                    item.getSkuId(),
                    defaultWarehouseId
            );
        }
    }

    @Override
    public void commitReservations(Order order) {
        applyToReservations(order, "commit");
    }

    @Override
    public void releaseReservations(Order order) {
        applyToReservations(order, "release");
    }

    private void applyToReservations(Order order, String action) {
        List<OrderItem> items = order.getItems();
        List<UUID> reservationRefs = order.getReservationRefs();
        for (int index = 0; index < reservationRefs.size(); index++) {
            post(
                    "/api/v1/inventory/{skuId}/{warehouseId}/reservations/{reservationId}/" + action,
                    null,
                    items.get(index).getSkuId(),
                    defaultWarehouseId,
                    reservationRefs.get(index)
            );
        }
    }

    private void post(String path, Object body, Object... uriVariables) {
        try {
            RestClient.RequestBodySpec request = restClient.post().uri(path, uriVariables);
            if (body == null) {
                request.retrieve().toBodilessEntity();
                return;
            }
            request.body(body).retrieve().toBodilessEntity();
        } catch (RestClientException exception) {
            throw new OrderIntegrationException("Inventory integration failed");
        }
    }

    private record ReserveStockRequest(UUID reservationId, Integer quantity) {
    }
}
