# ADR-005: InventoryItem como Aggregate Root no Inventory Service

## Status

Aceito

## Contexto

A Story-008 exige o bootstrap do Inventory Service como primeiro contexto realmente transacional da plataforma.

Era necessario formalizar a fronteira transacional entre `InventoryItem` e `Reservation`, protegendo:

- `physicalQuantity >= 0`
- `reservedQuantity >= 0`
- `reservedQuantity <= physicalQuantity`
- `availableQuantity = physicalQuantity - reservedQuantity`
- Reserva somente quando quantidade solicitada e menor ou igual ao disponivel
- Transicoes da reserva apenas de `CREATED` para `COMMITTED`, `RELEASED` ou `EXPIRED`
- Estado final de reserva imutavel

## Decisao

Adotar **InventoryItem** como aggregate root no Inventory Service.

- `Reservation` torna-se entidade interna do agregado.
- O estado de reserva e suas transicoes ficam encapsulados no dominio.
- Operacoes de ajuste, reserva, commit e release ocorrem sempre via agregado carregado por `skuId + warehouseId`.
- `availableQuantity` e derivado no dominio e nao persistido como campo de entrada/manual.
- O agregado registra Domain Events para futura publicacao assíncrona, sem acoplar a Sprint 1 a Kafka/Saga.

## Consequencias

Positivas:

- Invariantes de estoque centralizadas e protegidas em um unico ponto.
- Menor risco de vazamento de regra para camada de aplicacao ou API.
- Base pronta para evolucao para Outbox/Kafka/Saga sem reescrita do nucleo de dominio.

Negativas:

- Operacoes em reserva exigem sempre carregar o agregado completo.
- Necessidade de disciplina para manter services como orquestradores e nao como validadores de regra.

## Alternativas consideradas

### Reservation como aggregate root independente

Rejeitada porque permitiria quebrar invariantes de `reservedQuantity` e fragmentaria a consistencia do estoque.

### Persistir `availableQuantity` como dado manual

Rejeitada porque cria risco de divergencia com `physicalQuantity` e `reservedQuantity`, enfraquecendo o modelo transacional.
