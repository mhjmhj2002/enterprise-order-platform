# Architecture Overview

Este documento consolida a visao geral da arquitetura do projeto.

## Estado atual (Sprint 1)

- Monorepo com servicos em `services/`.
- Comunicacao inicial via REST sincrono.
- Persistencia por servico com schema proprio.
- Catalog Service bootstrapado com DDD + arquitetura hexagonal.

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

## Referencias

- [ADRs](ADR)
- [C4 Model](C4.md)
- [Engineering Handbook](ENGINEERING_HANDBOOK.md)
- [Architecture Notes](ARCHITECTURE_NOTES.md)
- [Context Map](CONTEXT_MAP.md)
- [Service Boundaries](SERVICE_BOUNDARIES.md)
