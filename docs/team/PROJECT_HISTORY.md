# Project History

Este documento registra a evolucao historica do projeto Enterprise Order Platform.

Ele nao substitui os ADRs.
Os ADRs documentam decisoes arquiteturais formais.
Este arquivo registra a narrativa do projeto, sua evolucao e seus marcos principais.

---

## 2026-07-06 - Criacao do projeto

O projeto Enterprise Order Platform foi criado como parte de um roadmap de estudos orientado a portfolio para Java Senior / Tech Lead.

A proposta e simular a evolucao de uma plataforma de e-commerce chamada Mercado Aurora, utilizando praticas de engenharia semelhantes as de um projeto corporativo real.

## Decisao inicial: uso de monorepo

Foi decidido utilizar monorepo para simplificar a evolucao inicial do projeto.

Motivos:

- Facilitar a organizacao dos servicos.
- Centralizar documentacao.
- Simplificar Docker Compose e CI/CD no futuro.
- Facilitar abertura do projeto em uma unica IDE.
- Reduzir overhead operacional durante a fase de estudo.

Alternativa considerada:

- Multi-repo por servico.

A alternativa multi-repo foi rejeitada neste momento por adicionar complexidade operacional desnecessaria para um projeto de portfolio em fase inicial.

## Sprint -1 - Bootstrap do Projeto

A Sprint -1 tem como objetivo organizar o repositorio antes da implementacao de codigo.

Entregas esperadas:

- Estrutura inicial do monorepo.
- Documentacao base.
- Issues.
- Milestones.
- Project Board.
- Primeira release bootstrap.

## 2026-07-06 - Consolidacao do Business Discovery

Foi concluida a Story de consolidacao do Business Discovery do Mercado Aurora.

O documento foi reorganizado para refletir uma visao de produto orientada ao negocio, separando claramente contexto atual, dores, necessidades e visao futura.

Resultados principais:

- Business Discovery estruturado em 18 secoes;
- consolidacao das regras de negocio identificadas;
- mapeamento de stakeholders, personas e fluxo macro da venda;
- registro de riscos, oportunidades e questoes em aberto para orientar os proximos refinamentos.

## 2026-07-07 - Sprint 0 governance alignment and Domain Glossary

Sprint 0 issues were formalized in GitHub to restore traceability between planning, documentation and implementation.

The team also introduced the Domain Glossary as the official source for shared business language before Context Map and Service Boundaries.

## 2026-07-07 - Gate review adjustments for Story-002

Post-review governance adjustments were applied to Sprint 0 stories, including standardized labels and explicit links between stories, tasks and produced documents.

Story-002 remained in review state, and the Project Board was aligned to reflect the gate decision while the glossary stays as In Review.

## 2026-07-07 - Business Flow discovery added to Sprint 0

The team introduced Business Flow Discovery as a dedicated step between Domain Glossary and Context Map to improve business traceability before boundary discussions.

A new Story-003 (Business Flow) was added to Sprint 0, subsequent stories were renumbered, and project artifacts were updated to preserve backlog consistency.

## 2026-07-07 - Architecture Notes registered for Context Map preparation

The first architecture meeting hypotheses between TM Chatinho and LT Aprendiz were formally recorded in `docs/architecture/ARCHITECTURE_NOTES.md`.

The documentation structure was also adjusted to support next steps of Story-004, including ADR consolidation in `docs/architecture/ADR/` and creation of placeholders for Context Map and Service Boundaries.

## 2026-07-08 - Sprint 0 closure and Sprint 1 kickoff

Sprint 0 was officially closed after completion of stories focused on business baseline and architecture foundation.

Story-002 (Domain Glossary) and Story-003 (Business Flow) were finalized as Baseline documents. Story-004, Story-005 and Story-006 were closed with explicit iterative refinement planned across Sprint 1.

The milestone `Sprint 0 — Fundacao Arquitetural` was closed, and Sprint 1 was prepared as `Sprint 1 — Primeiros Microsservicos` with backlog organized from Story-007 through Story-013.

## 2026-07-08 - Story-007 bootstrap do Catalog Service

O projeto recebeu a primeira implementacao funcional de microsservico com foco em dominio comercial de catalogo.

Principais entregas:

- Criacao do `catalog-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Aplicacao de DDD/hexagonal com Product como aggregate root e Sku como entidade interna.
- Implementacao dos endpoints REST de produto e SKU previstos para a Story-007.
- Implementacao de regras de negocio criticas:
  - Product ACTIVE exige SKU ACTIVE;
  - SKU nao existe sem Product;
  - sellerCode obrigatorio e unico no catalogo;
  - ean unico quando informado.
- Introducao de testes de dominio, testes de aplicacao com mocks e testes de integracao com Testcontainers.
- Criacao da ADR-004 para registrar a decisao arquitetural do aggregate root de Catalog.

## 2026-07-08 - Story-008 bootstrap do Inventory Service

O projeto recebeu o segundo microsservico funcional, agora com foco em consistencia transacional de estoque.

Principais entregas:

- Criacao do `inventory-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Aplicacao de DDD/hexagonal com `InventoryItem` como aggregate root e `Reservation` como entidade interna.
- Implementacao dos endpoints REST de estoque, ajuste fisico e ciclo de vida de reservas.
- Encapsulamento das invariantes de estoque e da maquina de estados da reserva no dominio.
- Garantia de `availableQuantity` derivado no agregado, sem persistencia manual.
- Introducao de Domain Events no agregado para preparar integracao futura com Kafka/Saga.
- Introducao de testes de dominio, aplicacao com mocks e integracao com Testcontainers.
- Criacao da ADR-005 para registrar a decisao arquitetural do aggregate root de Inventory.

## 2026-07-09 - Story-008 regularizada no GitHub

A Story-008 foi regularizada para manter rastreabilidade do projeto antes do inicio da Story-009.

Principais acoes:

- Issue da Story-008 encerrada oficialmente.
- Item correspondente movido para Done no Project Board.
- Release `v0.2.0-inventory-service` publicada no GitHub.
- Link da release registrado na issue.
- Verificacao de consistencia de registro em `CHANGELOG.md` e `PROJECT_HISTORY.md`.

## 2026-07-09 - Story-009 bootstrap do Order Service

O projeto recebeu o terceiro microsservico funcional, agora com foco no compromisso comercial do pedido.

Principais entregas:

- Criacao do `order-service` com Spring Boot, Maven, JPA, Flyway e PostgreSQL.
- Aplicacao de DDD/hexagonal com `Order` como aggregate root e `OrderItem` como entidade interna.
- Implementacao dos endpoints REST para ciclo de vida de pedido, pagamento e confirmacao.
- Encapsulamento das invariantes de pedido no dominio, incluindo regras de confirmacao e cancelamento.
- Introducao de testes de dominio, aplicacao com mocks e integracao com Testcontainers.
- Criacao da ADR-006 para registrar a decisao arquitetural do aggregate root de Order.

## 2026-07-10 - Review da Story-009

A revisao tecnica do PR da Story-009 ajustou a orquestracao de reserva de estoque antes da validacao funcional via Postman.

Principais ajustes:

- O agregado `Order` passou a ser validado antes das chamadas externas ao Inventory.
- O adapter REST de Inventory passou a tentar liberar reservas ja criadas quando uma reserva posterior falha no mesmo pedido.
- Foram adicionados testes para ordem de orquestracao, falha de integracao e reserva parcial.
- O PR e a Story permaneceram em Review, sem merge e sem publicacao de release.

## 2026-07-10 - Correcoes de QE da Story-009

A validacao de qualidade identificou lacunas de observabilidade, compensacao de pagamento e tratamento de entradas invalidas.

Principais ajustes:

- Order e Inventory passaram a expor health check pelo Spring Boot Actuator.
- O PaymentFake ganhou modo de falha configuravel, com liberacao best-effort do estoque e cancelamento persistido do pedido.
- A ordem de orquestracao passou a validar o agregado antes de pagamento e commit de reservas.
- O correlation ID passou a ser devolvido ao cliente e propagado do Order para o Inventory.
- JSON malformado e UUID invalido passaram a retornar respostas `400` sanitizadas.

## 2026-07-10 - Encerramento da Story-009

A Story-009 concluiu o ciclo de desenvolvimento do Order Service apos aprovacao tecnica e validacao funcional do Quality Engineer.

Resultados do quality gate:

- 34 requests executadas;
- 109 assertions aprovadas;
- nenhuma falha;
- smoke, happy path, cenarios negativos e regressao aprovados.

O PR #20 foi destinado ao merge em `main` e a entrega foi consolidada na release `v0.3.0-order-service`. A issue #24 permanece aberta como backlog tecnico Medium, sem bloquear a Sprint 1, para complementar a evidencia de Correlation ID ponta a ponta.

Com esse marco, Catalog, Inventory e Order estao concluidos na Sprint 1. O proximo ciclo inicia a arquitetura da Story-010, dedicada ao Payment Service.
