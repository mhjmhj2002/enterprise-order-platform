package com.mercadoaurora.order.domain;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class OrderDomainTest {
    @Test
    void shouldNotCreateOrderWithoutItems() {
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(), Instant.now()));
        assertEquals("Order must have at least one item", exception.getMessage());
    }
    @Test
    void shouldNotStartPaymentBeforeStockReservation() {
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(buildItem(2)), Instant.now());
        DomainConflictException exception = assertThrows(DomainConflictException.class,
                () -> order.startPayment(Instant.now()));
        assertEquals("Cannot start payment without reserved stock", exception.getMessage());
    }
    @Test
    void shouldNotConfirmWithoutPaidPayment() {
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(buildItem(1)), Instant.now());
        order.reserveStock(List.of(UUID.randomUUID()), Instant.now());
        DomainConflictException exception = assertThrows(DomainConflictException.class,
                () -> order.confirm(Instant.now()));
        assertEquals("Cannot confirm order before payment", exception.getMessage());
    }
    @Test
    void shouldNotCancelConfirmedOrderInThisSprint() {
        Instant now = Instant.parse("2026-07-09T10:00:00Z");
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(buildItem(2)), now);
        order.reserveStock(List.of(UUID.randomUUID()), now.plusSeconds(60));
        order.startPayment(now.plusSeconds(120));
        order.markPaid(now.plusSeconds(180));
        order.confirm(now.plusSeconds(240));
        DomainConflictException exception = assertThrows(DomainConflictException.class,
                () -> order.cancel(now.plusSeconds(300)));
        assertEquals("Confirmed order cannot be cancelled in this sprint", exception.getMessage());
    }
    @Test
    void shouldKeepConsistentTotal() {
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(buildItem(3), buildItem(1)), Instant.now());
        assertEquals(0, new BigDecimal("40.00").compareTo(order.getTotalAmount()));
    }
    private OrderItem buildItem(int quantity) {
        BigDecimal unitPrice = new BigDecimal("10.00");
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return OrderItem.create(
                UUID.randomUUID(),
                "Camiseta",
                "Camiseta Preta M",
                java.util.Map.of("color", "black"),
                quantity,
                unitPrice,
                discount,
                lineTotal
        );
    }
}
