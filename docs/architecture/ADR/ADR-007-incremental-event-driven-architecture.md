# ADR-007: Evolução incremental para Event-Driven Architecture

## Status

Aceito e implementado incrementalmente na Sprint 2; a Story #34 da Sprint 3 adicionou confiabilidade interna e a Story #44 da Sprint 4 adicionou visibilidade operacional local ao processamento do consumer, sem alterar esta decisão.

## Contexto

Na baseline da Sprint 1, Order coordena reserva, commit e liberação de estoque com Inventory por REST síncrono. Esse fluxo permanece necessário, porém fatos de negócio posteriores à confirmação do pedido podem ser comunicados sem nova dependência síncrona do Order.

O planejamento aprovado da Sprint 2 propõe demonstrar um fluxo assíncrono ponta a ponta, preservando o comportamento REST existente e evitando uma migração integral antes da validação do primeiro caso.

## Decisão

Adotar uma evolução híbrida e incremental com Apache Kafka para o único evento inicial `OrderConfirmed` v1:

- Order será o producer e owner da semântica do fato de domínio.
- Inventory será o consumer inicial planejado.
- O tópico planejado será `mercadoaurora.order.order-confirmed.v1`.
- O evento será publicado apenas após confirmação bem-sucedida do pedido, pagamento aprovado e estoque previamente reservado.
- O resultado do consumidor será definido no refinamento técnico, deve ser verificável e rastreável, e não poderá criar nem repetir reserva de estoque.
- REST continuará suportado; a Sprint não promove substituição integral das integrações atuais.

As convenções de nomenclatura, versão e ownership estão em [Event Conventions](../events/EVENT_CONVENTIONS.md); o contrato mínimo está no [Event Catalog](../events/EVENT_CATALOG.md).

## Alternativas consideradas

### Manter somente REST

Rejeitada para o objetivo de aprendizado da Sprint, pois não demonstra processamento assíncrono nem reduz o acoplamento temporal para fatos posteriores à confirmação.

### Migrar todas as integrações REST para Kafka

Rejeitada por ampliar o risco e substituir fluxos já validados antes de comprovar o primeiro evento ponta a ponta.

### Introduzir Payment Service, Saga ou Gateway junto com Kafka

Rejeitada por extrapolar o planejamento aprovado e por misturar responsabilidades ainda não necessárias ao primeiro fluxo assíncrono.

## Consequências

### Positivas

- Cria uma baseline reproduzível de comunicação assíncrona entre Order e Inventory.
- Mantém baixo risco ao preservar REST durante a transição.
- Estabelece contrato, ownership e versionamento antes da implementação.
- Torna explícitos os requisitos de rastreabilidade e não duplicidade do efeito de negócio.

### Limitações atuais

- Kafka, producer e consumer inicial estão implementados para `OrderConfirmed` v1; a recuperação de pendências e a observação operacional local permanecem internas ao Inventory.
- O contrato de negócio do evento e o escopo do consumidor permanecem limitados ao reconhecimento rastreável, sem nova regra de estoque.
- Não há promessa de processamento exatamente uma vez.
- Retry, DLT e Schema Registry dependem de capacidade e aprovação posterior.

## Evolução prevista

A Sprint 2 validará somente o fluxo `OrderConfirmed` v1. Eventos adicionais, novos consumidores, confiabilidade ampliada ou arquitetura distribuída mais abrangente serão avaliados depois da evidência do núcleo mínimo.
