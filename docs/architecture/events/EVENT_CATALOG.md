# Event Catalog

**Status:** Baseline de planejamento da Sprint 2
**Fonte de aprovação:** [Sprint 2 Product Plan](../../team/sprints/SPRINT_2_PRODUCT_PLAN.md)

## Escopo

O catálogo contém somente o primeiro fato de domínio aprovado para a Sprint 2. Ele não descreve uma implementação Kafka já existente e não autoriza novos eventos sem refinamento e aprovação.

## `OrderConfirmed` v1

| Campo | Definição |
| --- | --- |
| Nome do evento | `OrderConfirmed` |
| Descrição | Fato de domínio de que a confirmação comercial do pedido foi concluída. |
| Versão | `v1` |
| Producer / owner | Order Service |
| Consumer inicial | Inventory Service |
| Tópico planejado | `mercadoaurora.order.order-confirmed.v1` |
| Gatilho | Pedido confirmado após pagamento aprovado e estoque previamente reservado, conforme o fluxo vigente. |
| Propósito | Permitir processamento posterior rastreável sem nova chamada síncrona do Order. |
| Resultado esperado no consumidor | Resultado verificável e rastreável, definido no refinamento com Engenharia; não cria nem repete reserva de estoque. |
| Ownership | Order evolui a semântica do fato; Inventory é responsável pelo próprio resultado de processamento. |
| Observações | REST continua ativo. O catálogo não promete processamento exatamente uma vez nem define Schema Registry, retry ou DLT como implementação. |

### Payload mínimo de negócio planejado

| Campo lógico | Finalidade |
| --- | --- |
| Identificador do pedido | Correlacionar o fato ao pedido confirmado. |
| Identificador do evento | Identificar a ocorrência para rastreabilidade e proteção contra efeito duplicado. |
| Data/hora de ocorrência | Registrar quando o fato de domínio ocorreu. |
| Identificador de correlação | Relacionar publicação, consumo e evidências operacionais. |

Os nomes de serialização, tipos, envelope e campos adicionais serão definidos no refinamento técnico. Dados além do mínimo exigem necessidade comprovada.

## Limites do catálogo inicial

- Não há Payment Service externo; o `PaymentFakeAdapter` integra a baseline existente.
- Não há novos eventos de reserva, baixa ou liberação de estoque aprovados para esta Sprint.
- Não há Saga distribuída, API Gateway ou novos bounded contexts.
