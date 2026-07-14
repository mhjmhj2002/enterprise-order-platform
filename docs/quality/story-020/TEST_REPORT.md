# TEST REPORT — STORY-020

## Resultado

**Validation Completed** para a infraestrutura local da plataforma de eventos. Os cenários de infraestrutura `EVT-INF-001` a `EVT-INF-010` foram executados em 2026-07-14 e aprovados. Os cenários de publicação/consumo continuam fora do escopo desta Story e dependem das Stories posteriores.

## Referência

| Item | Valor |
| --- | --- |
| Story | #37 — Story-020, Ambiente local para demonstração de eventos |
| Branch | `feature/story-020-event-platform-infrastructure` |
| Commit validado | `940e2aa` |
| Contrato | [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) |
| Data | 2026-07-14 (America/Sao_Paulo) |
| Docker | 29.3.1 |
| Docker Compose | v5.1.1 |

## Serviços validados

- Kafka `apache/kafka:3.9.0`, KRaft em nó único.
- Kafka UI `provectuslabs/kafka-ui:v0.7.2`.
- Configuração opt-in Kafka de Order Service e Inventory Service.

## Cenários executados

| ID | Resultado | Evidência / resultado obtido |
| --- | --- | --- |
| EVT-INF-001 | Aprovado | `docker compose ... up -d --wait` deixou Kafka e Kafka UI saudáveis; nenhum ZooKeeper foi iniciado. |
| EVT-INF-002 | Aprovado | Configuração efetiva confirmou Kafka 3.9.0, `broker,controller`, KRaft, quorum `1@kafka:9093`, cluster ID estável e sem ZooKeeper. |
| EVT-INF-003 | Aprovado | CLI interna respondeu por `kafka:9092`; cliente externo respondeu em `localhost:9094`; somente 9094 foi publicado para o broker. |
| EVT-INF-004 | Aprovado | Kafka UI respondeu `{"status":"UP"}` em `http://localhost:8080/actuator/health`; parada controlada da UI não interrompeu Kafka. |
| EVT-INF-005 | Aprovado | Rede bridge `event-platform` continha somente `event-platform-kafka-1` e `event-platform-kafka-ui-1`; 9093 não foi publicado. |
| EVT-INF-006 | Aprovado | Provisionamento criou somente `mercadoaurora.order.order-confirmed.v1`; subidas repetidas foram idempotentes. |
| EVT-INF-007 | Aprovado | Descrição do tópico: 1 partição, RF=1, `cleanup.policy=delete`, `retention.ms=604800000` (7 dias). |
| EVT-INF-008 | Aprovado | Após `restart kafka`, o volume nomeado `kafka-data`, o ID do tópico e suas propriedades permaneceram disponíveis. |
| EVT-INF-009 | Aprovado | Publicação no tópico não autorizado `mercadoaurora.qa.unauthorized.v1` retornou `UNKNOWN_TOPIC_OR_PARTITION`; após a tentativa, a listagem continha somente o tópico aprovado. |
| EVT-INF-010 | Aprovado | Ao parar Kafka, a UI registrou `Broker may not be available`; após a inicialização, broker, UI, health e tópico voltaram a responder. |

## Profiles Spring e regressão

| Verificação | Resultado |
| --- | --- |
| Order com `mvn -Pkafka test` | Aprovado: 26 testes, 0 falhas, 3 ignorados. |
| Inventory com `mvn -Pkafka test` | Aprovado: 22 testes, 0 falhas, 6 ignorados. |
| Configuração dos profiles | Aprovado por inspeção: ativação somente no profile Spring `kafka`, bootstrap externalizável e consumer group padrão `mercadoaurora.inventory.v1`. |
| Health runtime de Order/Inventory com profile `kafka` | Não executado: os bancos `order` e `inventory` existem no container `eop-postgres`, mas não estão acessíveis ao processo Spring executado no host. Nenhuma configuração foi alterada pelo QE para contornar essa limitação. |

## Limitações conhecidas

- A primeira execução encontrou containers da plataforma já existentes havia aproximadamente 15 horas. Foram executadas reinicialização controlada, nova subida com `--wait` e verificação de persistência; não foi removido o volume preexistente para preservar dados locais.

## Pendências rastreáveis

| ID | Pendência | Impacto | Encaminhamento |
| --- | --- | --- | --- |
| PEND-001 | Testcontainers 1.20.1 usa API Docker 1.32, inferior ao mínimo 1.40 do daemon do ambiente. | As integrações Maven foram ignoradas; testes unitários e de API restantes passaram. | Atualizar ou alinhar a compatibilidade Testcontainers–Docker antes da regressão integrada das Stories subsequentes. |
| PEND-002 | Os bancos `order` e `inventory` existem em `eop-postgres`, mas não estão acessíveis ao processo Spring executado no host. | Não foi possível executar o health runtime de Order/Inventory com o profile `kafka`. | Disponibilizar conectividade de banco ao ambiente que executará os serviços antes da validação integrada de publicação/consumo. |

## Defeitos abertos

Nenhum defeito Critical ou High identificado no escopo da infraestrutura.

## Evidências reproduzíveis

```bash
docker compose -f infra/event-platform/compose.yaml up -d --wait
docker compose -f infra/event-platform/compose.yaml ps
docker compose -f infra/event-platform/compose.yaml exec -T kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --describe --topic mercadoaurora.order.order-confirmed.v1
docker compose -f infra/event-platform/compose.yaml exec -T kafka /opt/kafka/bin/kafka-configs.sh --bootstrap-server kafka:9092 --entity-type topics --entity-name mercadoaurora.order.order-confirmed.v1 --describe
curl --fail --silent --show-error http://localhost:8080/actuator/health
cd services/order-service && mvn -Pkafka test
cd services/inventory-service && mvn -Pkafka test
```

## Recomendação

Infraestrutura local apta para as Stories subsequentes. Antes da validação integrada de publicação/consumo, disponibilizar os bancos Order e Inventory ao processo que iniciará os serviços e atualizar a compatibilidade Testcontainers–Docker para executar as integrações não ignoradas.
