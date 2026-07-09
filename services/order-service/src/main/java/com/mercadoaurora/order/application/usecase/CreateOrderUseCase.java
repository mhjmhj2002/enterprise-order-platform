package com.mercadoaurora.order.application.usecase;
import com.mercadoaurora.order.application.command.CreateOrderCommand;
import com.mercadoaurora.order.application.command.CreateOrderItemInput;
import com.mercadoaurora.order.application.port.out.OrderRepositoryPort;
import com.mercadoaurora.order.domain.Order;
import com.mercadoaurora.order.domain.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Service
public class CreateOrderUseCase {
    private final OrderRepositoryPort repositoryPort;
    private final Clock clock;
    public CreateOrderUseCase(OrderRepositoryPort repositoryPort, Clock clock) {
        this.repositoryPort = repositoryPort;
        this.clock = clock;
    }
    @Transactional
    public Order execute(CreateOrderCommand command) {
        Instant now = Instant.now(clock);
        List<OrderItem> items = command.items().stream().map(this::toDomainItem).toList();
        Order order = Order.create(UUID.randomUUID(), command.customerId(), items, now);
        return repositoryPort.save(order);
    }
    private OrderItem toDomainItem(CreateOrderItemInput input) {
        return OrderItem.create(
                input.skuId(),
                input.productNameSnapshot(),
                input.skuNameSnapshot(),
                input.attributesSnapshot(),
                input.quantity(),
                input.unitPriceSnapshot(),
                input.discountSnapshot(),
                input.lineTotalSnapshot()
        );
    }
}
