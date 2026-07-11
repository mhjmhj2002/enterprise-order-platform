# Context Map

## Status

Baseline implementada (Sprint 1) com evolucao planejada para Sprint 2.

## Bounded Contexts mapeados

| Contexto | Tipo (DDD) | Responsabilidade principal |
|---|---|---|
| Catalogo Comercial | Supporting | Produto, SKU, categoria, marca e conteudo comercial |
| Preco & Promocoes | Supporting | Politica de precificacao e campanhas promocionais |
| Estoque & Reserva | Core | Disponibilidade, reserva e consistencia de estoque |
| Pedido | Core | Compromisso comercial do pedido |
| Pagamento & Conciliacao | Core | Captura e conciliacao financeira |
| Fulfillment & Entrega | Supporting | Separacao, expedicao e entrega |
| Atendimento & Pos-venda | Supporting | Suporte e tratamento de incidentes pos-compra |

## Relacoes atuais (Sprint 1)

1. Catalogo Comercial mantem produtos e SKUs, sem integracao runtime com os demais servicos nesta baseline.
2. Estoque & Reserva recebe identificadores de SKU pela API e controla disponibilidade e reserva por warehouse, sem consultar o Catalog Service.
3. Pedido recebe snapshots comerciais e identificadores de SKU na criacao, sem consultar o Catalog Service.
4. Pedido integra-se ao Inventory Service por REST sincrono para reservar, confirmar e liberar estoque.
5. Pagamento real nao esta implementado; o Order Service utiliza um `PaymentFakeAdapter` interno.

## Comunicacao assincrona planejada (Sprint 2)

- Order continuara sendo o owner do fato `OrderConfirmed` v1 e o producer planejado desse evento.
- Inventory sera o consumidor inicial planejado por meio de Kafka, sem assumir nova regra de reserva ou baixa de estoque.
- A comunicacao assincrona complementa, e nao substitui, as chamadas REST ja existentes entre Order e Inventory.
- Catalog, Payment Service, Gateway e Saga nao entram nessa evolucao.

O contrato planejado esta definido no [Event Catalog](events/EVENT_CATALOG.md). Nenhum producer, consumer ou broker Kafka esta implementado nesta baseline.

## Decisoes refletidas nos bootstraps de Sprint 1

- Product definido como aggregate root.
- SKU modelado como entidade subordinada ao Product.
- Catalogo nao conhece preco, estoque, pedido e pagamento.
- InventoryItem definido como aggregate root no contexto de Estoque & Reserva.
- Reservation modelada como entidade interna com transicoes encapsuladas no dominio.
- Disponibilidade (`availableQuantity`) calculada no dominio e nao persistida como dado primario.
- Order definido como aggregate root no contexto de Pedido, com `OrderItem` como entidade interna e snapshots comerciais imutaveis durante o fluxo.
