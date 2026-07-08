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
