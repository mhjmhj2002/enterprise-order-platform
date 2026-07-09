package com.mercadoaurora.order.api;
import com.mercadoaurora.order.api.dto.CreateOrderRequest;
import com.mercadoaurora.order.api.dto.OrderResponse;
import com.mercadoaurora.order.api.dto.ReserveOrderStockRequest;
import com.mercadoaurora.order.api.mapper.OrderApiMapper;
import com.mercadoaurora.order.application.command.OrderActionCommand;
import com.mercadoaurora.order.application.command.ReserveOrderStockCommand;
import com.mercadoaurora.order.application.usecase.CancelOrderUseCase;
import com.mercadoaurora.order.application.usecase.ConfirmOrderUseCase;
import com.mercadoaurora.order.application.usecase.CreateOrderUseCase;
import com.mercadoaurora.order.application.usecase.GetOrderUseCase;
import com.mercadoaurora.order.application.usecase.ListOrdersByCustomerUseCase;
import com.mercadoaurora.order.application.usecase.MarkOrderPaidUseCase;
import com.mercadoaurora.order.application.usecase.ReserveOrderStockUseCase;
import com.mercadoaurora.order.application.usecase.StartPaymentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final ReserveOrderStockUseCase reserveOrderStockUseCase;
    private final StartPaymentUseCase startPaymentUseCase;
    private final MarkOrderPaidUseCase markOrderPaidUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersByCustomerUseCase listOrdersByCustomerUseCase;
    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            ReserveOrderStockUseCase reserveOrderStockUseCase,
            StartPaymentUseCase startPaymentUseCase,
            MarkOrderPaidUseCase markOrderPaidUseCase,
            ConfirmOrderUseCase confirmOrderUseCase,
            CancelOrderUseCase cancelOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            ListOrdersByCustomerUseCase listOrdersByCustomerUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.reserveOrderStockUseCase = reserveOrderStockUseCase;
        this.startPaymentUseCase = startPaymentUseCase;
        this.markOrderPaidUseCase = markOrderPaidUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersByCustomerUseCase = listOrdersByCustomerUseCase;
    }
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return OrderApiMapper.toResponse(createOrderUseCase.execute(OrderApiMapper.toCommand(request)));
    }
    @PostMapping("/orders/{orderId}/reserve-stock")
    public OrderResponse reserveOrderStock(
            @PathVariable UUID orderId,
            @Valid @RequestBody ReserveOrderStockRequest request
    ) {
        return OrderApiMapper.toResponse(
                reserveOrderStockUseCase.execute(new ReserveOrderStockCommand(orderId, request.reservationRefs()))
        );
    }
    @PostMapping("/orders/{orderId}/start-payment")
    public OrderResponse startPayment(@PathVariable UUID orderId) {
        return OrderApiMapper.toResponse(startPaymentUseCase.execute(new OrderActionCommand(orderId)));
    }
    @PostMapping("/orders/{orderId}/mark-paid")
    public OrderResponse markOrderPaid(@PathVariable UUID orderId) {
        return OrderApiMapper.toResponse(markOrderPaidUseCase.execute(new OrderActionCommand(orderId)));
    }
    @PostMapping("/orders/{orderId}/confirm")
    public OrderResponse confirmOrder(@PathVariable UUID orderId) {
        return OrderApiMapper.toResponse(confirmOrderUseCase.execute(new OrderActionCommand(orderId)));
    }
    @PostMapping("/orders/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID orderId) {
        return OrderApiMapper.toResponse(cancelOrderUseCase.execute(new OrderActionCommand(orderId)));
    }
    @GetMapping("/orders/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return OrderApiMapper.toResponse(getOrderUseCase.execute(orderId));
    }
    @GetMapping("/customers/{customerId}/orders")
    public List<OrderResponse> listByCustomer(@PathVariable UUID customerId) {
        return listOrdersByCustomerUseCase.execute(customerId).stream().map(OrderApiMapper::toResponse).toList();
    }
}
