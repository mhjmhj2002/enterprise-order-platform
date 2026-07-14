# TEST PLAN — STORY-016

## Status

**Preparado — não executado.** A execução depende de aprovação explícita deste plano e do handoff testável da Engenharia.

## Referência

| Item | Valor |
| --- | --- |
| Issue / Story | [#32 — Story-016](https://github.com/mhjmhj2002/enterprise-order-platform/issues/32), publicação do fato de pedido confirmado |
| Serviço sob teste | Order Service (producer) |
| Contrato | [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) |
| Evento | `OrderConfirmed` v1 |
| Tópico | `mercadoaurora.order.order-confirmed.v1` |

## Objetivo

Validar que o Order Service, somente com o profile Spring `kafka` ativo, publica uma única mensagem `OrderConfirmed` v1 depois de uma confirmação comercial concluída com sucesso. A publicação deve respeitar integralmente o contrato de tópico, chave e envelope JSON, receber confirmação do broker e não causar regressão no fluxo REST quando Kafka estiver desativado.

## Escopo

- Gatilho de publicação após estoque reservado, pagamento aprovado e pedido confirmado.
- Tópico, chave, serialização UTF-8 JSON e envelope `OrderConfirmed` v1.
- Confirmação técnica do broker (`acks=all`) e rastreabilidade operacional do producer.
- Ausência de evento em confirmações não concluídas.
- Ausência de duplicidade de publicação para uma confirmação bem-sucedida.
- Comportamento opt-in do profile `kafka` e regressão REST sem o profile.

## Fora de escopo

- Consumer, grupo de consumo, resultado no Inventory, idempotência de consumo e reprocessamento (Story #33).
- Retry, DLT/DLQ, Outbox, garantias de exatamente uma vez, novos tópicos/eventos e mudança de regras de negócio.
- Alteração de código, arquitetura, infraestrutura ou Collections durante o planejamento.

## Ambiente planejado

| Componente | Condição |
| --- | --- |
| Kafka | Plataforma da Story #37 saudável; broker externo em `localhost:9094`; tópico provisionado. |
| Order Service | Executado com `spring.profiles.active=kafka`, banco Order disponível e Inventory REST disponível para a confirmação. |
| Inventory Service | Disponível apenas para sustentar a reserva REST já existente; nenhum consumer Kafka é esperado. |
| Inspeção da mensagem | Consumer de teste efêmero/CLI Kafka ou ferramenta equivalente, configurado para capturar chave, valor, tópico, partição e offset sem alterar o consumer da aplicação. |
| Regressão sem Kafka | Order e Inventory disponíveis com profiles padrão, sem profile `kafka`. |

## Pré-condições de entrada

- Handoff da Engenharia com commit/PR informado e instruções de execução atualizadas.
- Story #37 aprovada e plataforma Kafka saudável.
- Banco do Order Service e Inventory REST acessíveis ao processo que inicia o Order.
- Massa apta a criar pedido, reservar estoque e concluir pagamento com `PaymentFakeAdapter`.
- Mecanismo de observação do tópico iniciado antes da confirmação, com offset/limite controlado.
- Logs estruturados do Order acessíveis e sem segredos.

## Massa de teste planejada

| Dado | Regra |
| --- | --- |
| `correlationId` | UUID novo por cenário, enviado em `X-Correlation-Id`. |
| Pedido válido | Pedido com estoque suficiente, reserva concluída e pagamento aprovado. |
| Pedido de falha | Pedido com confirmação não concluída por falha de pagamento e, quando viável, ausência de reserva. |
| `orderId` | UUID retornado pela criação do pedido; deve coincidir com chave Kafka e `data.orderId`. |

## Estratégia de validação

1. Confirmar saúde de Kafka, Order e Inventory e registrar offsets iniciais do tópico.
2. Executar o fluxo REST completo de confirmação com profile `kafka` e capturar a mensagem publicada.
3. Validar chave, encoding, tópico, envelope, tipos, UUIDs e instante UTC contra o contrato.
4. Cruzar a mensagem com resposta REST, `correlationId`, logs do producer, partição e offset.
5. Executar cenários negativos e verificar que não há mensagem adicional correspondente.
6. Repetir/observar o fluxo para provar uma publicação por confirmação; não reenviar comandos de confirmação fora das regras do domínio.
7. Reiniciar o Order sem profile `kafka` e executar a regressão REST, verificando a ausência de publicação Kafka.

## Cenários de teste planejados

| ID | Tipo | Cenário e critério esperado | Evidência planejada |
| --- | --- | --- | --- |
| ORDER-EVT-001 | Smoke | Com profile `kafka`, Order inicia e actuator evidencia conectividade Kafka; Kafka e Inventory estão saudáveis. | Status/health e configuração de execução sanitizada. |
| ORDER-EVT-002 | Happy path | Pedido com reserva e pagamento aprovado é confirmado; exatamente uma mensagem é publicada no tópico aprovado. | Roteiro REST, offset inicial/final, registro de mensagem e logs do producer. |
| ORDER-EVT-003 | Tópico e chave | Mensagem de ORDER-EVT-002 usa exclusivamente `mercadoaurora.order.order-confirmed.v1` e chave string UUID igual ao `orderId`. | Captura com chave/tópico/partição/offset e comparação com resposta REST. |
| ORDER-EVT-004 | Envelope e serialização | Valor é JSON UTF-8 Jackson válido, com `eventId`, `correlationId` e `data.orderId` UUIDs não nulos; `eventType=OrderConfirmed`, `eventVersion=1` e `occurredAt` ISO-8601 UTC. | Payload sanitizado, validação de JSON/tipos e correlação com a requisição. |
| ORDER-EVT-005 | Confirmação e logs | Producer usa confirmação técnica compatível com `acks=all`; logs INFO permitem rastrear eventId, correlationId, orderId, tópico, partição e offset sem payload completo. | Configuração/telemetria do producer e trecho de log sanitizado. |
| ORDER-EVT-006 | Duplicidade | Para uma única confirmação bem-sucedida, há uma única mensagem associada ao `orderId`/`correlationId`; repetição de observação não revela publicação adicional. | Contagem de mensagens no intervalo/offsets e logs correlacionados. |
| ORDER-EVT-007 | Negativo — pagamento | Confirmação não concluída por pagamento recusado não publica `OrderConfirmed`. | Roteiro REST, estado final e ausência de mensagem após offset de controle. |
| ORDER-EVT-008 | Negativo — pré-condição | Fluxo sem reserva válida não confirma pedido e não publica `OrderConfirmed`. | Roteiro REST, erro/estado e ausência de mensagem. |
| ORDER-EVT-009 | Negativo — broker | Broker indisponível torna a falha de publicação diagnosticável por health/logs, sem payload completo. O comportamento funcional observado deve ser comparado ao contrato/handoff, sem inferir retry ou DLT fora de escopo. | Health, logs WARN/ERROR e resposta/estado do pedido. |
| ORDER-EVT-010 | Regressão REST | Sem profile `kafka`, criação, reserva, pagamento e confirmação REST permanecem funcionais e não há publicação no tópico. | Roteiro Postman/HTTP, estado do pedido e offsets sem nova mensagem. |
| ORDER-EVT-011 | Regressão automatizada | Suítes existentes do Order e Inventory passam no ambiente entregue; resultados ignorados/bloqueados são registrados com causa. | Saídas Maven/Surefire e versões de runtime. |

## Critérios de aceite

- ORDER-EVT-001 a ORDER-EVT-008 e ORDER-EVT-010 aprovados; ORDER-EVT-009 é executado conforme segurança do ambiente e seu resultado fica documentado.
- Uma confirmação válida produz uma única mensagem `OrderConfirmed` v1 no tópico aprovado, com chave e envelope integralmente aderentes ao contrato.
- Eventos não são publicados quando a confirmação não é concluída.
- A publicação é confirmada pelo broker e rastreável sem exposição do payload completo em logs.
- Sem profile `kafka`, os fluxos REST de Sprint 1 continuam funcionais e não publicam Kafka.
- Nenhum defeito Critical ou High aberto relacionado ao producer.

## Estratégia de evidências

- Identificar cada execução com data/hora, commit/PR, ambiente, `correlationId`, `orderId`, offset inicial/final e ID de cenário.
- Preservar apenas payload de teste e logs sanitizados; nunca credenciais, tokens ou dados comerciais reais.
- Registrar chave, valor, tópico, partição e offset da mensagem, além do resultado HTTP e do estado final do pedido.
- Associar ausência de publicação a uma janela de observação e offsets documentados, evitando concluir ausência somente pela Kafka UI.
- Atualizar Collection/Environment Postman somente se o handoff expuser um roteiro repetível que precise ser preservado; qualquer alteração será registrada no relatório.

## Riscos e bloqueios previstos

- Indisponibilidade de banco, Inventory REST ou plataforma Kafka bloqueia o fluxo de confirmação ponta a ponta.
- Consumer de inspeção iniciado após a publicação pode perder a prova; offsets devem ser registrados antes do gatilho.
- Sem requisito aprovado de retry/DLT, o cenário de broker indisponível não pode ser usado para exigir comportamento não contratado.
- Reexecução indevida de endpoints pode alterar estado do pedido; cada cenário usará massa e `correlationId` próprios.

## Critérios de saída

- Resultados dos cenários e evidências registrados em `TEST_REPORT.md`.
- Defeitos encontrados reportados com severidade, passos, esperado, obtido e evidência.
- Recomendação do QE limitada a `Validation Completed` ou `Validation Rejected`, após a execução autorizada.
