# Changelog

Todas as mudancas relevantes deste projeto serao documentadas neste arquivo.

O formato segue uma abordagem inspirada em Keep a Changelog e versionamento SemVer.

## [Unreleased]

### Changed

- Gate documental da Sprint 2 aprovado: contrato, catálogo, READMEs, arquitetura, replanejamento da #34, evidências da #35 e limites da baseline foram consolidados para decisão de release do Engineering Manager.
- Preparação de encerramento da Sprint 2 consolidada: #34 replanejada para a Sprint 3, #35 dependente diretamente da #33 e registros de baseline/revisão sincronizados; não há release da Sprint 2 nesta etapa.
- Sprint 2 sincronizada com o backlog refinado: #30, #31, #32, #33, #34, #35 e #37 agora estão rastreáveis no Product Plan, com dependências oficiais preservadas.
- Project History e relatório documental da Sprint 2 atualizados para registrar a aprovação do Event Platform Technical Contract e sua consistência com o Event Catalog.
- README evoluido para a Portfolio Edition v2.0, com resumo executivo, visao arquitetural, navegacao, execucao local, fluxo funcional, evidencias de qualidade, roadmap e documentacao categorizada.
- Documentacao arquitetural da Sprint 1 alinhada as integracoes REST implementadas e aos Domain Events atuais do Order Service.
- C4 deixou de ser placeholder e passou a representar os containers efetivamente implementados.
- Revisao final de conteudo da Sprint 1 aprovada, aguardando apenas o merge do PR #28 e o fechamento administrativo do milestone.
- Story #15 regularizada e encerrada administrativamente com o escopo implementado de integracao REST sincrona Order → Inventory.
- Story #16 aprovada e encerrada com o Context Map alinhado a baseline implementada.
- Story #17 aprovada e encerrada com os Service Boundaries alinhados ao C4, Context Map, endpoints e integracoes implementadas.
- Story #18 aprovada e encerrada com a Architecture Overview consolidada como baseline da Sprint 1.
- Issue #24 removida do milestone e mantida como backlog tecnico Medium nao bloqueante.
- Sprint 1 encerrada operacionalmente após merge do PR #28 e fechamento do milestone.
- Baseline documental da Sprint 2 adicionada para a evolução incremental a eventos, sem implementação de Kafka ou novos serviços.
- Playbooks de Engineering Manager, Product Owner, Technical Writer, Software Engineer, Quality Engineer e LT subordinados ao Engineering Workflow.
- `docs/team/WORKFLOW.md` reposicionado como documento complementar para evitar conflito de governança.

### Added

- Story #33 concluída: Inventory Service consome `OrderConfirmed` v1, persiste evidência idempotente por `eventId` e preserva estoque, reservas e integração REST sem alterações.
- Story #32 concluída: Order Service publica `OrderConfirmed` v1 de forma assíncrona no Kafka, com envelope JSON v1, versionamento e `orderId` como chave da mensagem.
- Event Catalog institucional com `OrderConfirmed` v1, ownership, tópico planejado e payload mínimo de negócio.
- Convenções institucionais de nomenclatura, versionamento, ownership, rastreabilidade e duplicidade de eventos.
- ADR-007 para registrar a adoção incremental de Event-Driven Architecture na Sprint 2.
- `docs/team/ENGINEERING_WORKFLOW.md` como fonte institucional de governança, handoffs, authority matrix e gates de engenharia.
- `docs/team/ENGINEERING_ROADMAP.md` como visão institucional, evolutiva e não vinculante das Sprints concluídas, atual e futuras.

## [0.3.0] - 2026-07-10

### Added

- Bootstrap completo do Order Service com arquitetura hexagonal, persistencia PostgreSQL e migracoes Flyway.
- Integracao REST sincrona com o Inventory Service para reserva, confirmacao e compensacao de estoque.
- `PaymentFakeAdapter` configuravel para os fluxos de aprovacao e falha de pagamento.
- Casos de uso do ciclo de vida de pedidos e ADR-006 para o aggregate root `Order`.
- Testes unitarios, de integracao e validacao funcional com 34 requests e 109 assertions aprovadas.
- Collection Postman autocontida para smoke, happy path, cenarios negativos e regressao.

### Changed

- Tratamento de JSON malformado e UUID invalido com respostas `400` sanitizadas.
- Propagacao de Correlation ID entre Order e Inventory; a evidencia ponta a ponta permanece no backlog tecnico da issue #24.

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
- Correcoes de QE da Story-009: health checks, PaymentFake configuravel com compensacao, correlation ID e erros `400` sanitizados para JSON/UUID invalidos.
- Collection da Story-009 isolada por cenario, com override de falha do PaymentFake por requisicao e sem necessidade de restart entre happy path e compensacao.

### Changed

- Atualizado `services/catalog-service/pom.xml` para baseline oficial de Java 21 LTS.
- Removido `productId` da entidade de dominio `Sku`, mantendo o vinculo com `Product` apenas como detalhe de persistencia/adapters.
