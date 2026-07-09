package com.mercadoaurora.order.unit;
import com.mercadoaurora.order.application.command.CreateOrderCommand;
import com.mercadoaurora.order.application.command.CreateOrderItemInput;
import com.mercadoaurora.order.application.command.OrderActionCommand;
import com.mercadoaurora.order.application.command.ReserveOrderStockCommand;
import com.mercadoaurora.order.application.exception.OrderConflictException;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.application.usecase.CancelOrderUseCase;
import com.mercadoaurora.order.application.usecase.ConfirmOrderUseCase;
import com.mercadoaurora.order.application.usecase.CreateOrderUseCase;
import com.mercadoaurora.order.application.usecase.GetOrderUseCase;
import com.mercadoaurora.order.application.usecase.ListOrdersByCustomerUseCase;
import com.mercadoaurora.order.application.usecase.MarkOrderPaidUseCase;
import com.mercadoaurora.order.application.usecase.ReserveOrderStockUseCase;
import com.mercadoaurora.order.application.usecase.StartPaymentUseCase;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class OrderUseCasesTest {
    @Mock
    private OrderRepositoryPort repositoryPort;
    private Clock clock;
    @BeforeEach
    void setupClock() {
        clock = Clock.fixed(Instant.parse("2026-07-09T10:00:00Z"), ZoneOffset.UTC);
    }
    @Test
    void shouldCreateOrder() {
        CreateOrderUseCase useCase = new CreateOrderUseCase(repositoryPort, clock);
        UUID customerId = UUID.randomUUID();
        when(repositoryPort.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Order order = useCase.execute(new CreateOrderCommand(customerId, List.of(buildItemInput(2))));
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        verify(repositoryPort).save(any(Order.class));
    }
    @Test
    void shouldExecuteCompleteHappyPathUntilConfirm() {
        Instant now = Instant.now(clock);
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(
                com.mercadoaurora.order.domain.OrderItem.create(
                        UUID.randomUUID(), "Produto", "SKU", Map.of(), 1,
                        new BigDecimal("20.00"), BigDecimal.ZERO, new BigDecimal("20.00")
                )
        ), now);
        when(repositoryPort.findById(order.getId())).thenReturn(Optional.of(order));
        when(repositoryPort.save(order)).thenReturn(order);
        ReserveOrderStockUseCase reserve = new ReserveOrderStockUseCase(repositoryPort, clock);
        StartPaymentUseCase startPayment = new StartPaymentUseCase(repositoryPort, clock);
        MarkOrderPaidUseCase markPaid = new MarkOrderPaidUseCase(repositoryPort, clock);
        ConfirmOrderUseCase confirm = new ConfirmOrderUseCase(repositoryPort, clock);
        reserve.execute(new ReserveOrderStockCommand(order.getId(), List.of(UUID.randomUUID())));
        startPayment.execute(new OrderActionCommand(order.getId()));
        markPaid.execute(new OrderActionCommand(order.getId()));
        Order confirmed = confirm.execute(new OrderActionCommand(order.getId()));
        assertEquals(OrderStatus.CONFIRMED, confirmed.getStatus());
    }
    @Test
    void shouldThrowNotFoundWhenOrderDoesNotExist() {
        UUID orderId = UUID.randomUUID();
        GetOrderUseCase useCase = new GetOrderUseCase(repositoryPort);
        when(repositoryPort.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> useCase.execute(orderId));
    }
    @Test
    void shouldThrowConflictWhenConfirmWithoutPayment() {
        Instant now = Instant.now(clock);
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(
                com.mercadoaurora.order.domain.OrderItem.create(
                        UUID.randomUUID(), "Produto", "SKU", Map.of(), 1,
                        new BigDecimal("20.00"), BigDecimal.ZERO, new BigDecimal("20.00")
                )
        ), now);
        when(repositoryPort.findById(order.getId())).thenReturn(Optional.of(order));
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(repositoryPort, clock);
        assertThrows(OrderConflictException.class, () -> useCase.execute(new OrderActionCommand(order.getId())));
    }
    @Test
    void shouldListOrdersByCustomer() {
        Instant now = Instant.now(clock);
        UUID customerId = UUID.randomUUID();
        Order order = Order.create(UUID.randomUUID(), customerId, List.of(
                com.mercadoaurora.order.domain.OrderItem.create(
                        UUID.randomUUID(), "Produto", "SKU", Map.of(), 1,
                        new BigDecimal("20.00"), BigDecimal.ZERO, new BigDecimal("20.00")
                )
        ), now);
        when(repositoryPort.findByCustomerId(customerId)).thenReturn(List.of(order));
        ListOrdersByCustomerUseCase useCase = new ListOrdersByCustomerUseCase(repositoryPort);
        List<Order> result = useCase.execute(customerId);
        assertEquals(1, result.size());
    }
    @Test
    void shouldCancelBeforeConfirmation() {
        Instant now = Instant.now(clock);
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(
                com.mercadoaurora.order.domain.OrderItem.create(
                        UUID.randomUUID(), "Produto", "SKU", Map.of(), 1,
                        new BigDecimal("20.00"), BigDecimal.ZERO, new BigDecimal("20.00")
                )
        ), now);
        when(repositoryPort.findById(order.getId())).thenReturn(Optional.of(order));
        when(repositoryPort.save(order)).thenReturn(order);
        CancelOrderUseCase useCase = new CancelOrderUseCase(repositoryPort, clock);
        Order cancelled = useCase.execute(new OrderActionCommand(order.getId()));
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
    }
    private CreateOrderItemInput buildItemInput(int quantity) {
        return new CreateOrderItemInput(
                UUID.randomUUID(),
                "Produto X",
                "SKU X",
                Map.of("size", "M"),
                quantity,
                new BigDecimal("10.00"),
                BigDecimal.ZERO,
                new BigDecimal("10.00").multiply(BigDecimal.valueOf(quantity))
        );
    }
}
