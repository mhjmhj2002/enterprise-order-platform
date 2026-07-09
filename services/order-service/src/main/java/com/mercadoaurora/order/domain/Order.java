package com.mercadoaurora.order.domain;
import com.mercadoaurora.order.domain.event.OrderCancelledEvent;
import com.mercadoaurora.order.domain.event.OrderConfirmedEvent;
import com.mercadoaurora.order.domain.event.OrderCreatedEvent;
import com.mercadoaurora.order.domain.event.OrderEvent;
import com.mercadoaurora.order.domain.event.PaymentApprovedEvent;
import com.mercadoaurora.order.domain.event.PaymentStartedEvent;
import com.mercadoaurora.order.domain.event.StockReservedEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
public class Order {
    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private final List<UUID> reservationRefs;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant confirmedAt;
    private Instant cancelledAt;
    private final List<OrderEvent> domainEvents;
    private Order(
            UUID id,
            UUID customerId,
            List<OrderItem> items,
            List<UUID> reservationRefs,
            OrderStatus status,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            Instant createdAt,
            Instant updatedAt,
            Instant confirmedAt,
            Instant cancelledAt,
            List<OrderEvent> domainEvents
    ) {
        this.id = requireId(id, "id");
        this.customerId = requireId(customerId, "customerId");
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.reservationRefs = reservationRefs == null ? new ArrayList<>() : new ArrayList<>(reservationRefs);
        this.status = status == null ? OrderStatus.CREATED : status;
        this.paymentStatus = paymentStatus == null ? PaymentStatus.NOT_STARTED : paymentStatus;
        this.totalAmount = requireMoney(totalAmount, "totalAmount");
        this.createdAt = requireInstant(createdAt, "createdAt");
        this.updatedAt = requireInstant(updatedAt, "updatedAt");
        this.confirmedAt = confirmedAt;
        this.cancelledAt = cancelledAt;
        this.domainEvents = domainEvents == null ? new ArrayList<>() : new ArrayList<>(domainEvents);
        validateInvariants();
    }
    public static Order create(UUID orderId, UUID customerId, List<OrderItem> items, Instant now) {
        Instant timestamp = requireInstant(now, "now");
        List<OrderItem> itemSnapshot = List.copyOf(requireItems(items));
        BigDecimal totals = calculateTotal(itemSnapshot);
        Order order = new Order(
                orderId,
                customerId,
                itemSnapshot,
                List.of(),
                OrderStatus.CREATED,
                PaymentStatus.NOT_STARTED,
                totals,
                timestamp,
                timestamp,
                null,
                null,
                new ArrayList<>()
        );
        order.registerEvent(new OrderCreatedEvent(order.id, order.customerId, timestamp));
        return order;
    }
    public static Order restore(
            UUID id,
            UUID customerId,
            List<OrderItem> items,
            List<UUID> reservationRefs,
            OrderStatus status,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            Instant createdAt,
            Instant updatedAt,
            Instant confirmedAt,
            Instant cancelledAt
    ) {
        return new Order(
                id,
                customerId,
                items,
                reservationRefs,
                status,
                paymentStatus,
                totalAmount,
                createdAt,
                updatedAt,
                confirmedAt,
                cancelledAt,
                new ArrayList<>()
        );
    }
    public void reserveStock(List<UUID> reservationReferences, Instant now) {
        ensureNotCancelled();
        ensureNotConfirmed();
        if (status != OrderStatus.CREATED) {
            throw new DomainConflictException("Order must be CREATED to reserve stock");
        }
        if (reservationReferences == null || reservationReferences.isEmpty()) {
            throw new DomainValidationException("reservationRefs must have at least one reference");
        }
        if (reservationReferences.size() != items.size()) {
            throw new DomainValidationException("reservationRefs must match order items");
        }
        this.reservationRefs.clear();
        this.reservationRefs.addAll(reservationReferences);
        this.status = OrderStatus.STOCK_RESERVED;
        this.updatedAt = requireInstant(now, "now");
        registerEvent(new StockReservedEvent(id, List.copyOf(this.reservationRefs), this.updatedAt));
    }
    public void startPayment(Instant now) {
        ensureNotCancelled();
        ensureNotConfirmed();
        if (status != OrderStatus.STOCK_RESERVED) {
            throw new DomainConflictException("Cannot start payment without reserved stock");
        }
        if (paymentStatus != PaymentStatus.NOT_STARTED && paymentStatus != PaymentStatus.FAILED) {
            throw new DomainConflictException("Payment flow already started");
        }
        this.paymentStatus = PaymentStatus.PENDING;
        this.status = OrderStatus.PAYMENT_PENDING;
        this.updatedAt = requireInstant(now, "now");
        registerEvent(new PaymentStartedEvent(id, this.updatedAt));
    }
    public void markPaid(Instant now) {
        ensureNotCancelled();
        ensureNotConfirmed();
        if (status != OrderStatus.PAYMENT_PENDING || paymentStatus != PaymentStatus.PENDING) {
            throw new DomainConflictException("Order must be PAYMENT_PENDING with payment PENDING to mark paid");
        }
        this.paymentStatus = PaymentStatus.PAID;
        this.status = OrderStatus.PAID;
        this.updatedAt = requireInstant(now, "now");
        registerEvent(new PaymentApprovedEvent(id, this.updatedAt));
    }
    public void confirm(Instant now) {
        ensureNotCancelled();
        if (status == OrderStatus.CONFIRMED) {
            throw new DomainConflictException("Order is already confirmed");
        }
        if (paymentStatus != PaymentStatus.PAID) {
            throw new DomainConflictException("Cannot confirm order before payment");
        }
        this.status = OrderStatus.CONFIRMED;
        this.confirmedAt = requireInstant(now, "now");
        this.updatedAt = this.confirmedAt;
        registerEvent(new OrderConfirmedEvent(id, this.updatedAt));
    }
    public void cancel(Instant now) {
        ensureNotCancelled();
        if (status == OrderStatus.CONFIRMED) {
            throw new DomainConflictException("Confirmed order cannot be cancelled in this sprint");
        }
        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = requireInstant(now, "now");
        this.updatedAt = this.cancelledAt;
        if (paymentStatus == PaymentStatus.NOT_STARTED) {
            this.paymentStatus = PaymentStatus.FAILED;
        }
        registerEvent(new OrderCancelledEvent(id, this.updatedAt));
    }
    public List<OrderEvent> pullDomainEvents() {
        List<OrderEvent> snapshot = List.copyOf(domainEvents);
        domainEvents.clear();
        return snapshot;
    }
    public UUID getId() {
        return id;
    }
    public UUID getCustomerId() {
        return customerId;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public List<UUID> getReservationRefs() {
        return List.copyOf(reservationRefs);
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public Instant getConfirmedAt() {
        return confirmedAt;
    }
    public Instant getCancelledAt() {
        return cancelledAt;
    }
    private static List<OrderItem> requireItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new DomainValidationException("Order must have at least one item");
        }
        return items;
    }
    private void validateInvariants() {
        requireItems(items);
        if (status == OrderStatus.CONFIRMED && paymentStatus != PaymentStatus.PAID) {
            throw new DomainValidationException("Confirmed order must have paid payment status");
        }
        if (status == OrderStatus.CANCELLED && cancelledAt == null) {
            throw new DomainValidationException("Cancelled order must set cancelledAt");
        }
        if (status == OrderStatus.CONFIRMED && confirmedAt == null) {
            throw new DomainValidationException("Confirmed order must set confirmedAt");
        }
        BigDecimal calculatedTotal = calculateTotal(items);
        if (calculatedTotal.compareTo(totalAmount) != 0) {
            throw new DomainValidationException("Order total is inconsistent with item totals");
        }
    }
    private static BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getLineTotalSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .stripTrailingZeros();
    }
    private void ensureNotCancelled() {
        if (status == OrderStatus.CANCELLED) {
            throw new DomainConflictException("Cancelled order cannot change state");
        }
    }
    private void ensureNotConfirmed() {
        if (status == OrderStatus.CONFIRMED) {
            throw new DomainConflictException("Confirmed order cannot change state");
        }
    }
    private void registerEvent(OrderEvent event) {
        domainEvents.add(event);
    }
    private static UUID requireId(UUID value, String field) {
        if (value == null) {
            throw new DomainValidationException(field + " is required");
        }
        return value;
    }
    private static Instant requireInstant(Instant value, String field) {
        if (value == null) {
            throw new DomainValidationException(field + " is required");
        }
        return value;
    }
    private static BigDecimal requireMoney(BigDecimal value, String field) {
        if (value == null) {
            throw new DomainValidationException(field + " is required");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException(field + " must be greater than or equal to zero");
        }
        return value.stripTrailingZeros();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order order)) {
            return false;
        }
        return Objects.equals(id, order.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
