# ADR-001: Escopo minimo da Sprint 0 - sem mensageria, sem orquestracao, pagamento simulado

## Status

Aceito

## Contexto

O Mercado Aurora precisa de uma base de arquitetura em microsservicos para evoluir sua plataforma de e-commerce. Atualmente opera em torno de 5 mil pedidos/dia e ainda nao possui demanda que justifique a introducao imediata de mensageria, cache distribuido, orquestracao de containers ou integracoes reais de pagamento/autenticacao.

Introduzir Kafka, Redis, Kubernetes e servicos reais de Payment/Auth desde o inicio adicionaria complexidade operacional sem beneficio imediato para a validacao das fronteiras de dominio.

## Decisao

A Sprint 0 implementara apenas:

- Catalog Service
- Inventory Service
- Order Service

A comunicacao entre os servicos sera feita via REST sincrono.

O pagamento sera simulado por meio de um `PaymentFakeAdapter` dentro do Order Service.

## Consequencias

Ganhamos velocidade para validar fronteiras de dominio, contratos entre servicos e o fluxo minimo de pedido.

Como trade-off, aceitamos revisitar o fluxo de pagamento, autenticacao e comunicacao assincrona nas proximas sprints.

Essa decisao fica documentada para evitar esquecimento tecnico e para impedir que a Sprint 0 cresca alem do necessario.

## Alternativas consideradas

### Implementar Payment Service real desde o inicio

Rejeitado porque adicionaria dependencia externa e complexidade antes de estabilizar o fluxo core de pedidos.

### Usar Kafka desde o inicio

Rejeitado porque o overhead operacional nao e justificado pelo volume atual e desviaria o foco da Sprint 0.

