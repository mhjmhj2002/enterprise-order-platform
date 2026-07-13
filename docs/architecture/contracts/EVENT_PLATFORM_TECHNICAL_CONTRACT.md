# Event Platform Technical Contract

**Status:** APPROVED — READY FOR SOFTWARE ENGINEERING

**Escopo:** Sprint 2, ambiente local de desenvolvimento e demonstração
**Data:** 2026-07-13

## 1. Decisão e limites

Este contrato define a plataforma compartilhada necessária para publicar e consumir exclusivamente o evento [`OrderConfirmed` v1](../events/EVENT_CATALOG.md#orderconfirmed-v1). Ele complementa o [Context Map](../CONTEXT_MAP.md) e as [convenções de eventos](../events/EVENT_CONVENTIONS.md).

- Order Service é o producer e owner da semântica do evento.
- Inventory Service é o único consumer inicial e é responsável somente pelo próprio resultado de processamento.
- A integração REST de Order para Inventory continua ativa; Kafka não cria, confirma, libera ou repete reservas nesta Sprint.
- Catalog, Payment Service externo, Saga, API Gateway e Kubernetes estão fora de escopo.
- Este contrato não autoriza novos tópicos, eventos ou regras de negócio.

## 2. Plataforma local

| Item | Decisão contratada |
| --- | --- |
| Distribuição e versão | Apache Kafka 3.9.0, imagem `apache/kafka:3.9.0`, com tag fixa. |
| Metadados | KRaft em nó único; ZooKeeper não é usado. |
| Orquestração | Docker Compose Specification, em arquivo dedicado `infra/event-platform/compose.yaml` a ser criado pela Engenharia. |
| Broker | Um container `kafka`, com `hostname` e nome de serviço `kafka`. |
| Interface operacional | Um container `kafka-ui`, imagem `provectuslabs/kafka-ui:v0.7.2`, somente para desenvolvimento local. |
| Replicação | Fator 1, pois o ambiente é de nó único e não representa alta disponibilidade. |
| Criação de tópicos | Explícita e idempotente pelo provisionamento do Compose; `auto.create.topics.enable=false`. |

O Kafka deve operar nos papéis combinados `broker` e `controller`, com listener interno para a rede Docker e listener externo para o host. A configuração de KRaft deve usar cluster ID estável, `controller.quorum.voters` apontando ao único nó e armazenamento formatado antes da inicialização. A Engenharia deve manter esses valores no Compose, sem segredo embutido.

### Containers, portas, rede e dados

| Recurso | Contrato |
| --- | --- |
| `kafka` interno | Listener `kafka:9092`, usado por containers na rede `event-platform`. |
| `kafka` externo | `localhost:9094`, usado por aplicações Spring executadas na máquina host. |
| Controller KRaft | Porta interna `9093`; não publicada ao host. |
| `kafka-ui` | `localhost:8080`; a UI conecta em `kafka:9092`. |
| Rede | Uma bridge Compose nomeada `event-platform`, isolada dos serviços de negócio. |
| Volume Kafka | Volume nomeado `kafka-data` montado no diretório de dados do broker; é o único dado persistente obrigatório. |
| Volume UI | Não obrigatório; configurações da UI são efêmeras. |

Não publicar 9092 no host. Isso evita que aplicações executadas fora do Docker recebam endereço não roteável do listener interno. As portas 8080 e 9094 são exclusivamente locais e não devem ser expostas por infraestrutura remota.

## 3. Perfis e configuração Spring

O suporte Kafka será opt-in e não pode alterar o comportamento atual quando desativado.

| Contexto | Perfil/condição | Bootstrap servers | Responsabilidade |
| --- | --- | --- | --- |
| Order Service | Perfil `kafka` | `${KAFKA_BOOTSTRAP_SERVERS:localhost:9094}` | Publicar `OrderConfirmed` v1 após o gatilho de domínio já existente. |
| Inventory Service | Perfil `kafka` | `${KAFKA_BOOTSTRAP_SERVERS:localhost:9094}` | Consumir o tópico aprovado e produzir resultado rastreável sem efeito duplicado. |
| Demais serviços | Sem perfil Kafka | Não aplicável | Não receber dependência, configuração ou container Kafka. |

Os grupos de consumo devem seguir `mercadoaurora.<servico>.v<versao-maior>`; para o consumidor inicial, `mercadoaurora.inventory.v1`. `group.id`, bootstrap servers e parâmetros operacionais devem ser externalizáveis por variáveis de ambiente. O perfil não deve substituir as configurações de datasource, portas HTTP ou integrações REST existentes.

Para execução de aplicações dentro de containers no futuro, a mesma variável deve poder receber `kafka:9092`; esse cenário não autoriza adicionar os serviços de negócio ao Compose nesta Sprint.

## 4. Tópico, evento e contrato de dados

O único tópico autorizado é:

```text
mercadoaurora.order.order-confirmed.v1
```

| Propriedade | Valor inicial |
| --- | --- |
| Partições | 1 |
| Replication factor | 1 |
| Retenção | 7 dias |
| Cleanup policy | `delete` |
| Chave Kafka | Identificador do pedido, serializado como string UUID. |

O nome do evento usa PascalCase e passado (`OrderConfirmed`); o tópico usa organização, domínio e fato em kebab-case, seguido de versão maior. A versão maior é parte do nome do tópico e do contrato.

### Envelope JSON v1

O valor da mensagem será JSON UTF-8, serializado com Jackson. Não haverá Schema Registry nesta Sprint. O envelope mínimo é:

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

`eventId`, `correlationId` e `orderId` são UUIDs não nulos; `occurredAt` é UTC no formato ISO-8601 instantâneo. `eventType` e `eventVersion` devem corresponder ao tópico. Cabeçalhos Kafka não substituem campos obrigatórios do envelope; podem levar somente metadados técnicos adicionais que não sejam necessários ao contrato de negócio.

Mudança aditiva compatível pode permanecer em v1 desde que consumidores ignorem campos desconhecidos e os campos atuais não sejam removidos, renomeados ou tenham tipo/semântica alterados. Alteração incompatível exige novo tópico com versão maior, coexistência explícita e atualização do Event Catalog antes da implementação.

## 5. Confiabilidade, saúde e diagnóstico

### Publicação e consumo

- A publicação deve receber confirmação do broker (`acks=all`) antes de ser considerada concluída tecnicamente.
- Producer e consumer devem registrar `eventId`, `correlationId`, `orderId`, tópico, partição e offset (quando disponível), sem registrar payload completo por padrão.
- O consumer deve ser idempotente por `eventId`; reentrega não pode gerar efeito de negócio duplicado.
- Falhas transitórias usam retry limitado e observável. Após esgotamento, a mensagem deve permanecer diagnosticável; DLT não é definida por este contrato e requer refinamento aprovado antes de ser criada.
- Este contrato não promete entrega ou processamento exatamente uma vez.

### Health checks

| Componente | Verificação mínima | Critério |
| --- | --- | --- |
| Kafka | `kafka-topics --bootstrap-server kafka:9092 --list` executado no container do broker | Broker responde e o tópico aprovado existe. |
| Kafka UI | HTTP em `http://localhost:8080/actuator/health` ou endpoint de saúde suportado pela imagem escolhida | UI disponível; falha da UI não derruba o broker. |
| Order/Inventory com perfil `kafka` | Actuator health inclui conectividade Kafka | Aplicação fica `DOWN`/`OUT_OF_SERVICE` quando o broker exigido não estiver acessível. |

Logs devem ser estruturados em nível `INFO` para publicação e consumo bem-sucedidos e `WARN`/`ERROR` para retries ou falhas, sempre com os identificadores de rastreio. Não registrar credenciais, tokens ou dados comerciais adicionais. Kafka UI é ferramenta de inspeção, não fonte de verdade nem mecanismo de reparo.

## 6. Critérios mínimos de aceite

1. `docker compose -f infra/event-platform/compose.yaml up -d` inicia Kafka em KRaft e Kafka UI sem ZooKeeper.
2. O broker responde em `localhost:9094`; a UI é acessível em `localhost:8080`; o controller não é exposto ao host.
3. A rede e o volume definidos neste contrato existem e uma reinicialização preserva os dados do broker.
4. O provisionamento cria somente `mercadoaurora.order.order-confirmed.v1`, com uma partição, replicação 1 e retenção de 7 dias.
5. Com perfil `kafka`, Order publica envelope JSON v1 válido após a confirmação já existente e Inventory o consome no grupo `mercadoaurora.inventory.v1`.
6. O consumo é rastreável por `eventId`, `correlationId` e `orderId`, e reprocessar a mesma mensagem não cria efeito de negócio duplicado.
7. Sem o perfil `kafka`, os serviços preservam o comportamento de Sprint 1; REST entre Order e Inventory continua funcional.
8. Health checks e logs permitem diagnosticar broker indisponível, falha de publicação e falha de consumo sem exibir payload completo.

## 7. Trade-offs e alternativas rejeitadas

| Alternativa | Decisão | Motivo |
| --- | --- | --- |
| ZooKeeper | Rejeitada | KRaft elimina componente adicional e é o caminho de metadados adotado para Kafka moderno. |
| Cluster multi-broker | Adiada | Alta disponibilidade e replicação real excedem a demonstração local; o contrato explicita fator 1. |
| Schema Registry/Avro/Protobuf | Adiado | Apenas um evento v1 e um consumidor inicial; JSON explícito reduz a infraestrutura inicial. |
| Auto-criação de tópicos | Rejeitada | Oculta erros de nomenclatura e não fornece propriedades de tópico reprodutíveis. |
| DLT como padrão | Adiada | Sem política funcional de reprocessamento aprovada, uma DLT só deslocaria a decisão operacional. |
| Kafka substituir REST | Rejeitada | O Context Map determina evolução complementar e não altera a regra de reserva atual. |
| Saga, Gateway, Kubernetes e Payment Service | Fora de escopo | Não são necessários para o primeiro fato de domínio e ampliariam indevidamente a Sprint. |

## 8. Handoff para Software Engineering

A Engenharia pode implementar a infraestrutura e o suporte Kafka somente conforme este contrato. Qualquer divergência — novo tópico, alteração de envelope, retry/DLT, mudança de semântica do Inventory, múltiplos brokers ou exposição externa — deve passar por novo refinamento técnico e Architecture Gate antes de entrar em código.
