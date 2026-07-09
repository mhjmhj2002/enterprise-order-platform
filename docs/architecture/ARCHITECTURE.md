# Architecture Overview

Este documento consolida a visao geral da arquitetura do projeto.

## Estado atual (Sprint 1)

- Monorepo com servicos em `services/`.
- Comunicacao inicial via REST sincrono.
- Persistencia por servico com schema proprio.
- Catalog Service bootstrapado com DDD + arquitetura hexagonal.
- Inventory Service bootstrapado com dominio transacional e arquitetura hexagonal.
- Order Service bootstrapado como contexto integrador de compromisso comercial.

## Catalog Service (Story-007)

Implementacao de referencia para os proximos microsservicos com camadas:

- `domain`: entidades e regras do agregado Product.
- `application`: casos de uso e portas.
- `infrastructure`: adaptadores de persistencia (JPA/Flyway).
- `api`: controllers REST, DTOs e tratamento de erro.
- `config`: beans tecnicos.

Regras obrigatorias implementadas:

- SKU nao existe sem Product.
- Product ACTIVE exige ao menos um SKU ACTIVE.
- `sellerCode` obrigatorio e unico no catalogo.
- `ean` unico quando informado.
- Catalogo sem acoplamento com preco, estoque, pedido e pagamento.

## Inventory Service (Story-008)

Implementacao do contexto de Estoque & Reserva com foco transacional:

- `InventoryItem` como aggregate root com identidade logica `skuId + warehouseId`.
- `Reservation` como entidade interna com maquina de estados (`CREATED`, `COMMITTED`, `RELEASED`, `EXPIRED`) encapsulada no dominio.
- `availableQuantity` derivado exclusivamente de `physicalQuantity - reservedQuantity`.
- Invariantes de consistencia protegidas no aggregate root (sem vazamento para service/controller).
- Casos de uso de criacao, ajuste de saldo fisico, reserva, commit/release de reserva e consultas.
- Persistencia em PostgreSQL (JPA + Flyway) sem coluna de `availableQuantity`.
- Registro de Domain Events no agregado, deixando pontos naturais para posterior integracao via Kafka/Saga.

## Order Service (Story-009)

Implementacao do contexto de Pedido com foco no compromisso comercial:

- `Order` como aggregate root com ciclo de vida de checkout transacional na Sprint 1.
- `OrderItem` como entidade interna com snapshot comercial e validacao de totais por item.
- Invariantes de dominio encapsuladas no agregado: pedido com ao menos um item, total consistente, sem confirmacao sem pagamento e sem inicio de pagamento sem estoque reservado.
- Casos de uso de criacao, reserva de estoque, inicio de pagamento, marcacao de pagamento, confirmacao, cancelamento e consultas.
- Persistencia em PostgreSQL (JPA + Flyway) com tabelas proprietarias `orders` e `order_items`.
- Registro de Domain Events (`OrderCreated`, `PaymentStarted`, `OrderPaid`, `OrderConfirmed`, `OrderCancelled`) mantendo o dominio pronto para evolucao orientada a eventos sem acoplamento com Kafka nesta sprint.

## Referencias

- [ADRs](ADR)
- [C4 Model](C4.md)
- [Engineering Handbook](ENGINEERING_HANDBOOK.md)
- [Architecture Notes](ARCHITECTURE_NOTES.md)
- [Context Map](CONTEXT_MAP.md)
- [Service Boundaries](SERVICE_BOUNDARIES.md)
