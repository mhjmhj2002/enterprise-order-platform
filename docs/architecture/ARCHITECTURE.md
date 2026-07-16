# Architecture Overview

Este documento consolida a visao geral da arquitetura do projeto.

## Estado atual (Sprint 4)

- Monorepo com servicos em `services/`.
- Comunicacao inicial via REST sincrono.
- Persistencia por servico com schema proprio.
- Catalog Service bootstrapado com DDD + arquitetura hexagonal.
- Inventory Service bootstrapado com dominio transacional e arquitetura hexagonal.
- Order Service bootstrapado como contexto integrador de compromisso comercial.
- Plataforma Kafka local operacional, com publicação assíncrona de `OrderConfirmed` v1 pelo Order Service e consumo confiável pelo Inventory Service.

## Evolucao da Sprint 2

A Sprint 2 realiza a evolucao incremental para arquitetura orientada a eventos. A plataforma permanece hibrida: as integracoes REST existentes continuam suportadas e Kafka foi introduzido para a publicação de `OrderConfirmed` v1 pelo Order Service e seu consumo de reconhecimento pelo Inventory Service.

O consumer inicial do Inventory Service reconhece o evento e persiste evidência idempotente por `eventId`, sem alterar estoque, reservas ou pedidos. Não há migração integral de REST, Payment Service, Saga distribuída, API Gateway ou novos serviços no escopo da Sprint 2.

Na Story #34 (Sprint 3), o Inventory passou a registrar uma pendência durável antes do reconhecimento e a retomá-la localmente quando uma falha temporária cessa. A consulta aditiva de processamento torna demonstrável o estado `PENDING` ou `COMPLETED`; o contrato `OrderConfirmed` v1, tópico, producer, grupo de consumo e integração REST permanecem inalterados.

Na Story #44 (Sprint 4), o Inventory passou a expor uma visão operacional local e aditiva por `orderId`. Ela associa o fato recebido ao estado atual, à evidência final e aos marcos seguros `REGISTERED`, `TEMPORARY_FAILURE` e `COMPLETED`. A visão não consulta o Order, não expõe payload, credenciais, stack traces ou exceções brutas, e não constitui observabilidade corporativa, tracing distribuído, métricas centralizadas ou dashboards.

O fluxo entregue publica o fato de domínio de que um pedido foi confirmado depois das regras vigentes de pagamento aprovado e estoque reservado. O Inventory registra a ciência rastreável do fato; a publicação e o consumo não alteram a semântica da confirmação nem a integração REST com Inventory.

As convencoes e o contrato institucional estao em [Event Catalog](events/EVENT_CATALOG.md), [convencoes de eventos](events/EVENT_CONVENTIONS.md) e [ADR-007](ADR/ADR-007-incremental-event-driven-architecture.md).

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
- Registro em memoria de Domain Events (`OrderCreated`, `StockReserved`, `StockReservationFailed`, `PaymentStarted`, `PaymentApproved`, `PaymentFailed`, `OrderConfirmed`, `OrderCancelled`) e publicação externa de `OrderConfirmed` v1 quando o perfil Kafka está ativo.
- Integracao REST sincrona exclusivamente com Inventory para reserva, commit e liberacao; nao existe integracao runtime entre Order e Catalog.

## Referencias

- [ADRs](ADR)
- [C4 Model](C4.md)
- [Engineering Handbook](ENGINEERING_HANDBOOK.md)
- [Architecture Notes](ARCHITECTURE_NOTES.md)
- [Context Map](CONTEXT_MAP.md)
- [Service Boundaries](SERVICE_BOUNDARIES.md)
- [Story #34 Architecture Gate](contracts/STORY_034_ARCHITECTURE_GATE.md)
- [Story #44 Architecture Gate](contracts/STORY_044_ARCHITECTURE_GATE.md)
