# TEST PLAN — STORY #33

## Objetivo

Validar o Inventory Consumer de `OrderConfirmed` v1 contra o contrato funcional e o [contrato técnico da plataforma](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md), incluindo consulta de evidência, idempotência, ausência de efeitos de estoque, rejeições, desserialização, retry e regressão.

## Escopo

- Inventory Service com perfil Spring/Maven `kafka`.
- Tópico `mercadoaurora.order.order-confirmed.v1` e grupo `mercadoaurora.inventory.v1`.
- Endpoint `GET /api/v1/inventory/order-confirmations/{orderId}`.
- Persistência da evidência de reconhecimento e fluxos REST de estoque existentes.

## Serviços envolvidos

- Kafka local da Story #37 — plataforma local de eventos aprovada para a Sprint 2.
- Inventory Service e PostgreSQL.
- Order Service somente como produtor opcional do evento válido; payloads negativos podem ser publicados pelo cliente Kafka.

## Ambiente e pré-condições

- Java 21 compatível com Spring Boot 3.3.2 e Mockito/Byte Buddy.
- Docker/Testcontainers compatíveis; Kafka disponível em `localhost:9094`; banco Inventory acessível.
- Profile `kafka` ativo, tópico provisionado e portas necessárias livres.
- Coleção `docs/api/postman/inventory-service.postman_collection.json` importada, com `baseUrl` e `orderId` configurados.

## Cenários

| ID | Cenário | Resultado esperado |
| --- | --- | --- |
| INV-033-001 | Smoke do serviço com perfil `kafka` e health | Serviço inicia; health inclui conectividade Kafka. |
| INV-033-002 | Consumir envelope válido | Consumer executa o caso de uso e conclui sem erro inesperado. |
| INV-033-003 | Consultar a evidência após consumo | HTTP 200 contém `orderId`, `eventId`, `correlationId`, `occurredAt` e `recognizedAt`. |
| INV-033-004 | Consultar pedido inexistente | HTTP 200 e array vazio. |
| INV-033-005 | Reentregar o mesmo `eventId` | Apenas uma evidência funcional persiste; segunda entrega é ignorada. |
| INV-033-006 | Ausência de efeitos colaterais | Estoque físico, reservado, disponível, reservas e pedidos permanecem inalterados. |
| INV-033-007 | `eventType` diferente de `OrderConfirmed` | Rejeitado antes do caso de uso; sem evidência. |
| INV-033-008 | `eventVersion` diferente de `1` | Rejeitado antes do caso de uso; sem evidência. |
| INV-033-009 | `eventId`, `correlationId` ou `occurredAt` ausente | Cada variação é rejeitada; sem evidência. |
| INV-033-010 | `data` ou `data.orderId` ausente | Cada variação é rejeitada; sem evidência. |
| INV-033-011 | JSON/tipos incompatíveis | Desserialização não alcança o caso de uso e não persiste evidência. |
| INV-033-012 | Retry de falha processável | Uma entrega inicial mais duas tentativas adicionais; após esgotamento, sem DLT e com mensagem diagnosticável. |
| INV-033-013 | Regressão dos fluxos de estoque na suíte Inventory | Executar `mvn -Pkafka test`; criar, ajustar, reservar, confirmar/liberar e consultar estoque permanecem aprovados. O profile Maven `kafka` é obrigatório porque os testes atuais carregam classes Kafka. |
| INV-033-014 | Regressão completa com perfil Kafka | `mvn -Pkafka test` executa sem falhas, incluindo os testes do consumer. |

## Critérios de aceite

- INV-033-001 a INV-033-014 aprovados, sem falhas Critical ou High.
- Eventos inválidos e payloads incompatíveis não deixam evidência.
- A única alteração funcional persistida por evento válido é a evidência idempotente por `eventId`.
- Não existe DLT nesta Sprint; retry observado é limitado a duas tentativas adicionais.

## Riscos conhecidos

- Limitação de ambiente Testcontainers–Docker: Testcontainers 1.20.1 não negocia com o Docker do ambiente atual (API mínima 1.40), inviabilizando integrações locais.
- Conectividade do PostgreSQL: health runtime fora de containers depende de conectividade do processo Spring ao banco Inventory.
- O perfil Maven `kafka` é obrigatório para disponibilizar dependências Kafka aos testes atuais.

## Fora de escopo

- DLT, novos tópicos/eventos, alteração de reserva/estoque, Saga, Schema Registry e regras de negócio adicionais.
