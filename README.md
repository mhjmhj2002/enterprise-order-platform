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

## Documentacao

- [Team Charter](TEAM_CHARTER.md)
- [Roadmap](docs/roadmap/Roadmap_Estudos_Portfolio_Java_TechLead_v2.md)
- [ADRs](docs/adr)

## Status

Sprint 0 - Fundacao arquitetural.

## Gestao do Projeto

Este projeto utiliza praticas simuladas de engenharia corporativa:

- Issues para rastrear trabalho.
- Milestones para organizar Sprints.
- Project Board para acompanhar fluxo.
- ADRs para documentar decisoes arquiteturais.
- Releases para marcar entregas incrementais.
