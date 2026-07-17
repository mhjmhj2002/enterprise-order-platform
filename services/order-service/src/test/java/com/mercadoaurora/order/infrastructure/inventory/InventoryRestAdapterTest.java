package com.mercadoaurora.order.infrastructure.inventory;

import com.mercadoaurora.order.application.exception.OrderIntegrationException;
import com.mercadoaurora.order.config.SecurityApiProperties;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryRestAdapterTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(InventoryRestAdapterConfig.class)
            .withPropertyValues(
                    "order.integrations.inventory.base-url=http://localhost:8082",
                    "order.integrations.inventory.default-warehouse-id=00000000-0000-0000-0000-000000000001",
                    "security.api.username=test-api-consumer",
                    "security.api.password=test-api-password"
            );

    @Test
    void shouldInstantiateAdapterWithSpringConstructorInjection() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(InventoryRestAdapter.class));
    }

    @Test
    void shouldReleaseSuccessfulReservationsWhenLaterItemFails() {
        FakeInventoryHttpClient httpClient = new FakeInventoryHttpClient(2);
        UUID warehouseId = UUID.randomUUID();
        InventoryRestAdapter adapter = new InventoryRestAdapter(httpClient, warehouseId);
        UUID firstReservation = UUID.randomUUID();
        UUID secondReservation = UUID.randomUUID();
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(
                buildItem("Produto 1", "SKU 1"),
                buildItem("Produto 2", "SKU 2")
        ), Instant.now());
        assertThrows(OrderIntegrationException.class,
                () -> adapter.reserveStock(order, List.of(firstReservation, secondReservation)));
        assertEquals(3, httpClient.calls.size());
        assertEquals(new Call(
                "/api/v1/inventory/{skuId}/{warehouseId}/reservations",
                List.of(order.getItems().get(0).getSkuId(), warehouseId)
        ), httpClient.calls.get(0));
        assertEquals(new Call(
                "/api/v1/inventory/{skuId}/{warehouseId}/reservations",
                List.of(order.getItems().get(1).getSkuId(), warehouseId)
        ), httpClient.calls.get(1));
        assertEquals(new Call(
                "/api/v1/inventory/{skuId}/{warehouseId}/reservations/{reservationId}/release",
                List.of(order.getItems().get(0).getSkuId(), warehouseId, firstReservation)
        ), httpClient.calls.get(2));
    }

    private OrderItem buildItem(String productName, String skuName) {
        return OrderItem.create(
                UUID.randomUUID(),
                productName,
                skuName,
                Map.of(),
                1,
                new BigDecimal("10.00"),
                BigDecimal.ZERO,
                new BigDecimal("10.00")
        );
    }

    private record Call(String path, List<Object> uriVariables) {
    }

    private static class FakeInventoryHttpClient implements InventoryHttpClient {
        private final List<Call> calls = new ArrayList<>();
        private final int failingCall;

        private FakeInventoryHttpClient(int failingCall) {
            this.failingCall = failingCall;
        }

        @Override
        public void post(String path, Object body, Object... uriVariables) {
            calls.add(new Call(path, Arrays.asList(uriVariables)));
            if (calls.size() == failingCall) {
                throw new OrderIntegrationException("Inventory integration failed");
            }
        }
    }

    @Configuration
    @Import(InventoryRestAdapter.class)
    @EnableConfigurationProperties(SecurityApiProperties.class)
    static class InventoryRestAdapterConfig {
        @Bean
        RestClient.Builder restClientBuilder() {
            return RestClient.builder();
        }
    }
}
