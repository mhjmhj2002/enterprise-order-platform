# Sprint 2 — Closure Readiness

**Status:** CLOSURE IN PROGRESS  
**Data:** 2026-07-15  
**Responsável:** Technical Writer

## Baseline consolidada

A Sprint 2 entregou a evolução incremental para eventos sem substituir a baseline REST:

- plataforma Kafka local em KRaft e Kafka UI (Story #37);
- Event Catalog e Event Platform Technical Contract institucionalizados (Stories #31 e #30);
- publicação de `OrderConfirmed` v1 pelo Order Service (Story #32);
- consumo de reconhecimento idempotente pelo Inventory Service, sem efeito sobre estoque ou reservas (Story #33).

O [Event Catalog](../architecture/events/EVENT_CATALOG.md), a [Architecture Overview](../architecture/ARCHITECTURE.md), o [README](../../README.md), o [README da plataforma](../../infra/event-platform/README.md), o [CHANGELOG](../../CHANGELOG.md) e o [PROJECT_HISTORY](PROJECT_HISTORY.md) refletem essa baseline.

## Estado do fechamento

| Item | Estado | Observação |
| --- | --- | --- |
| #30, #31, #32, #33 e #37 | Concluídas | Evidências, documentação e rastreabilidade disponíveis. |
| #34 — Confiabilidade inicial | Replanejada para Sprint 3 | Should Have; não bloqueia a conclusão Must Have da Sprint 2. |
| #35 — Evidências e documentação | Em andamento | Única Story ainda aberta no milestone; depende diretamente da #33. |
| Release da Sprint 2 | Não criada | Deve ser avaliada após #35 e autorização do Engineering Manager. |
| Milestone Sprint 2 | Aberto | Não fechar enquanto #35 permanecer aberta. |

## Limites da baseline

- REST continua ativo para o fluxo Order → Inventory.
- Não há DLT, Outbox, Saga, Payment Service externo, API Gateway, Kubernetes ou migração integral de REST.
- O retry limitado já validado é parte da baseline; evolução adicional de confiabilidade pertence à Sprint 3.

## Próximos passos de encerramento

1. Concluir e validar a Story #35.
2. Registrar e publicar a regularização administrativa da #34 para a Sprint 3 no GitHub.
3. Confirmar que não há Issues abertas no milestone Sprint 2 e que todos os cards estão em Done.
4. Preparar release notes e versão de release, mediante autorização do Engineering Manager.
5. Repository Owner executa as operações administrativas finais; o Engineering Manager autoriza release e fechamento do milestone.

## Resultado atual

**CHANGES REQUIRED** — a documentação de baseline está consolidada, mas a Story #35 e as operações administrativas de encerramento ainda estão pendentes.
