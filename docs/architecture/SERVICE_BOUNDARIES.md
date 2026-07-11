# Service Boundaries

## Status

Baseline (Sprint 1)

## Catalog Service

### Responsabilidades

- CRUD e consulta de Product e Sku.
- Regras de consistencia do agregado Product.
- Exposicao de API publica para consulta e manutencao de catalogo.

### Fora da fronteira

- Preco e promocoes.
- Estoque e reserva.
- Pedido e checkout.
- Pagamento.

### Dados proprietarios

- Tabela `products`.
- Tabela `skus`.
- Constraint de unicidade para `sellerCode` e `ean`.

## Inventory Service

### Responsabilidades

- Controle transacional de saldo, reserva e baixa de estoque por SKU e warehouse.
- Protecao de invariantes de estoque no aggregate root `InventoryItem`.
- Encapsulamento da maquina de estados de `Reservation`.

### Fora da fronteira

- Dados comerciais de produto.
- Preco e promocoes.
- Orquestracao de pedido, pagamento e saga.

### Integracoes

- Nao possui integracao runtime com Catalog; recebe `skuId` pela propria API.

### Dados proprietarios

- Tabela `inventory_items`.
- Tabela `inventory_reservations`.
- Constraints para proteger nao-negatividade e `reserved <= physical`.
- `availableQuantity` calculado e nao persistido em coluna dedicada.

## Order Service

### Responsabilidades

- Criacao de pedido, consolidacao de itens e fluxo inicial de checkout.
- Protecao das invariantes do agregado `Order` no ciclo de vida de pagamento e confirmacao.
- Exposicao dos comandos de reserva, pagamento, confirmacao e cancelamento de pedido.

### Fora da fronteira

- Regras de consistencia de catalogo (produto, SKU, conteudo comercial de origem).
- Regras de estoque transacional e ciclo de vida de reservas fisicas.
- Captura financeira real, conciliacao e antifraude.

### Dependencias

- Integra-se ao Inventory Service por REST sincrono para reserva, commit e liberacao.
- Recebe snapshots comerciais na criacao do pedido; nao consulta o Catalog Service nesta baseline.
- Usa `PaymentFakeAdapter` interno, sem Payment Service externo.

### Dados proprietarios

- Tabela `orders`.
- Tabela `order_items`.
- Constraints de consistencia para status, payment status e totais monetarios nao negativos.
