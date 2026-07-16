package com.mercadoaurora.inventory.integration;

import com.mercadoaurora.inventory.api.dto.AdjustPhysicalStockRequest;
import com.mercadoaurora.inventory.api.dto.CreateInventoryItemRequest;
import com.mercadoaurora.inventory.api.dto.InventoryItemResponse;
import com.mercadoaurora.inventory.api.dto.ReserveStockRequest;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationProcessingResponse;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationObservationResponse;
import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;
import com.mercadoaurora.inventory.application.usecase.RecoverOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.RegisterOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.RecordOrderConfirmationTemporaryFailureUseCase;
import com.mercadoaurora.inventory.domain.OrderConfirmationProcessing;
import com.mercadoaurora.inventory.infrastructure.persistence.entity.OrderConfirmationProcessingEntity;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataOrderConfirmationEvidenceRepository;
import com.mercadoaurora.inventory.infrastructure.persistence.repository.SpringDataOrderConfirmationProcessingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Autowired
    SpringDataOrderConfirmationProcessingRepository processingRepository;

    @Autowired
    SpringDataOrderConfirmationEvidenceRepository evidenceRepository;

    @Autowired
    RegisterOrderConfirmationProcessingUseCase registerProcessing;

    @Autowired
    RecoverOrderConfirmationProcessingUseCase recoverProcessing;

    @Autowired
    RecordOrderConfirmationTemporaryFailureUseCase recordTemporaryFailure;

    @BeforeEach
    void configureHttpClientThatSupportsPatch() {
        restTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
    }

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

    @Test
    void shouldExposePendingProcessingThroughSupportedConsultation() {
        UUID orderId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        OrderConfirmationProcessingEntity pending = processingEntity(eventId, orderId);
        processingRepository.save(pending);

        ResponseEntity<OrderConfirmationProcessingResponse[]> response = restTemplate.getForEntity(
                baseUrl("/api/v1/inventory/order-confirmation-processings/" + orderId), OrderConfirmationProcessingResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertEquals(eventId, response.getBody()[0].eventId());
        assertEquals(OrderConfirmationProcessing.Status.PENDING, response.getBody()[0].status());
        assertEquals(0, response.getBody()[0].attemptCount());
        assertEquals(null, response.getBody()[0].completedAt());
    }

    @Test
    void shouldKeepOneEvidenceWhenRecoveryRunsConcurrently() throws Exception {
        UUID eventId = UUID.randomUUID();
        RecognizeOrderConfirmationCommand command = new RecognizeOrderConfirmationCommand(eventId, UUID.randomUUID(), UUID.randomUUID(),
                Instant.now(), "mercadoaurora.order.order-confirmed.v1", 0, 10L);
        registerProcessing.execute(command);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<?> first = executor.submit(() -> recoverAfterStart(start, eventId));
            Future<?> second = executor.submit(() -> recoverAfterStart(start, eventId));
            start.countDown();
            first.get();
            second.get();
        }

        assertEquals(OrderConfirmationProcessing.Status.COMPLETED, processingRepository.findById(eventId).orElseThrow().getStatus());
        assertTrue(evidenceRepository.existsById(eventId));
    }

    @Test
    void shouldExposeOperationalObservationWithTemporaryFailureAndRecovery() {
        UUID eventId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        registerProcessing.execute(new RecognizeOrderConfirmationCommand(eventId, UUID.randomUUID(), orderId,
                Instant.now(), "mercadoaurora.order.order-confirmed.v1", 0, 22L));
        recordTemporaryFailure.execute(eventId);
        recoverProcessing.execute(eventId);

        ResponseEntity<OrderConfirmationObservationResponse[]> response = restTemplate.getForEntity(
                baseUrl("/api/v1/inventory/order-confirmation-observations/" + orderId), OrderConfirmationObservationResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        OrderConfirmationObservationResponse observation = response.getBody()[0];
        assertEquals(eventId, observation.eventId());
        assertEquals(OrderConfirmationProcessing.Status.COMPLETED, observation.status());
        assertTrue(observation.uniqueFunctionalResult());
        assertEquals(List.of("REGISTERED", "TEMPORARY_FAILURE", "COMPLETED"),
                observation.lifecycle().stream().map(item -> item.milestone().name()).toList());
        assertEquals("TEMPORARY_PROCESSING_FAILURE", observation.lifecycle().get(1).failureCategory());
    }

    private void recoverAfterStart(CountDownLatch start, UUID eventId) {
        try {
            start.await();
            recoverProcessing.execute(eventId);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }

    private OrderConfirmationProcessingEntity processingEntity(UUID eventId, UUID orderId) {
        OrderConfirmationProcessingEntity entity = new OrderConfirmationProcessingEntity();
        Instant now = Instant.now();
        entity.setEventId(eventId);
        entity.setCorrelationId(UUID.randomUUID());
        entity.setOrderId(orderId);
        entity.setOccurredAt(now);
        entity.setTopic("mercadoaurora.order.order-confirmed.v1");
        entity.setPartition(0);
        entity.setOffset(10L);
        entity.setStatus(OrderConfirmationProcessing.Status.PENDING);
        entity.setAttemptCount(0);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    private String baseUrl(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }
}
