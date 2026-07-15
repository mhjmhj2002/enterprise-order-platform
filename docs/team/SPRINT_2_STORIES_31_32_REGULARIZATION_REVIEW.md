# Parecer de Regularização Administrativa — Stories #31 e #32

**Data:** 2026-07-15  
**Responsável:** Technical Writer  
**Escopo:** Rastreabilidade institucional da Sprint 2; sem alteração de código, arquitetura ou comportamento funcional.

## Resultado executivo

As Stories #31 e #32 atendem aos respectivos critérios de aceite na branch `main`. Não foi identificada divergência técnica ou documental que justifique mantê-las abertas. Ambas devem ser **encerradas administrativamente**.

O impedimento para o encerramento da Sprint 2 não está nas Stories #31 ou #32. Permanecem a decisão e a regularização de backlog das Stories #34 e #35, além do fluxo de release e fechamento da Sprint.

## Story #31 — Catálogo inicial de eventos de pedido

### Evidências verificadas

- O [Event Catalog](../architecture/events/EVENT_CATALOG.md) identifica exclusivamente `OrderConfirmed` v1, seu propósito, o Order Service como producer/owner, o Inventory Service como consumer, tópico, chave, gatilho, envelope JSON e versão.
- O catálogo define os dados mínimos de rastreabilidade (`eventId`, `correlationId`, `data.orderId` e `occurredAt`) e a regra de evolução incompatível por nova versão maior do tópico.
- O [Event Platform Technical Contract](../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) complementa as convenções de tópico, serialização, ownership e compatibilidade sem introduzir evento adicional.
- O [Architecture Overview](../architecture/ARCHITECTURE.md) e o README descrevem o estado implementado de publicação e consumo sem contradizer o catálogo.

### Situação administrativa encontrada

| Artefato | Estado encontrado | Estado recomendado |
| --- | --- | --- |
| Issue #31 | Aberta, label `status:backlog` | Fechada, label `status:done` |
| Project Board | `Todo` | `Done` |
| Milestone Sprint 2 | Associada | Manter associada como entrega concluída |

### Recomendação

**Encerrar administrativamente a Story #31.** O catálogo já é institucional, está publicado na `main` e foi consumido pelas entregas #32 e #33. Não há pendência funcional ou documental remanescente no escopo da Story.

## Story #32 — Publicação de `OrderConfirmed` v1

### Evidências verificadas

- A implementação do producer está integrada na `main` pelos PRs [#40](https://github.com/mhjmhj2002/enterprise-order-platform/pull/40) e [#41](https://github.com/mhjmhj2002/enterprise-order-platform/pull/41).
- O Order Service publica em `mercadoaurora.order.order-confirmed.v1`, usa `orderId` como chave string UUID, envelope JSON v1 e confirmação técnica `acks=all` no perfil `kafka`.
- O [Test Report da Story-016](../quality/story-016/TEST_REPORT.md) registra validação concluída, correção e reteste da serialização ISO-8601, cenários negativos e regressão REST sem Kafka.
- O Event Catalog, a Architecture Overview, o CHANGELOG, o PROJECT_HISTORY e o README da plataforma refletem a entrega. A Story #33 também foi concluída e valida o consumo do evento publicado.

### Situação administrativa encontrada

| Artefato | Estado encontrado | Estado recomendado |
| --- | --- | --- |
| Issue #32 | Aberta, label `status:review` | Fechada, label `status:done` |
| Project Board | `In Progress` | `Done` |
| Milestone Sprint 2 | Associada | Manter associada como entrega concluída |

### Recomendação

**Encerrar administrativamente a Story #32.** A única pendência identificada é administrativa: a Issue e o card não foram atualizados depois do merge e da validação. Não há pendência técnica aberta no escopo do producer.

## Consistência de rastreabilidade

| Fonte | #31 — Catálogo | #32 — Producer | Resultado |
| --- | --- | --- | --- |
| GitHub Issue | Aberta indevidamente | Aberta indevidamente | Regularização necessária |
| Project Board | `Todo` | `In Progress` | Mover ambas para `Done` |
| Milestone Sprint 2 | Associada | Associada | Preservar vínculo; não remover do milestone |
| Event Catalog | Entrega refletida | Producer refletido | Consistente |
| Architecture Overview | Contrato e fluxo refletidos | Publicação refletida | Consistente |
| Engineering Roadmap | Direção da Sprint registrada | Direção da Sprint registrada | Consistente; estado operacional é do GitHub |
| CHANGELOG / PROJECT_HISTORY | Catálogo e publicação registrados | Publicação registrada | Consistente |
| README da plataforma | Tópico e perfis documentados | Producer documentado | Consistente |

## Ações administrativas recomendadas

1. Fechar as Issues #31 e #32 com comentário de regularização e referência às evidências deste parecer.
2. Trocar os labels de estado para `status:done`.
3. Mover os dois cards do Project Board para `Done`.
4. Manter #31 e #32 no milestone Sprint 2 como entregas concluídas.
5. Como higiene de rastreabilidade, revisar os labels `status:review` ainda presentes nas Issues fechadas #30 e #37; os respectivos cards já estão em `Done`.

## Prontidão para a Story #35 e para o encerramento da Sprint

As regularizações de #31 e #32 tornam explícitas as dependências já entregues para o fluxo ponta a ponta. Portanto, **a Sprint 2 está tecnicamente apta a prosseguir para a Story #35**, desde que o replanejamento aprovado da Story #34 seja institucionalizado no GitHub e na documentação versionada.

Na data deste parecer, há alterações locais não versionadas no Sprint Plan e no Engineering Roadmap que registram o adiamento de #34 para a Sprint 3 e a mudança de dependência de #35 para #33. Essas alterações pertencem ao fluxo de replanejamento e não foram incorporadas por este parecer. Antes de iniciar ou encerrar #35, o responsável deve:

- publicar o replanejamento aprovado;
- remover #34 do escopo/milestone da Sprint 2 ou movê-la formalmente para a Sprint 3; e
- atualizar a Issue #35 para depender diretamente da #33, em alinhamento com o plano aprovado.

Enquanto #34 e #35 permanecerem abertas no milestone Sprint 2, a Sprint não está apta ao encerramento administrativo. Não foi encontrada divergência técnica que exija escalonamento ao Engineering Manager para #31 ou #32.
