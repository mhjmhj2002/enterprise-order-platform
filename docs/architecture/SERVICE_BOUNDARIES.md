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

### Dependencia de Catalog

- Consome identificadores de SKU do Catalog Service.

### Dados proprietarios

- Tabela `inventory_items`.
- Tabela `inventory_reservations`.
- Constraints para proteger nao-negatividade e `reserved <= physical`.
- `availableQuantity` calculado e nao persistido em coluna dedicada.

## Order Service (planejado)

### Responsabilidades

- Criacao de pedido, consolidacao de itens e fluxo inicial de checkout.

### Dependencias

- Consulta Catalog Service para dados comerciais.
- Consulta Inventory Service para disponibilidade.
