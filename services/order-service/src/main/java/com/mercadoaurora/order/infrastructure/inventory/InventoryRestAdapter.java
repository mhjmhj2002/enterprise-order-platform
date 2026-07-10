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
    private final InventoryHttpClient inventoryHttpClient;
    private final UUID defaultWarehouseId;

    public InventoryRestAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${order.integrations.inventory.base-url}") String inventoryBaseUrl,
            @Value("${order.integrations.inventory.default-warehouse-id}") UUID defaultWarehouseId
    ) {
        this(new RestClientInventoryHttpClient(restClientBuilder.baseUrl(inventoryBaseUrl).build()), defaultWarehouseId);
    }

    InventoryRestAdapter(InventoryHttpClient inventoryHttpClient, UUID defaultWarehouseId) {
        this.inventoryHttpClient = inventoryHttpClient;
        this.defaultWarehouseId = defaultWarehouseId;
    }

    @Override
    public void reserveStock(Order order, List<UUID> reservationRefs) {
        List<OrderItem> items = order.getItems();
        int reservedItems = 0;
        for (int index = 0; index < items.size(); index++) {
            OrderItem item = items.get(index);
            UUID reservationRef = reservationRefs.get(index);
            try {
                post(
                        "/api/v1/inventory/{skuId}/{warehouseId}/reservations",
                        new ReserveStockRequest(reservationRef, item.getQuantity()),
                        item.getSkuId(),
                        defaultWarehouseId
                );
                reservedItems++;
            } catch (OrderIntegrationException exception) {
                releaseReservedItems(items, reservationRefs, reservedItems);
                throw exception;
            }
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
            applyReservationAction(items.get(index).getSkuId(), reservationRefs.get(index), action);
        }
    }

    private void releaseReservedItems(List<OrderItem> items, List<UUID> reservationRefs, int reservedItems) {
        for (int index = 0; index < reservedItems; index++) {
            try {
                applyReservationAction(items.get(index).getSkuId(), reservationRefs.get(index), "release");
            } catch (OrderIntegrationException exception) {
                // Keep the original reservation failure as the operation outcome.
            }
        }
    }

    private void applyReservationAction(UUID skuId, UUID reservationRef, String action) {
        post(
                "/api/v1/inventory/{skuId}/{warehouseId}/reservations/{reservationId}/" + action,
                null,
                skuId,
                defaultWarehouseId,
                reservationRef
        );
    }

    private void post(String path, Object body, Object... uriVariables) {
        inventoryHttpClient.post(path, body, uriVariables);
    }

    private record ReserveStockRequest(UUID reservationId, Integer quantity) {
    }
}

interface InventoryHttpClient {
    void post(String path, Object body, Object... uriVariables);
}

class RestClientInventoryHttpClient implements InventoryHttpClient {
    private final RestClient restClient;

    RestClientInventoryHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void post(String path, Object body, Object... uriVariables) {
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
}
