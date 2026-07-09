package com.mercadoaurora.order.integration;
import com.mercadoaurora.order.api.dto.CreateOrderItemRequest;
import com.mercadoaurora.order.api.dto.CreateOrderRequest;
import com.mercadoaurora.order.api.dto.OrderResponse;
import com.mercadoaurora.order.api.dto.ReserveOrderStockRequest;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("order")
            .withUsername("order")
            .withPassword("order");
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
    void shouldCreateGetAndListOrderByCustomer() {
        UUID customerId = UUID.randomUUID();
        ResponseEntity<OrderResponse> created = restTemplate.postForEntity(
                baseUrl("/api/v1/orders"),
                buildCreateRequest(customerId),
                OrderResponse.class
        );
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        ResponseEntity<OrderResponse> loaded = restTemplate.getForEntity(
                baseUrl("/api/v1/orders/" + created.getBody().id()),
                OrderResponse.class
        );
        assertEquals(HttpStatus.OK, loaded.getStatusCode());
        assertEquals(customerId, loaded.getBody().customerId());
        ResponseEntity<OrderResponse[]> listed = restTemplate.getForEntity(
                baseUrl("/api/v1/customers/" + customerId + "/orders"),
                OrderResponse[].class
        );
        assertEquals(HttpStatus.OK, listed.getStatusCode());
        assertNotNull(listed.getBody());
        assertEquals(1, listed.getBody().length);
    }
    @Test
    void shouldExecuteCheckoutFlowUntilConfirm() {
        UUID customerId = UUID.randomUUID();
        UUID reservationRef = UUID.randomUUID();
        OrderResponse created = restTemplate.postForEntity(
                baseUrl("/api/v1/orders"),
                buildCreateRequest(customerId),
                OrderResponse.class
        ).getBody();
        ResponseEntity<OrderResponse> reserved = restTemplate.postForEntity(
                baseUrl("/api/v1/orders/" + created.id() + "/reserve-stock"),
                new ReserveOrderStockRequest(List.of(reservationRef)),
                OrderResponse.class
        );
        assertEquals(HttpStatus.OK, reserved.getStatusCode());
        restTemplate.postForEntity(baseUrl("/api/v1/orders/" + created.id() + "/start-payment"), null, OrderResponse.class);
        restTemplate.postForEntity(baseUrl("/api/v1/orders/" + created.id() + "/mark-paid"), null, OrderResponse.class);
        ResponseEntity<OrderResponse> confirmed = restTemplate.postForEntity(
                baseUrl("/api/v1/orders/" + created.id() + "/confirm"),
                null,
                OrderResponse.class
        );
        assertEquals(HttpStatus.OK, confirmed.getStatusCode());
        assertNotNull(confirmed.getBody());
        assertEquals("CONFIRMED", confirmed.getBody().status().name());
    }
    @Test
    void shouldReturnConflictWhenStartingPaymentWithoutStockReservation() {
        UUID customerId = UUID.randomUUID();
        OrderResponse created = restTemplate.postForEntity(
                baseUrl("/api/v1/orders"),
                buildCreateRequest(customerId),
                OrderResponse.class
        ).getBody();
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl("/api/v1/orders/" + created.id() + "/start-payment"),
                null,
                String.class
        );
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    private CreateOrderRequest buildCreateRequest(UUID customerId) {
        return new CreateOrderRequest(customerId, List.of(new CreateOrderItemRequest(
                UUID.randomUUID(),
                "Produto X",
                "SKU X",
                Map.of("color", "black"),
                2,
                new BigDecimal("25.00"),
                new BigDecimal("5.00"),
                new BigDecimal("40.00")
        )));
    }
    private String baseUrl(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }
}
