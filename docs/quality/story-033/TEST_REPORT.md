# TEST REPORT — STORY #33

## Resultado

**Validation Completed.** A validação integrada com PostgreSQL, Kafka e Inventory em Java 21 comprovou o consumo, a evidência consultável, a idempotência, as rejeições contratuais, a recuperação sem DLT e a ausência de efeitos em estoque e reservas.

## Referência

| Item | Valor |
| --- | --- |
| Story | #33 — Inventory Consumer `OrderConfirmed` v1 |
| Data | 2026-07-14 (America/Sao_Paulo) |
| Serviço | Inventory Service |
| Contrato | [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) |
| Baseline Java para aprovação | Temurin 21.0.11 / Java 21, conforme `.sdkmanrc` |
| Docker/Testcontainers | daemon mínimo API 1.40; Testcontainers 1.20.1 solicita API 1.32 |

## Contagem

| Executados | Aprovados | Falhas | Bloqueados |
| ---: | ---: | ---: | ---: |
| 14 | 14 | 0 | 0 |

A limitação de ambiente Testcontainers–Docker permanece registrada: seis testes Testcontainers foram ignorados por incompatibilidade com a API Docker, mas a validação integrada equivalente foi executada no ambiente local preparado pela Engenharia.

## Cenários e evidências

| ID | Resultado | Evidência objetiva |
| --- | --- | --- |
| INV-033-001 | Aprovado | Inventory iniciou com profile `kafka`, Flyway v1/v2 aplicado, consumer inscrito em `mercadoaurora.inventory.v1`; `GET /actuator/health` retornou `{"status":"UP"}`. |
| INV-033-002 a INV-033-003 | Aprovado | Publicado `OrderConfirmed` válido; `GET /api/v1/inventory/order-confirmations/33333333-3333-3333-3333-333333333333` retornou HTTP 200 com os IDs, `occurredAt`, `recognizedAt`, tópico, partição 0 e offset 2. |
| INV-033-004 | Aprovado | Pedido `99999999-9999-9999-9999-999999999999` retornou `[]` com HTTP 200. |
| INV-033-005 | Aprovado | Duas publicações do mesmo `eventId=11111111-1111-1111-1111-111111111111` produziram uma única evidência; consulta e banco retornaram uma linha. |
| INV-033-006 | Aprovado | Após todos os eventos de teste: `inventory_items=0`, `inventory_reservations=0`, `order_confirmation_evidence=1`. |
| INV-033-007 a INV-033-010 | Aprovado | Publicados `eventType` inválido, `eventVersion=2`, e envelopes sem `eventId`, `correlationId` e `occurredAt`; nenhuma evidência adicional foi persistida. |
| INV-033-011 | Aprovado | Publicados envelopes sem `data` e sem `data.orderId`, além de `eventId` incompatível (`42`); contagem final de evidências permaneceu 1. |
| INV-033-012 | Aprovado | Com o `FixedBackOff` contratado (duas tentativas adicionais), os inválidos foram recuperados sem DLT; o grupo terminou em offset 12, log-end 12, lag 0. |
| INV-033-013 | Aprovado | Regressão dos fluxos de estoque executada com `mvn -Pkafka test`: criar, ajustar, reservar, confirmar/liberar e consultar estoque permaneceram aprovados na suíte; 25 testes, 0 falhas/erros. O profile Maven `kafka` é obrigatório porque os testes atuais carregam classes Kafka. |
| INV-033-014 | Aprovado | `mvn -Pkafka test` com Temurin 21.0.11: 25 testes, 0 falhas/erros, 6 ignorados pela limitação de ambiente Testcontainers–Docker. |

## Observações de aderência estática (não substituem execução)

- O consumer valida todos os campos mandatórios, `eventType=OrderConfirmed` e `eventVersion=1` antes de chamar o caso de uso.
- A persistência usa `ON CONFLICT (event_id) DO NOTHING`, compatível com idempotência por `eventId`.
- O caso de uso grava somente `order_confirmation_evidence`; não invoca portas de estoque, reserva ou pedido.
- O `DefaultErrorHandler(new FixedBackOff(1000L, 2L))` configura duas tentativas adicionais e não configura DLT.

Essas observações reduzem risco, mas não provam o comportamento em execução nem atendem aos critérios de aprovação.

## Limitações e bloqueios

| Referência | Classificação | Impacto |
| --- | --- | --- |
| Limitação de ambiente Testcontainers–Docker | Conhecida, não bloqueadora | Testcontainers 1.20.1 não é compatível com a API Docker disponível; seis testes foram ignorados. A cobertura integrada foi executada manualmente com a infraestrutura local. |
| Conectividade do PostgreSQL | Resolvida no ambiente | A Engenharia recriou o PostgreSQL com `localhost:5432`; Inventory conectou, migrou e iniciou. |

## Evidências reproduzíveis

```bash
cd services/inventory-service
mvn -Pkafka test
```

Com Temurin 21.0.11, `mvn -Pkafka test` encerra com `BUILD SUCCESS`: 25 testes, 0 falhas/erros e 6 ignorados pela limitação de ambiente Testcontainers–Docker. `mvn test` sem o profile não é uma regressão aplicável a esta Story, pois as classes Kafka dos testes só são disponibilizadas pelo profile Maven `kafka`. A execução integrada usou `mvn -Pkafka spring-boot:run -Dspring-boot.run.profiles=kafka`, Kafka em `localhost:9094`, PostgreSQL em `localhost:5432` e o grupo `mercadoaurora.inventory.v1`.

## Coleção Postman

Atualizada a coleção Inventory com a variável `orderId` e a requisição **Get Order Confirmation Evidence**, incluindo asserções de HTTP 200 e resposta em array. A requisição deve ser executada após publicação de evento válido; pedido inexistente também deve retornar array vazio.

## Issues abertas

Nenhuma issue ou defeito formal foi aberto pelo QE. A limitação Testcontainers–Docker e a conectividade do PostgreSQL foram registradas no contexto desta validação.

## Recomendação final

**APPROVED — Validation Completed.** O contrato funcional e técnico foi demonstrado com evidências reproduzíveis. Manter Java 21 conforme `.sdkmanrc` e manter registrada a limitação Testcontainers–Docker.
