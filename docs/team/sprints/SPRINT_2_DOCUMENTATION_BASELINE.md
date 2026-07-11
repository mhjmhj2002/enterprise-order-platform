# Sprint 2 Documentation Report

- **Sprint:** Sprint 2 — Event-Driven Architecture
- **Data:** 2026-07-11
- **Responsável:** Technical Writer
- **Resultado:** READY FOR IMPLEMENTATION

## Documentos atualizados

- [Architecture Overview](../../architecture/ARCHITECTURE.md)
- [Context Map](../../architecture/CONTEXT_MAP.md)
- [C4 Model](../../architecture/C4.md)
- [ADR index](../../architecture/ADR/README.md)
- [README](../../../README.md)
- [Project History](../PROJECT_HISTORY.md)
- [Changelog](../../../CHANGELOG.md)

## Novos documentos

- [Event Catalog](../../architecture/events/EVENT_CATALOG.md)
- [Convenções de eventos e tópicos](../../architecture/events/EVENT_CONVENTIONS.md)
- [ADR-007 — Evolução incremental para Event-Driven Architecture](../../architecture/ADR/ADR-007-incremental-event-driven-architecture.md)

## Decisões registradas

- A arquitetura da Sprint 2 será híbrida: REST continua suportado.
- Kafka será introduzido incrementalmente apenas para `OrderConfirmed` v1.
- Order é producer e owner do fato; Inventory é o consumer inicial planejado.
- O contrato inicial exige identificadores de pedido, evento e correlação, além da data/hora de ocorrência.
- O efeito do consumo no Inventory será definido no refinamento técnico e não poderá criar ou repetir reserva de estoque.

## Pendências deliberadamente adiadas

- Resultado funcional específico do consumer e mecanismos técnicos de implementação.
- Retry, DLT e reprocessamento controlado, sujeitos à capacidade aprovada.
- Payment Service, Saga distribuída, API Gateway, autenticação, Kubernetes, Schema Registry e migração integral de REST.

## Validações

- Planejamento aprovado confrontado com os documentos institucionais.
- Links relativos e referências cruzadas validados.
- Diagramas distinguem explicitamente baseline implementada e evolução planejada.
- Nenhuma alteração de código, infraestrutura ou regras de negócio realizada.
