package com.mercadoaurora.catalog.api;

import com.mercadoaurora.catalog.api.dto.AddSkuRequest;
import com.mercadoaurora.catalog.api.dto.ChangeProductStatusRequest;
import com.mercadoaurora.catalog.api.dto.CreateProductRequest;
import com.mercadoaurora.catalog.api.dto.ProductResponse;
import com.mercadoaurora.catalog.api.dto.SkuResponse;
import com.mercadoaurora.catalog.api.dto.UpdateProductRequest;
import com.mercadoaurora.catalog.api.dto.UpdateSkuRequest;
import com.mercadoaurora.catalog.api.mapper.CatalogApiMapper;
import com.mercadoaurora.catalog.application.command.AddSkuCommand;
import com.mercadoaurora.catalog.application.command.ChangeProductStatusCommand;
import com.mercadoaurora.catalog.application.command.CreateProductCommand;
import com.mercadoaurora.catalog.application.command.UpdateProductCommand;
import com.mercadoaurora.catalog.application.command.UpdateSkuCommand;
import com.mercadoaurora.catalog.application.usecase.AddSkuUseCase;
import com.mercadoaurora.catalog.application.usecase.ChangeProductStatusUseCase;
import com.mercadoaurora.catalog.application.usecase.CreateProductUseCase;
import com.mercadoaurora.catalog.application.usecase.GetProductUseCase;
import com.mercadoaurora.catalog.application.usecase.ListActiveProductsUseCase;
import com.mercadoaurora.catalog.application.usecase.UpdateProductUseCase;
import com.mercadoaurora.catalog.application.usecase.UpdateSkuUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ChangeProductStatusUseCase changeProductStatusUseCase;
    private final AddSkuUseCase addSkuUseCase;
    private final UpdateSkuUseCase updateSkuUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListActiveProductsUseCase listActiveProductsUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            ChangeProductStatusUseCase changeProductStatusUseCase,
            AddSkuUseCase addSkuUseCase,
            UpdateSkuUseCase updateSkuUseCase,
            GetProductUseCase getProductUseCase,
            ListActiveProductsUseCase listActiveProductsUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.changeProductStatusUseCase = changeProductStatusUseCase;
        this.addSkuUseCase = addSkuUseCase;
        this.updateSkuUseCase = updateSkuUseCase;
        this.getProductUseCase = getProductUseCase;
        this.listActiveProductsUseCase = listActiveProductsUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return CatalogApiMapper.toResponse(createProductUseCase.execute(
                new CreateProductCommand(
                        request.name(),
                        request.description(),
                        request.status(),
                        request.brandId(),
                        request.categoryId(),
                        request.specifications(),
                        request.images()
                )
        ));
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return CatalogApiMapper.toResponse(updateProductUseCase.execute(
                new UpdateProductCommand(
                        productId,
                        request.name(),
                        request.description(),
                        request.brandId(),
                        request.categoryId(),
                        request.specifications(),
                        request.images()
                )
        ));
    }

    @PatchMapping("/{productId}/status")
    public ProductResponse changeProductStatus(
            @PathVariable UUID productId,
            @Valid @RequestBody ChangeProductStatusRequest request
    ) {
        return CatalogApiMapper.toResponse(changeProductStatusUseCase.execute(
                new ChangeProductStatusCommand(productId, request.status())
        ));
    }

    @GetMapping
    public List<ProductResponse> listActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return listActiveProductsUseCase.execute(page, size).stream()
                .map(CatalogApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable UUID productId) {
        return CatalogApiMapper.toResponse(getProductUseCase.execute(productId));
    }

    @GetMapping("/{productId}/skus")
    public List<SkuResponse> listProductSkus(@PathVariable UUID productId) {
        return getProductUseCase.execute(productId).getSkus().stream()
                .map(CatalogApiMapper::toResponse)
                .toList();
    }

    @PostMapping("/{productId}/skus")
    @ResponseStatus(HttpStatus.CREATED)
    public SkuResponse addSku(@PathVariable UUID productId, @Valid @RequestBody AddSkuRequest request) {
        return CatalogApiMapper.toResponse(addSkuUseCase.execute(
                new AddSkuCommand(productId, request.sellerCode(), request.ean(), request.attributes(), request.status())
        ));
    }

    @PutMapping("/{productId}/skus/{skuId}")
    public SkuResponse updateSku(
            @PathVariable UUID productId,
            @PathVariable UUID skuId,
            @Valid @RequestBody UpdateSkuRequest request
    ) {
        return CatalogApiMapper.toResponse(updateSkuUseCase.execute(
                new UpdateSkuCommand(
                        productId,
                        skuId,
                        request.sellerCode(),
                        request.ean(),
                        request.attributes(),
                        request.status()
                )
        ));
    }
}
