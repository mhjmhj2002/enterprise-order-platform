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

