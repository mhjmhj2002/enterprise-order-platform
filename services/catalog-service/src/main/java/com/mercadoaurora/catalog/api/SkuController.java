package com.mercadoaurora.catalog.api;

import com.mercadoaurora.catalog.api.dto.SkuResponse;
import com.mercadoaurora.catalog.api.mapper.CatalogApiMapper;
import com.mercadoaurora.catalog.application.usecase.GetSkuUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skus")
public class SkuController {

    private final GetSkuUseCase getSkuUseCase;

    public SkuController(GetSkuUseCase getSkuUseCase) {
        this.getSkuUseCase = getSkuUseCase;
    }

    @GetMapping("/{skuId}")
    public SkuResponse getSku(@PathVariable UUID skuId) {
        return CatalogApiMapper.toResponse(getSkuUseCase.execute(skuId));
    }
}
