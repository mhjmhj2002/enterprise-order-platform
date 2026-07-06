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
