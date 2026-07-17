package com.mercadoaurora.catalog.integration;

import com.mercadoaurora.catalog.api.dto.AddSkuRequest;
import com.mercadoaurora.catalog.api.dto.ChangeProductStatusRequest;
import com.mercadoaurora.catalog.api.dto.CreateProductRequest;
import com.mercadoaurora.catalog.api.dto.ProductResponse;
import com.mercadoaurora.catalog.api.dto.SkuResponse;
import com.mercadoaurora.catalog.domain.ProductStatus;
import com.mercadoaurora.catalog.domain.SkuStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("catalog")
            .withUsername("catalog")
            .withPassword("catalog");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("security.api.username", () -> "test-api-consumer");
        registry.add("security.api.password", () -> "test-api-password");
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void authenticateRequests() {
        restTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor("test-api-consumer", "test-api-password"));
    }

    @Test
    void shouldRejectUnauthenticatedBusinessRequestAndKeepTechnicalEndpointsPublic() {
        TestRestTemplate anonymous = new TestRestTemplate();

        ResponseEntity<String> response = anonymous.postForEntity(
                baseUrl("/api/v1/products"),
                new CreateProductRequest("Produto bloqueado", "Descricao", ProductStatus.INACTIVE,
                        UUID.randomUUID(), UUID.randomUUID(), Map.of(), List.of()), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getHeaders().getFirst("WWW-Authenticate"));
        assertEquals(HttpStatus.OK, anonymous.getForEntity(baseUrl("/actuator/health"), String.class).getStatusCode());
        assertEquals(HttpStatus.OK, anonymous.getForEntity(baseUrl("/v3/api-docs"), String.class).getStatusCode());
    }

    @Test
    void shouldCreateAndGetProduct() {
        ResponseEntity<ProductResponse> created = restTemplate.postForEntity(
                baseUrl("/api/v1/products"),
                new CreateProductRequest(
                        "Produto 1",
                        "Descricao",
                        ProductStatus.INACTIVE,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Map.of("material", "cotton"),
                        List.of("https://image")
                ),
                ProductResponse.class
        );

        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        assertNotNull(created.getBody().id());

        ResponseEntity<ProductResponse> loaded = restTemplate.getForEntity(
                baseUrl("/api/v1/products/" + created.getBody().id()),
                ProductResponse.class
        );
        assertEquals(HttpStatus.OK, loaded.getStatusCode());
        assertEquals(created.getBody().id(), loaded.getBody().id());
    }

    @Test
    void shouldReturnBadRequestWhenActivatingProductWithoutActiveSku() {
        ProductResponse product = restTemplate.postForEntity(
                baseUrl("/api/v1/products"),
                new CreateProductRequest(
                        "Produto 2",
                        "Descricao",
                        ProductStatus.INACTIVE,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Map.of(),
                        List.of()
                ),
                ProductResponse.class
        ).getBody();

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl("/api/v1/products/" + product.id() + "/status"),
                org.springframework.http.HttpMethod.PATCH,
                new HttpEntity<>(new ChangeProductStatusRequest(ProductStatus.ACTIVE)),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldAddAndGetSku() {
        ProductResponse product = restTemplate.postForEntity(
                baseUrl("/api/v1/products"),
                new CreateProductRequest(
                        "Produto 3",
                        "Descricao",
                        ProductStatus.INACTIVE,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Map.of(),
                        List.of()
                ),
                ProductResponse.class
        ).getBody();

        ResponseEntity<SkuResponse> skuCreated = restTemplate.postForEntity(
                baseUrl("/api/v1/products/" + product.id() + "/skus"),
                new AddSkuRequest("SELLER-I-001", "EAN-I-001", Map.of("size", "M"), SkuStatus.ACTIVE),
                SkuResponse.class
        );
        assertEquals(HttpStatus.CREATED, skuCreated.getStatusCode());
        assertNotNull(skuCreated.getBody());

        ResponseEntity<SkuResponse> skuLoaded = restTemplate.getForEntity(
                baseUrl("/api/v1/skus/" + skuCreated.getBody().id()),
                SkuResponse.class
        );
        assertEquals(HttpStatus.OK, skuLoaded.getStatusCode());
        assertEquals(skuCreated.getBody().id(), skuLoaded.getBody().id());
    }

    @Test
    void shouldReturnConflictWhenDuplicatedSellerCode() {
        ProductResponse product = restTemplate.postForEntity(
                baseUrl("/api/v1/products"),
                new CreateProductRequest(
                        "Produto 4",
                        "Descricao",
                        ProductStatus.INACTIVE,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Map.of(),
                        List.of()
                ),
                ProductResponse.class
        ).getBody();

        restTemplate.postForEntity(
                baseUrl("/api/v1/products/" + product.id() + "/skus"),
                new AddSkuRequest("SELLER-I-002", null, Map.of(), SkuStatus.ACTIVE),
                SkuResponse.class
        );

        ResponseEntity<String> conflict = restTemplate.postForEntity(
                baseUrl("/api/v1/products/" + product.id() + "/skus"),
                new AddSkuRequest("SELLER-I-002", null, Map.of(), SkuStatus.ACTIVE),
                String.class
        );

        assertEquals(HttpStatus.CONFLICT, conflict.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForUnknownProduct() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl("/api/v1/products/" + UUID.randomUUID()),
                String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private String baseUrl(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }
}
