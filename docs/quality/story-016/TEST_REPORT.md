# TEST REPORT — STORY-016

## Resultado

**Validation Completed.** A divergência inicial de serialização foi corrigida e retestada no commit `1cd21d9`. O producer publicou uma única mensagem `OrderConfirmed` v1 com chave, tópico, versão, correlação e instante UTC aderentes ao contrato; os negativos aplicáveis e a regressão REST também foram aprovados.

## Referência

| Item | Valor |
| --- | --- |
| Issue / Story | [#32 — Story-016](https://github.com/mhjmhj2002/enterprise-order-platform/issues/32) |
| Commits validados | `5341ee7 feat(order): publish OrderConfirmed event`; `1cd21d9 fix(order): serialize event timestamps as ISO-8601` |
| Contrato | [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) |
| Data | 2026-07-14 (America/Sao_Paulo) |
| Ambiente | Kafka/Kafka UI, Order e Inventory saudáveis; Order reiniciado pelo ES com profile `kafka` confirmado. |

## Execução inicial

| Cenário | Resultado | Evidência |
| --- | --- | --- |
| ORDER-EVT-001 — Smoke | Aprovado | Kafka UI, Inventory e Order retornaram `{"status":"UP"}`; tópico existente em offset 0 antes do reteste. |
| ORDER-EVT-002 — Publicação | Aprovado | Uma confirmação válida moveu o offset do tópico de 0 para 1 e produziu uma única mensagem. |
| ORDER-EVT-003 — Tópico e chave | Aprovado | Tópico `mercadoaurora.order.order-confirmed.v1`; chave `94879ddc-4b8d-4b39-81a8-04fdbccbe2b1`, igual ao `orderId`. |
| ORDER-EVT-004 — Envelope e serialização | Falhou | `occurredAt` publicado como `1784039269.076152145`, em vez de ISO-8601 UTC. Demais campos observados aderentes: `eventId`, `eventType=OrderConfirmed`, `eventVersion=1`, `correlationId` e `data.orderId`. |
| ORDER-EVT-005 a ORDER-EVT-011 | Não executados | Regra de parada aplicada: contrato de API/evento diverge da documentação aprovada. |

## Reteste após correção

| Cenário | Resultado | Evidência |
| --- | --- | --- |
| ORDER-EVT-001 — Smoke | Aprovado | Kafka UI, Inventory e Order retornaram `{"status":"UP"}` antes do reteste. |
| ORDER-EVT-002 / ORDER-EVT-006 — Publicação única | Aprovado | Offset `1 → 2` para uma única confirmação; somente uma mensagem foi capturada a partir do offset inicial. |
| ORDER-EVT-003 — Tópico e chave | Aprovado | Tópico aprovado; chave `f7dd3924-158c-4369-96bf-91cc223b44d3` igual ao `orderId`. |
| ORDER-EVT-004 — Envelope e serialização | Aprovado | `occurredAt` publicado como `2026-07-14T14:44:06.672941486Z`; JSON contém `eventId`, `eventType=OrderConfirmed`, `eventVersion=1`, `correlationId` e `data.orderId` corretos. |
| ORDER-EVT-005 — Confirmação e logs | Aprovado | Configuração efetiva usa `acks=all`, `StringSerializer` e `JsonSerializer`; a suíte do publisher passou e registra evento, correlação, pedido, tópico, partição e offset sem payload completo. |
| ORDER-EVT-007 — Pagamento recusado | Aprovado | HTTP 503; offset permaneceu `2 → 2`, sem evento indevido. |
| ORDER-EVT-006 — Confirmação repetida | Aprovado | HTTP 409 para confirmação repetida; offset permaneceu `2 → 2`. |
| ORDER-EVT-008 — Sem reserva válida | Aprovado | Tentativa de iniciar pagamento sem reserva retornou HTTP 409; offset permaneceu `2 → 2`, sem evento indevido. |
| ORDER-EVT-009 — Broker indisponível | Não executado | Comportamento de negócio para falha de broker/retry não é definido pela Story; não foi induzida indisponibilidade adicional após a recuperação da plataforma. |
| ORDER-EVT-010 — Regressão REST sem Kafka | Aprovado | Order reiniciado sem profile Kafka: confirmação retornou `CONFIRMED` e offset permaneceu `2 → 2`. |
| ORDER-EVT-011 — Regressão automatizada | Aprovado com limitação | Order: 28 testes, 0 falhas/erros, 3 ignorados. Inventory: 22 testes, 0 falhas/erros, 6 ignorados. Integrações ignoradas pela incompatibilidade Testcontainers/Docker conhecida. |

## Bug report

| Campo | Registro |
| --- | --- |
| ID | QE-032-001 |
| Resumo | `OrderConfirmed` v1 serializa `occurredAt` como timestamp numérico, não ISO-8601 UTC. |
| Ambiente | Plataforma Kafka local saudável; Order com profile `kafka`; commit `5341ee7`. |
| Passos para reproduzir | 1. Iniciar Kafka, Inventory e Order com profile `kafka`. 2. Criar item de estoque. 3. Criar pedido, reservar, iniciar pagamento, marcar pago e confirmar. 4. Ler a mensagem no tópico aprovado a partir do offset inicial. |
| Esperado | `occurredAt` é instantâneo UTC em formato ISO-8601, conforme contrato v1. |
| Obtido | `occurredAt: 1784039269.076152145` no valor JSON publicado. |
| Evidência | `94879ddc-4b8d-4b39-81a8-04fdbccbe2b1|{"eventId":"c3259604-8f2c-40b8-a4da-f724e7ca6929","eventType":"OrderConfirmed","eventVersion":1,"occurredAt":1784039269.076152145,"correlationId":"4d88122a-ee0f-4b0f-a71b-cd921a3821dd","data":{"orderId":"94879ddc-4b8d-4b39-81a8-04fdbccbe2b1"}}` |
| Severidade | High — viola contrato de integração obrigatório e pode quebrar consumidores que esperam o formato ISO-8601. |
| Impacto | Story não atende integralmente o envelope `OrderConfirmed` v1 aprovado. |

## Resolução do bug

`QE-032-001` foi corrigido no commit `1cd21d9` e retestado com sucesso. Não há defeitos Critical ou High abertos no escopo da Story.

## Observação de ambiente

Uma tentativa anterior ocorreu com Order sem evidência do profile `kafka`: o pedido confirmou e o offset permaneceu em 0. Após o ES reiniciar o serviço com profile confirmado, o reteste acima publicou uma única mensagem; este é o resultado considerado para a validação do producer.

## Recomendação

Recomendação favorável para o escopo da Story #32. As integrações Maven ignoradas permanecem como limitação de ambiente já conhecida e não bloqueante; consumer, idempotência de consumo e processamento no Inventory continuam fora de escopo.
