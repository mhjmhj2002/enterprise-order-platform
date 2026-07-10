# Context Map

## Status

Baseline (Sprint 1)

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

## Decisoes refletidas nos bootstraps de Sprint 1

- Product definido como aggregate root.
- SKU modelado como entidade subordinada ao Product.
- Catalogo nao conhece preco, estoque, pedido e pagamento.
- InventoryItem definido como aggregate root no contexto de Estoque & Reserva.
- Reservation modelada como entidade interna com transicoes encapsuladas no dominio.
- Disponibilidade (`availableQuantity`) calculada no dominio e nao persistida como dado primario.
- Order definido como aggregate root no contexto de Pedido, com `OrderItem` como entidade interna e snapshots comerciais imutaveis durante o fluxo.
