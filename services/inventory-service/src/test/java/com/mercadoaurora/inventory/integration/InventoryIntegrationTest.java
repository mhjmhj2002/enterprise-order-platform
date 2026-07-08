package com.mercadoaurora.inventory.integration;

import com.mercadoaurora.inventory.api.dto.AdjustPhysicalStockRequest;
import com.mercadoaurora.inventory.api.dto.CreateInventoryItemRequest;
import com.mercadoaurora.inventory.api.dto.InventoryItemResponse;
import com.mercadoaurora.inventory.api.dto.ReserveStockRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("inventory")
            .withUsername("inventory")
            .withPassword("inventory");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndGetInventoryItem() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        ResponseEntity<InventoryItemResponse> created = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 20),
                InventoryItemResponse.class
        );

        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        assertEquals(20, created.getBody().physicalQuantity());
        assertEquals(20, created.getBody().availableQuantity());

        ResponseEntity<InventoryItemResponse> loaded = restTemplate.getForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId),
                InventoryItemResponse.class
        );

        assertEquals(HttpStatus.OK, loaded.getStatusCode());
        assertEquals(skuId, loaded.getBody().skuId());
    }

    @Test
    void shouldReserveAndCommitStock() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 10),
                InventoryItemResponse.class
        );

        ResponseEntity<InventoryItemResponse> reserved = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/reservations"),
                new ReserveStockRequest(reservationId, 4),
                InventoryItemResponse.class
        );

        assertEquals(HttpStatus.CREATED, reserved.getStatusCode());
        assertEquals(4, reserved.getBody().reservedQuantity());
        assertEquals(6, reserved.getBody().availableQuantity());

        ResponseEntity<InventoryItemResponse> committed = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/reservations/" + reservationId + "/commit"),
                null,
                InventoryItemResponse.class
        );

        assertEquals(HttpStatus.OK, committed.getStatusCode());
        assertEquals(6, committed.getBody().physicalQuantity());
        assertEquals(0, committed.getBody().reservedQuantity());
    }

    @Test
    void shouldReserveAndReleaseStock() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 10),
                InventoryItemResponse.class
        );

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/reservations"),
                new ReserveStockRequest(reservationId, 3),
                InventoryItemResponse.class
        );

        ResponseEntity<InventoryItemResponse> released = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/reservations/" + reservationId + "/release"),
                null,
                InventoryItemResponse.class
        );

        assertEquals(HttpStatus.OK, released.getStatusCode());
        assertEquals(10, released.getBody().physicalQuantity());
        assertEquals(0, released.getBody().reservedQuantity());
        assertEquals(10, released.getBody().availableQuantity());
    }

    @Test
    void shouldAdjustPhysicalStock() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 10),
                InventoryItemResponse.class
        );

        InventoryItemResponse adjusted = restTemplate.patchForObject(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/adjust"),
                new AdjustPhysicalStockRequest(5),
                InventoryItemResponse.class
        );

        ResponseEntity<InventoryItemResponse> loaded = restTemplate.getForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId),
                InventoryItemResponse.class
        );

        assertNotNull(adjusted);
        assertEquals(HttpStatus.OK, loaded.getStatusCode());
        assertEquals(15, loaded.getBody().physicalQuantity());
    }

    @Test
    void shouldReturnBadRequestWhenReservationExceedsAvailable() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 2),
                InventoryItemResponse.class
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory/" + skuId + "/" + warehouseId + "/reservations"),
                new ReserveStockRequest(UUID.randomUUID(), 3),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnConflictWhenCreatingDuplicatedInventoryItem() {
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 2),
                InventoryItemResponse.class
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl("/api/v1/inventory"),
                new CreateInventoryItemRequest(skuId, warehouseId, 3),
                String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    private String baseUrl(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }
}
