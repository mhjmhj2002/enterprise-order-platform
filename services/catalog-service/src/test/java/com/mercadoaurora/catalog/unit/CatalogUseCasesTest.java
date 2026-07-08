package com.mercadoaurora.catalog.unit;

import com.mercadoaurora.catalog.application.command.AddSkuCommand;
import com.mercadoaurora.catalog.application.command.ChangeProductStatusCommand;
import com.mercadoaurora.catalog.application.command.CreateProductCommand;
import com.mercadoaurora.catalog.application.command.UpdateProductCommand;
import com.mercadoaurora.catalog.application.command.UpdateSkuCommand;
import com.mercadoaurora.catalog.application.exception.CatalogConflictException;
import com.mercadoaurora.catalog.application.exception.ProductNotFoundException;
import com.mercadoaurora.catalog.application.exception.SkuNotFoundException;
import com.mercadoaurora.catalog.application.port.out.CatalogRepositoryPort;
import com.mercadoaurora.catalog.application.usecase.AddSkuUseCase;
import com.mercadoaurora.catalog.application.usecase.ChangeProductStatusUseCase;
import com.mercadoaurora.catalog.application.usecase.CreateProductUseCase;
import com.mercadoaurora.catalog.application.usecase.GetProductUseCase;
import com.mercadoaurora.catalog.application.usecase.GetSkuUseCase;
import com.mercadoaurora.catalog.application.usecase.ListActiveProductsUseCase;
import com.mercadoaurora.catalog.application.usecase.UpdateProductUseCase;
import com.mercadoaurora.catalog.application.usecase.UpdateSkuUseCase;
import com.mercadoaurora.catalog.domain.Product;
import com.mercadoaurora.catalog.domain.ProductStatus;
import com.mercadoaurora.catalog.domain.Sku;
import com.mercadoaurora.catalog.domain.SkuStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogUseCasesTest {

    @Mock
    private CatalogRepositoryPort repositoryPort;

    private Clock clock;

    @BeforeEach
    void setupClock() {
        clock = Clock.fixed(Instant.parse("2026-07-08T10:00:00Z"), ZoneOffset.UTC);
    }

    @Test
    void shouldCreateProduct() {
        CreateProductUseCase useCase = new CreateProductUseCase(repositoryPort, clock);
        CreateProductCommand command = new CreateProductCommand(
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of("x", "y"),
                List.of("img")
        );

        when(repositoryPort.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Product product = useCase.execute(command);

        assertEquals("Produto", product.getName());
        verify(repositoryPort).save(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() {
        Product existing = newProductWithActiveSku();
        UpdateProductUseCase useCase = new UpdateProductUseCase(repositoryPort, clock);
        UpdateProductCommand command = new UpdateProductCommand(
                existing.getId(),
                "Produto Atualizado",
                "Nova descricao",
                existing.getBrandId(),
                existing.getCategoryId(),
                Map.of("size", "M"),
                List.of("img")
        );

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        when(repositoryPort.save(existing)).thenReturn(existing);

        Product updated = useCase.execute(command);
        assertEquals("Produto Atualizado", updated.getName());
        verify(repositoryPort).save(existing);
    }

    @Test
    void shouldChangeProductStatus() {
        Product existing = newProductWithActiveSku();
        ChangeProductStatusUseCase useCase = new ChangeProductStatusUseCase(repositoryPort, clock);

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        when(repositoryPort.save(existing)).thenReturn(existing);

        Product updated = useCase.execute(new ChangeProductStatusCommand(existing.getId(), ProductStatus.ACTIVE));
        assertEquals(ProductStatus.ACTIVE, updated.getStatus());
    }

    @Test
    void shouldAddSku() {
        Product existing = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                List.of(),
                Instant.now(clock)
        );
        AddSkuUseCase useCase = new AddSkuUseCase(repositoryPort, clock);

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        when(repositoryPort.existsSellerCode(eq("SELLER-002"), eq(null))).thenReturn(false);
        when(repositoryPort.existsEan(eq("789002"), eq(null))).thenReturn(false);
        when(repositoryPort.save(existing)).thenAnswer(invocation -> invocation.getArgument(0));

        Sku sku = useCase.execute(new AddSkuCommand(existing.getId(), "SELLER-002", "789002", Map.of(), SkuStatus.ACTIVE));
        assertEquals("SELLER-002", sku.getSellerCode());
    }

    @Test
    void shouldUpdateSku() {
        Product existing = newProductWithActiveSku();
        UUID skuId = existing.getSkus().get(0).getId();
        UpdateSkuUseCase useCase = new UpdateSkuUseCase(repositoryPort, clock);

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        when(repositoryPort.existsSellerCode(eq("SELLER-001-NEW"), eq(skuId))).thenReturn(false);
        when(repositoryPort.existsEan(eq("999001"), eq(skuId))).thenReturn(false);
        when(repositoryPort.save(existing)).thenReturn(existing);

        Sku sku = useCase.execute(new UpdateSkuCommand(
                existing.getId(),
                skuId,
                "SELLER-001-NEW",
                "999001",
                Map.of("color", "black"),
                SkuStatus.ACTIVE
        ));

        assertEquals("SELLER-001-NEW", sku.getSellerCode());
    }

    @Test
    void shouldGetProduct() {
        Product existing = newProductWithActiveSku();
        GetProductUseCase useCase = new GetProductUseCase(repositoryPort);

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        Product product = useCase.execute(existing.getId());

        assertEquals(existing.getId(), product.getId());
    }

    @Test
    void shouldGetSku() {
        Product existing = newProductWithActiveSku();
        Sku sku = existing.getSkus().get(0);
        GetSkuUseCase useCase = new GetSkuUseCase(repositoryPort);

        when(repositoryPort.findSkuById(sku.getId())).thenReturn(Optional.of(sku));
        Sku result = useCase.execute(sku.getId());

        assertEquals(sku.getId(), result.getId());
    }

    @Test
    void shouldListActiveProducts() {
        Product product = newProductWithActiveSku();
        ListActiveProductsUseCase useCase = new ListActiveProductsUseCase(repositoryPort);

        when(repositoryPort.findActiveProducts(0, 20)).thenReturn(List.of(product));

        List<Product> products = useCase.execute(0, 20);
        assertEquals(1, products.size());
    }

    @Test
    void shouldFailWhenSellerCodeIsDuplicatedInCatalog() {
        Product existing = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                List.of(),
                Instant.now(clock)
        );
        AddSkuUseCase useCase = new AddSkuUseCase(repositoryPort, clock);

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));
        when(repositoryPort.existsSellerCode(eq("SELLER-001"), eq(null))).thenReturn(true);

        assertThrows(CatalogConflictException.class,
                () -> useCase.execute(new AddSkuCommand(existing.getId(), "SELLER-001", null, Map.of(), SkuStatus.ACTIVE)));
    }

    @Test
    void shouldFailWhenProductDoesNotExist() {
        UpdateProductUseCase useCase = new UpdateProductUseCase(repositoryPort, clock);
        UUID productId = UUID.randomUUID();
        when(repositoryPort.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(new UpdateProductCommand(
                productId,
                "Produto",
                "Descricao",
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                List.of()
        )));
    }

    @Test
    void shouldFailWhenSkuDoesNotExistOnUpdate() {
        Product existing = newProductWithActiveSku();
        UpdateSkuUseCase useCase = new UpdateSkuUseCase(repositoryPort, clock);
        UUID nonexistentSku = UUID.randomUUID();

        when(repositoryPort.findProductById(existing.getId())).thenReturn(Optional.of(existing));

        assertThrows(SkuNotFoundException.class, () -> useCase.execute(new UpdateSkuCommand(
                existing.getId(),
                nonexistentSku,
                "SELLER-NEW",
                "789123",
                Map.of(),
                SkuStatus.ACTIVE
        )));
    }

    private Product newProductWithActiveSku() {
        Instant now = Instant.now(clock);
        Product product = Product.create(
                UUID.randomUUID(),
                "Produto",
                "Descricao",
                ProductStatus.INACTIVE,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                List.of(),
                now
        );

        product.addSku(
                Sku.create(
                        UUID.randomUUID(),
                        "SELLER-001",
                        "789001",
                        Map.of("color", "white"),
                        SkuStatus.ACTIVE,
                        now
                ),
                now
        );
        return product;
    }
}
