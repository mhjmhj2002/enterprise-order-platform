# Event Catalog

**Status:** Implementado — producer entregue na Story #32; consumer de reconhecimento entregue na Story #33 e confiabilidade inicial de processamento entregue na Story #34
**Fonte de aprovação:** [Sprint 2 Product Plan](../../team/sprints/SPRINT_2_PRODUCT_PLAN.md) e [Event Platform Technical Contract](../contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md)

## Escopo

O catálogo contém somente o primeiro fato de domínio aprovado para a Sprint 2. A publicação pelo Order Service e o consumo de reconhecimento pelo Inventory Service estão implementados. O catálogo não autoriza novos eventos sem refinamento e aprovação.

## `OrderConfirmed` v1

| Campo | Definição |
| --- | --- |
| Nome do evento | `OrderConfirmed` |
| Descrição | Fato de domínio de que a confirmação comercial do pedido foi concluída. |
| Versão | `v1` |
| Producer / owner | Order Service |
| Consumer | Inventory Service — registra processamento idempotente por `eventId`, recupera localmente pendências temporárias e persiste a evidência de reconhecimento; não altera estoque. |
| Tópico oficial | `mercadoaurora.order.order-confirmed.v1` |
| Chave Kafka | `orderId`, serializado como string UUID. |
| Gatilho | Pedido confirmado após pagamento aprovado e estoque previamente reservado, conforme o fluxo vigente. |
| Propósito | Registrar assincronamente o fato de confirmação comercial, sem substituir a integração REST vigente. |
| Ownership | Order Service é owner da semântica e producer do evento. |
| Observações | A publicação usa `acks=all`. REST continua ativo; o consumidor não executa reserva, baixa ou outra regra comercial. A recuperação interna de pendências da Story #34 não muda o contrato do evento. |

### Envelope JSON v1 implementado

```json
{
  "eventId": "uuid",
  "eventType": "OrderConfirmed",
  "eventVersion": 1,
  "occurredAt": "2026-07-13T12:00:00Z",
  "correlationId": "uuid",
  "data": {
    "orderId": "uuid"
  }
}
```

O valor é JSON UTF-8. `eventId`, `correlationId` e `data.orderId` são UUIDs não nulos; `occurredAt` é um instante UTC em ISO-8601; `eventType` é `OrderConfirmed` e `eventVersion` é `1`. Alterações incompatíveis exigem nova versão maior do tópico e atualização prévia deste catálogo.

## Limites do catálogo inicial

- Não há Payment Service externo; o `PaymentFakeAdapter` integra a baseline existente.
- Não há novos eventos de reserva, baixa ou liberação de estoque aprovados para esta Sprint.
- Não há Saga distribuída, API Gateway ou novos bounded contexts.
- O consumidor do Inventory Service limita-se ao reconhecimento rastreável e idempotente do evento.
