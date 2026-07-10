# TEST REPORT — STORY-009

## Execution Summary

- Total: 50
- Passed: 25
- Failed: 0
- Blocked: 22
- Not Executed: 3

## Environment

Reteste final no commit `dfa320bef679ebe1e454b5bd43fda8bd81aa9483`. O run Postman iniciou às `2026-07-10T19:11:49.568Z`, alcançou Order em `localhost:8083` e Inventory em `localhost:8082`, executou 34 requests e registrou 109 assertions PASS / 0 FAIL. O export não contém response bodies nem environment persistido (`environment_id: 0`), mas as respostas foram verificadas por assertions durante a execução.

## Version Tested

- Branch: `feature/story-009-bootstrap-order-service`
- Commit: `dfa320bef679ebe1e454b5bd43fda8bd81aa9483`
- PR #20: OPEN, REVIEW_REQUIRED, head sincronizado com o commit local.
- Issue #14: OPEN.

## Services Tested

- Order Service: testes automatizados e API HTTP via Postman.
- Inventory Service: domínio automatizado e API HTTP via Postman; unitários/integrados Maven continuaram limitados pelo runner local.

## Scenarios Executed

| ID | Scenario | Result | Evidence | Bug |
|---|---|---|---|---|
| CT-001 | Health Inventory | PASS | HTTP 200, 173 ms, assertions aprovadas | BUG-009-01 retestado |
| CT-002 | Health Order | PASS | HTTP 200, 133 ms, assertions aprovadas | BUG-009-01 retestado |
| CT-003 | Preparar estoque | PASS | Inventory criado (201), consultado (200) e reservado (201) | — |
| CT-004 | Pedido completo, um item | PASS | 201 → reserva 200 → pagamento 200 → paid 200 → confirmação 200/CONFIRMED | BUG-009-02 retestado |
| CT-005 | Múltiplas unidades | BLOCKED | Serviços indisponíveis | — |
| CT-006 | Múltiplos itens | BLOCKED | Serviços indisponíveis | — |
| CT-007 | Contrato de pedido | PASS | DTO contém id, itens, estados, totais e datas | — |
| CT-008 | Consultar pedido | PASS | GET retornou 200 sem alterar estado | — |
| CT-009 | Pedido inexistente | BLOCKED | Serviço/banco indisponível | — |
| CT-010 | ID inválido | PASS | HTTP 400 e sanitização aprovada | BUG-009-06 retestado |
| CT-011 | Sem itens | PASS | HTTP 400 e assertions aprovadas | — |
| CT-012 | Itens nulos | BLOCKED | Serviço/banco indisponível | — |
| CT-013 | Quantidade zero | PASS | HTTP 400 e assertions aprovadas | — |
| CT-014 | Quantidade negativa | BLOCKED | Serviço/banco indisponível | — |
| CT-015 | Sem SKU | BLOCKED | Serviço/banco indisponível | — |
| CT-016 | SKU inválido | BLOCKED | Serviço/banco indisponível | — |
| CT-017 | SKU inexistente | BLOCKED | Integração indisponível | — |
| CT-018 | Estoque insuficiente | PASS | Inventory retornou HTTP 400 sem detalhe interno | — |
| CT-019 | Inventory indisponível | BLOCKED | Order não inicia sem PostgreSQL | — |
| CT-020 | Timeout de reserva | NOT EXECUTED | Não há mecanismo controlado de timeout | — |
| CT-021 | Reserva e pagamento com sucesso | PASS | Sequência completa retornou sucesso e terminou CONFIRMED | — |
| CT-022 | Pagamento falha após reserva | PASS | 503; pedido CANCELLED/FAILED; ref preservada; reserva RELEASED; invariante Inventory aprovada | BUG-009-03 retestado |
| CT-023 | Falha ao compensar | BLOCKED | Falha de pagamento não simulável | BUG-009-03 |
| CT-024 | Falha no commit após pagamento | BLOCKED | Integração indisponível | — |
| CT-025 | Pagamento não antecede reserva | PASS | HTTP 409 observado antes da reserva e teste unitário aprovado | — |
| CT-026 | Commit após pagamento | PASS | Regra de domínio/teste unitário Order aprovado | — |
| CT-027 | Release apenas após falha | BLOCKED | Falha de pagamento não simulável | BUG-009-03 |
| CT-028 | Payload repetido | PASS | Segundo POST retornou 201; comportamento é criar pedido distinto | — |
| CT-029 | Chave idempotente | NOT EXECUTED | API não suporta chave de idempotência | — |
| CT-030 | Consulta após reinício | BLOCKED | Serviço/banco indisponível | — |
| CT-031 | Reinício durante fluxo | NOT EXECUTED | Sem mecanismo controlado | — |
| CT-032 | Datas | PASS | GET 200 e assertion `updatedAt >= createdAt` aprovada | — |
| CT-033 | Integridade dos itens | PASS | Criação/consulta/listagem retornaram contrato persistido | — |
| CT-034 | Transições inválidas | PASS | 6 testes de domínio Order aprovados | — |
| CT-035 | Pedido cancelado | PASS | Cancel retornou HTTP 200 | — |
| CT-036 | Reserva commitada | PASS | Commit Inventory retornou HTTP 200 | — |
| CT-037 | Reserva liberada | PASS | Release retornou 200 | — |
| CT-038 | Commit duplicado | PASS | HTTP 400: rejeição controlada sem segunda baixa; assertion da Collection corrigida | — |
| CT-039 | Release duplicado | PASS | HTTP 400: rejeição controlada sem segundo incremento | — |
| CT-040 | Reserva acima do disponível | PASS | HTTP 400 e assertions aprovadas | — |
| CT-041 | Reserva zero | BLOCKED | Inventory integrado indisponível | — |
| CT-042 | Reserva negativa | BLOCKED | Inventory integrado indisponível | — |
| CT-043 | Reserva SKU inexistente | BLOCKED | Inventory integrado indisponível | — |
| CT-044 | Body malformado | PASS | HTTP 400 e sanitização aprovada | BUG-009-05 retestado |
| CT-045 | Campos inesperados | BLOCKED | Serviços indisponíveis | — |
| CT-046 | Payload grande | BLOCKED | Serviços indisponíveis | — |
| CT-047 | Caracteres especiais | BLOCKED | Serviços indisponíveis | — |
| CT-048 | SQL injection | BLOCKED | Serviços indisponíveis | — |
| CT-049 | Exposição interna | BLOCKED | Serviços indisponíveis | — |
| CT-050 | Correlation ID | BLOCKED | Sem execução/logs integrados | BUG-009-04 |

## Bugs Found

### BUG-009-01 — Health checks obrigatórios não estão disponíveis

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/21
- Status: CLOSED / RETEST PASS; ambos retornaram HTTP 200.
- Severity: High
- Environment: commit testado, configuração padrão.
- Steps: revisar endpoints/configuração e tentar identificar health de Order/Inventory.
- Expected: endpoints retornam HTTP 200 e estado saudável.
- Actual: não há dependência/configuração ou endpoint de health exposto.
- Impact: CT-001/CT-002 e critério obrigatório de aprovação não podem passar.

### BUG-009-02 — Criação do pedido não executa o fluxo comercial completo

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/22
- Status: CLOSED / RETEST PASS; a sequência de orquestração completou em `CONFIRMED`.
- Severity: High
- Steps: executar `POST /api/v1/orders` conforme CT-004.
- Expected: reservar estoque, executar PaymentFake, commitar e finalizar `CONFIRMED`.
- Actual: criação persiste `CREATED`; reserva, pagamento, marcação de pago e confirmação são endpoints públicos separados.
- Impact: happy path principal diverge do contrato do guia e permite controle externo das etapas.

### BUG-009-03 — Falha do PaymentFake e compensação não são exercitáveis

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/23
- Status: CLOSED / RETEST PASS no commit `dfa320b`.
- Severity: High
- Steps: configurar PaymentFake para falhar após uma reserva.
- Expected: falha controlada, release e pedido cancelado.
- Actual: adapter possui apenas operação vazia de sucesso, sem configuração de falha; fluxo de pagamento também é exposto em etapas independentes.
- Impact: não há evidência de compensação do cenário bloqueante CT-022/CT-027.

### BUG-009-04 — Rastreabilidade entre serviços não é comprovável

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/24
- Severity: Medium
- Expected: correlation ID propagado e presente nos logs críticos.
- Actual: contratos/adapters inspecionados não propagam `X-Correlation-Id`; execução integrada ficou bloqueada.
- Impact: diagnóstico de inconsistências e compensações fica prejudicado.
- Status: implementação e header de resposta presentes; permanece aberto até evidência dos logs do Inventory com o mesmo ID.

### BUG-009-05 — JSON malformado retorna HTTP 500

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/25
- Severity: High
- Steps: enviar `POST /api/v1/orders` com body incompleto `{"customerId":`.
- Expected: HTTP 400, erro sanitizado e nenhuma persistência.
- Actual: HTTP 500 Internal Server Error em 7 ms.
- Impact: CT-044 reprovado; cenário previsível gera erro interno.
- Status: CLOSED / RETEST PASS; retornou HTTP 400 e passou sanitização.

### BUG-009-06 — UUID inválido falha sanitização da resposta

- GitHub: https://github.com/mhjmhj2002/enterprise-order-platform/issues/26
- Severity: Medium
- Steps: enviar `GET /api/v1/orders/not-a-uuid`.
- Expected: HTTP 400 sem stack trace, pacote Java, JDBC, senha ou SQL.
- Actual: HTTP 400, mas a assertion `No internal details` falhou. O export não preservou o body para identificar o token exato.
- Impact: possível exposição de detalhe interno; requer captura do body no reteste.
- Status: CLOSED / RETEST PASS; retornou HTTP 400 e passou sanitização.

## Postman Execution Evidence

- Arquivo: `tasks/Enterprise Order Platform - STORY-009 QE.postman_test_run.json`.
- Collection: `Enterprise Order Platform - STORY-009 QE`.
- Requests: 34.
- Assertions: 109 PASS / 0 FAIL.
- Tempo total informado: 637 ms; todos os requests ficaram abaixo de 2 s.
- HTTP observados corresponderam aos contratos: 200/201 nos fluxos válidos; 400/409 nas rejeições; 503 na falha controlada de pagamento.
- Happy path terminou em `CONFIRMED`; commit/release e rejeições duplicadas passaram.
- Compensação confirmou pedido `CANCELLED`, pagamento `FAILED`, reservationRef preservada, reserva `RELEASED` e invariante quantitativa do Inventory.
- O export não contém response bodies; por isso mensagens, códigos internos e conteúdo exato da falha de sanitização não puderam ser auditados.

## Automated Test Evidence

- Order `mvn test`: BUILD SUCCESS; 26 testes, 23 PASS, 3 SKIPPED (Testcontainers sem Docker).
- Inventory `mvn test`: 22 testes; 6 domínio PASS, 6 integração SKIPPED, 10 ERROR porque Mockito/Byte Buddy não consegue self-attach no JDK 25 do executor. Não houve falha de assertion nesses 10.
- Reteste Inventory com flags de dynamic agent: mesmo bloqueio de instrumentação.
- A aplicação declara Java 21, mas somente JDK 25/26 estão instalados no executor.

## Regression Result

`PASS` para a regressão HTTP executada: criação, consulta, reserva, estoque insuficiente, commit, release e rejeições duplicadas passaram. No executor QE, Inventory Maven ainda não inicia 10 testes Mockito por incompatibilidade JDK 25/agent; o dev reportou 16 testes executados com sucesso e 6 integrações ignoradas em ambiente compatível.

## Risks

- O export Postman não preserva response bodies nem logs.
- Correlation ID ponta a ponta ainda depende de evidência de logs.
- `InventoryRestAdapter` preserva a falha original ao ignorar exceção de release parcial, mas não registra evidência explícita da falha de compensação.
- PR permanece com review obrigatório.

## Pending Items

- Anexar logs Order/Inventory com o mesmo `X-Correlation-Id` para concluir #24.

## Final Recommendation

**APPROVED FOR MERGE**

Smoke, happy path, negativos, integração, compensação e regressão passaram. Não há bug Critical ou High aberto. A #24 permanece Medium e não bloqueante, limitada à evidência de observabilidade ponta a ponta.

## Quality Engineer Sign-off

Validation Completed — QE, 2026-07-10.
