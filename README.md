# Enterprise Order Platform

Projeto de portfolio Enterprise em Java/Spring Boot para estudo pratico de arquitetura de microsservicos, mensageria, resiliencia, observabilidade e lideranca tecnica.

## Objetivo

Este projeto simula a evolucao de uma plataforma de e-commerce chamada Mercado Aurora.

A proposta e construir a solucao de forma incremental, tomando decisoes arquiteturais justificadas por problemas reais de negocio.

## Arquitetura inicial

Na Sprint 0, o projeto comeca com tres servicos principais:

- Catalog Service
- Inventory Service
- Order Service

A comunicacao inicial sera sincrona via REST.

Pagamento sera simulado dentro do Order Service por meio de um PaymentFakeAdapter.

## Ambiente local (Docker)

O passo a passo completo para subir PostgreSQL, validar Docker, criar bancos/usuarios e rodar os servicos localmente esta em:

- [`docker/README.md`](docker/README.md)

## Implementacao atual

### Catalog Service

`services/catalog-service` foi bootstrapado na Story-007 com:

- Spring Boot + Maven + PostgreSQL + Flyway.
- DDD com Product (aggregate root) e Sku (entidade).
- Arquitetura hexagonal (`domain`, `application`, `infrastructure`, `api`, `config`).
- Endpoints REST em `/api/v1/products` e `/api/v1/skus`.
- Testes de dominio, aplicacao (mocks) e integracao (Testcontainers).

Comando rapido:

```bash
cd services/catalog-service
mvn test
```

### Inventory Service

`services/inventory-service` foi bootstrapado na Story-008 com:

- Spring Boot + Maven + PostgreSQL + Flyway.
- DDD com `InventoryItem` (aggregate root) e `Reservation` (entidade interna).
- Arquitetura hexagonal (`domain`, `application`, `infrastructure`, `api`, `config`).
- Endpoints REST em `/api/v1/inventory`.
- Testes de dominio, aplicacao (mocks) e integracao (Testcontainers).
- Pontos naturais para evolucao de eventos de dominio (Kafka/Saga) sem reescrever o agregado.

Comando rapido:

```bash
cd services/inventory-service
mvn test
```

### Order Service

`services/order-service` foi bootstrapado na Story-009 com:

- Spring Boot + Maven + PostgreSQL + Flyway.
- DDD com `Order` (aggregate root) e `OrderItem` (entidade interna).
- Arquitetura hexagonal (`domain`, `application`, `infrastructure`, `api`, `config`).
- Endpoints REST em `/api/v1/orders` e `/api/v1/customers/{customerId}/orders`.
- Fluxo de pedido com estados `CREATED -> STOCK_RESERVED -> PAYMENT_PENDING -> PAID -> CONFIRMED` e cancelamento controlado.
- Testes de dominio, aplicacao (mocks) e integracao (Testcontainers).

Comando rapido:

```bash
cd services/order-service
mvn test
```

## Documentacao

### Arquitetura

- [Engineering Handbook](docs/architecture/ENGINEERING_HANDBOOK.md)
- [Architecture Overview](docs/architecture/ARCHITECTURE.md)
- [Architecture Notes](docs/architecture/ARCHITECTURE_NOTES.md)
- [C4 Model](docs/architecture/C4.md)
- [Context Map](docs/architecture/CONTEXT_MAP.md)
- [Service Boundaries](docs/architecture/SERVICE_BOUNDARIES.md)
- [ADRs](docs/architecture/ADR)

### Time

- [Team Charter](docs/team/TEAM_CHARTER.md)
- [Project History](docs/team/PROJECT_HISTORY.md)
- [Business Discovery](docs/business/BUSINESS_DISCOVERY.md)
- [Domain Glossary](docs/business/GLOSSARY.md)
- [Business Flow](docs/business/BUSINESS_FLOW.md)

### Roadmap

- [Roadmap](docs/roadmap/Roadmap_Estudos_Portfolio_Java_TechLead_v2.md)

## Status

Sprint 0 - Fundacao arquitetural (encerrada).
Sprint 1 - Primeiros Microsservicos (em andamento).

## Gestao do Projeto

Este projeto utiliza praticas simuladas de engenharia corporativa:

- Issues para rastrear trabalho.
- Milestones para organizar Sprints.
- Project Board para acompanhar fluxo.
- ADRs para documentar decisoes arquiteturais.
- Releases para marcar entregas incrementais.
