# Changelog

Todas as mudancas relevantes deste projeto serao documentadas neste arquivo.

O formato segue uma abordagem inspirada em Keep a Changelog e versionamento SemVer.

## [0.1.0] - Em desenvolvimento

### Added

- Estrutura inicial do monorepo.
- Documentacao base do projeto.
- Team Charter.
- Roadmap de estudos orientado a portfolio.
- ADR-001 com escopo minimo da Sprint 0.
- Organizacao inicial do GitHub com Issues, Milestones e Project Board.
- `PROJECT_HISTORY.md` para registrar evolucao historica do projeto.
- ADR-002 documentando decisao de monorepo.
- Primeira release bootstrap planejada (`v0.0.1-bootstrap`).
- Reorganizacao da documentacao com nova estrutura em `docs/architecture/` e `docs/team/`.
- Promocao de `ENGINEERING_GUIDE` para `docs/architecture/ENGINEERING_HANDBOOK.md`.
- Criacao de `docs/architecture/ARCHITECTURE.md` e `docs/architecture/C4.md`.
- Criacao da ADR-003 para formalizar a organizacao da documentacao.
- Consolidacao de `docs/business/BUSINESS_DISCOVERY.md` com visao madura de produto em 18 secoes.
- Added Domain Glossary with core business terms for Mercado Aurora.
- Added Sprint 0 GitHub issues and project governance alignment.
- Added supporting document traceability links to Sprint 0 stories in GitHub Issues.
- Standardized Sprint 0 governance labels by type, role, domain, sprint and status.
- Updated Story-002 gate status to In Review and aligned Project Board to Review.
- Evolved `docs/team/WORKFLOW.md` with Document Lifecycle states.
- Added `docs/business/BUSINESS_FLOW.md` describing the end-to-end business order flow.
- Added Story-003 (Business Flow) in Sprint 0 and renumbered subsequent Sprint 0 stories.
- Updated Sprint 0 GitHub board and issue traceability after story renumbering.
- Added `docs/architecture/ARCHITECTURE_NOTES.md` as the official log for architecture discussions in progress.
- Reorganized ADR location to `docs/architecture/ADR/` and updated architecture references.
- Added placeholders `docs/architecture/CONTEXT_MAP.md` and `docs/architecture/SERVICE_BOUNDARIES.md`.
- Added `docs/tasks/` directory for documentation task artifacts.
- Closed Sprint 0 stories (Story-002 to Story-006), with Domain Glossary and Business Flow promoted to Baseline.
- Closed milestone `Sprint 0 — Fundacao Arquitetural` and opened Sprint 1 execution track.
- Aligned Sprint 1 milestone as `Sprint 1 — Primeiros Microsservicos` and reorganized backlog with Story-007 to Story-013.
- Updated `docs/team/WORKFLOW.md` with guidance for continuous architecture/doc evolution via ADRs.
- Bootstrap completo de `services/catalog-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Implementacao do modelo de dominio de Catalogo com Product (aggregate root) e Sku (entidade).
- Implementacao dos casos de uso: CreateProduct, UpdateProduct, ChangeProductStatus, AddSku, UpdateSku, GetProduct, GetSku, ListActiveProducts.
- Implementacao da API REST v1 de Catalogo com tratamento padronizado de erros (400/404/409/500).
- Migracao Flyway inicial `V1__init_catalog_schema.sql` com constraints de unicidade de `sellerCode` e `ean`.
- Testes de dominio para regras do agregado Product/Sku.
- Testes de aplicacao com mocks cobrindo todos os casos de uso de Catalogo.
- Testes de integracao com Spring Boot Test + Testcontainers + PostgreSQL para fluxos de Product e SKU.
- ADR-004 formalizando Product como Aggregate Root no Catalog Service.
- Atualizacao de `CONTEXT_MAP.md`, `SERVICE_BOUNDARIES.md`, `ARCHITECTURE.md`, `ARCHITECTURE_NOTES.md` e READMEs com baseline de Sprint 1.
- Bootstrap completo de `services/inventory-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Implementacao do modelo de dominio de Inventory com `InventoryItem` (aggregate root) e `Reservation` (entidade interna).
- Implementacao dos casos de uso: CreateInventoryItem, AdjustPhysicalStock, ReserveStock, ReleaseReservation, CommitReservation, GetInventoryBySku, GetInventoryBySkuAndWarehouse.
- Implementacao da API REST v1 de Inventory com tratamento padronizado de erros (400/404/500).
- Migracao Flyway inicial `V1__init_inventory_schema.sql` com constraints de consistencia para estoque e reservas.
- Testes de dominio para regras do agregado Inventory/Reservation.
- Testes de aplicacao com mocks cobrindo todos os casos de uso de Inventory.
- Testes de integracao com Spring Boot Test + Testcontainers + PostgreSQL para fluxos de estoque e reserva.
- ADR-005 formalizando `InventoryItem` como Aggregate Root no Inventory Service.
- Nova collection Postman: `docs/api/postman/inventory-service.postman_collection.json`.
- Atualizacao de `CONTEXT_MAP.md`, `SERVICE_BOUNDARIES.md`, `ARCHITECTURE.md`, `ARCHITECTURE_NOTES.md` e READMEs com baseline transacional do Inventory.
- Regularizacao da Story-008 no GitHub com issue encerrada, board em Done e release `v0.2.0-inventory-service` publicada.
- Bootstrap completo de `services/order-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Implementacao do modelo de dominio de Pedido com `Order` (aggregate root) e `OrderItem` (entidade interna).
- Implementacao dos casos de uso: CreateOrder, ReserveOrderStock, StartPayment, MarkOrderPaid, ConfirmOrder, CancelOrder, GetOrder, ListOrdersByCustomer.
- Implementacao da API REST v1 de Order com tratamento padronizado de erros (400/404/409/500).
- Migracao Flyway inicial `V1__init_order_schema.sql` com constraints de consistencia para status e totais.
- Testes de dominio para invariantes e transicoes de estado do agregado Order.
- Testes de aplicacao com mocks cobrindo fluxo principal e cenarios de erro de Order.
- Testes de integracao com Spring Boot Test + Testcontainers + PostgreSQL para fluxos de pedido.
- ADR-006 formalizando `Order` como Aggregate Root no Order Service.
- Nova collection Postman: `docs/api/postman/order-service.postman_collection.json`.
- Ajuste pós-review da Story-009: validacao do agregado antes de chamadas ao Inventory, compensacao de reserva parcial e testes de erro de integracao.

### Changed

- Atualizado `services/catalog-service/pom.xml` para baseline oficial de Java 21 LTS.
- Removido `productId` da entidade de dominio `Sku`, mantendo o vinculo com `Product` apenas como detalhe de persistencia/adapters.
