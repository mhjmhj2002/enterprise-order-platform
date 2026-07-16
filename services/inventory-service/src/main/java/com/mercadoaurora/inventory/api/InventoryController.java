package com.mercadoaurora.inventory.api;

import com.mercadoaurora.inventory.api.dto.AdjustPhysicalStockRequest;
import com.mercadoaurora.inventory.api.dto.CreateInventoryItemRequest;
import com.mercadoaurora.inventory.api.dto.InventoryItemResponse;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationEvidenceResponse;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationProcessingResponse;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationObservationResponse;
import com.mercadoaurora.inventory.api.dto.OrderConfirmationLifecycleResponse;
import com.mercadoaurora.inventory.api.dto.ReserveStockRequest;
import com.mercadoaurora.inventory.api.mapper.InventoryApiMapper;
import com.mercadoaurora.inventory.application.command.AdjustPhysicalStockCommand;
import com.mercadoaurora.inventory.application.command.CommitReservationCommand;
import com.mercadoaurora.inventory.application.command.CreateInventoryItemCommand;
import com.mercadoaurora.inventory.application.command.ReleaseReservationCommand;
import com.mercadoaurora.inventory.application.command.ReserveStockCommand;
import com.mercadoaurora.inventory.application.usecase.AdjustPhysicalStockUseCase;
import com.mercadoaurora.inventory.application.usecase.CommitReservationUseCase;
import com.mercadoaurora.inventory.application.usecase.CreateInventoryItemUseCase;
import com.mercadoaurora.inventory.application.usecase.GetInventoryBySkuAndWarehouseUseCase;
import com.mercadoaurora.inventory.application.usecase.GetInventoryBySkuUseCase;
import com.mercadoaurora.inventory.application.usecase.GetOrderConfirmationEvidenceUseCase;
import com.mercadoaurora.inventory.application.usecase.GetOrderConfirmationProcessingUseCase;
import com.mercadoaurora.inventory.application.usecase.GetOrderConfirmationObservationUseCase;
import com.mercadoaurora.inventory.application.usecase.ReleaseReservationUseCase;
import com.mercadoaurora.inventory.application.usecase.ReserveStockUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final CreateInventoryItemUseCase createInventoryItemUseCase;
    private final AdjustPhysicalStockUseCase adjustPhysicalStockUseCase;
    private final ReserveStockUseCase reserveStockUseCase;
    private final ReleaseReservationUseCase releaseReservationUseCase;
    private final CommitReservationUseCase commitReservationUseCase;
    private final GetInventoryBySkuUseCase getInventoryBySkuUseCase;
    private final GetInventoryBySkuAndWarehouseUseCase getInventoryBySkuAndWarehouseUseCase;
    private final GetOrderConfirmationEvidenceUseCase getOrderConfirmationEvidenceUseCase;
    private final GetOrderConfirmationProcessingUseCase getOrderConfirmationProcessingUseCase;
    private final GetOrderConfirmationObservationUseCase getOrderConfirmationObservationUseCase;

    public InventoryController(
            CreateInventoryItemUseCase createInventoryItemUseCase,
            AdjustPhysicalStockUseCase adjustPhysicalStockUseCase,
            ReserveStockUseCase reserveStockUseCase,
            ReleaseReservationUseCase releaseReservationUseCase,
            CommitReservationUseCase commitReservationUseCase,
            GetInventoryBySkuUseCase getInventoryBySkuUseCase,
            GetInventoryBySkuAndWarehouseUseCase getInventoryBySkuAndWarehouseUseCase,
            GetOrderConfirmationEvidenceUseCase getOrderConfirmationEvidenceUseCase,
            GetOrderConfirmationProcessingUseCase getOrderConfirmationProcessingUseCase,
            GetOrderConfirmationObservationUseCase getOrderConfirmationObservationUseCase
    ) {
        this.createInventoryItemUseCase = createInventoryItemUseCase;
        this.adjustPhysicalStockUseCase = adjustPhysicalStockUseCase;
        this.reserveStockUseCase = reserveStockUseCase;
        this.releaseReservationUseCase = releaseReservationUseCase;
        this.commitReservationUseCase = commitReservationUseCase;
        this.getInventoryBySkuUseCase = getInventoryBySkuUseCase;
        this.getInventoryBySkuAndWarehouseUseCase = getInventoryBySkuAndWarehouseUseCase;
        this.getOrderConfirmationEvidenceUseCase = getOrderConfirmationEvidenceUseCase;
        this.getOrderConfirmationProcessingUseCase = getOrderConfirmationProcessingUseCase;
        this.getOrderConfirmationObservationUseCase = getOrderConfirmationObservationUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse createInventoryItem(@Valid @RequestBody CreateInventoryItemRequest request) {
        return InventoryApiMapper.toResponse(createInventoryItemUseCase.execute(
                new CreateInventoryItemCommand(request.skuId(), request.warehouseId(), request.physicalQuantity())
        ));
    }

    @PatchMapping("/{skuId}/{warehouseId}/adjust")
    public InventoryItemResponse adjustPhysicalStock(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId,
            @Valid @RequestBody AdjustPhysicalStockRequest request
    ) {
        return InventoryApiMapper.toResponse(adjustPhysicalStockUseCase.execute(
                new AdjustPhysicalStockCommand(skuId, warehouseId, request.quantityDelta())
        ));
    }

    @PostMapping("/{skuId}/{warehouseId}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse reserveStock(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId,
            @Valid @RequestBody ReserveStockRequest request
    ) {
        return InventoryApiMapper.toResponse(reserveStockUseCase.execute(
                new ReserveStockCommand(skuId, warehouseId, request.reservationId(), request.quantity())
        ));
    }

    @PostMapping("/{skuId}/{warehouseId}/reservations/{reservationId}/commit")
    public InventoryItemResponse commitReservation(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId,
            @PathVariable UUID reservationId
    ) {
        return InventoryApiMapper.toResponse(commitReservationUseCase.execute(
                new CommitReservationCommand(skuId, warehouseId, reservationId)
        ));
    }

    @PostMapping("/{skuId}/{warehouseId}/reservations/{reservationId}/release")
    public InventoryItemResponse releaseReservation(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId,
            @PathVariable UUID reservationId
    ) {
        return InventoryApiMapper.toResponse(releaseReservationUseCase.execute(
                new ReleaseReservationCommand(skuId, warehouseId, reservationId)
        ));
    }

    @GetMapping("/{skuId}")
    public List<InventoryItemResponse> getInventoryBySku(@PathVariable UUID skuId) {
        return getInventoryBySkuUseCase.execute(skuId).stream()
                .map(InventoryApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/order-confirmations/{orderId}")
    public List<OrderConfirmationEvidenceResponse> getOrderConfirmations(@PathVariable UUID orderId) {
        return getOrderConfirmationEvidenceUseCase.execute(orderId).stream()
                .map(evidence -> new OrderConfirmationEvidenceResponse(
                        evidence.eventId(), evidence.correlationId(), evidence.orderId(), evidence.occurredAt(),
                        evidence.recognizedAt(), evidence.topic(), evidence.partition(), evidence.offset()))
                .toList();
    }

    @GetMapping("/order-confirmation-processings/{orderId}")
    public List<OrderConfirmationProcessingResponse> getOrderConfirmationProcessings(@PathVariable UUID orderId) {
        return getOrderConfirmationProcessingUseCase.execute(orderId).stream()
                .map(processing -> new OrderConfirmationProcessingResponse(
                        processing.eventId(), processing.correlationId(), processing.orderId(), processing.occurredAt(),
                        processing.topic(), processing.partition(), processing.offset(), processing.status(), processing.attemptCount(),
                        processing.createdAt(), processing.updatedAt(), processing.completedAt()))
                .toList();
    }

    @GetMapping("/order-confirmation-observations/{orderId}")
    public List<OrderConfirmationObservationResponse> getOrderConfirmationObservations(@PathVariable UUID orderId) {
        return getOrderConfirmationObservationUseCase.execute(orderId).stream().map(observation -> {
            var processing = observation.processing();
            var evidence = observation.evidence();
            return new OrderConfirmationObservationResponse(processing.orderId(), processing.eventId(), processing.correlationId(),
                    "OrderConfirmed", 1, processing.occurredAt(), processing.topic(), processing.partition(), processing.offset(),
                    processing.status(), processing.attemptCount(), processing.createdAt(), processing.updatedAt(), processing.completedAt(),
                    evidence == null ? null : evidence.recognizedAt(), evidence != null,
                    observation.lifecycle().stream().map(item -> new OrderConfirmationLifecycleResponse(
                            item.milestone(), item.occurredAt(), item.failureCategory())).toList());
        }).toList();
    }

    @GetMapping("/{skuId}/{warehouseId}")
    public InventoryItemResponse getInventoryBySkuAndWarehouse(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId
    ) {
        return InventoryApiMapper.toResponse(getInventoryBySkuAndWarehouseUseCase.execute(skuId, warehouseId));
    }
}
