# TEST PLAN — STORY-020

## Status

**Preparado — não executado.** Este artefato define a estratégia de validação para o handoff da Engenharia. Não há resultado de teste, evidência de execução ou aprovação de qualidade neste momento.

## Objetivo

Validar que a plataforma local de eventos disponibiliza um Kafka 3.9.0 em KRaft e Kafka UI de forma reproduzível, isolada e diagnosticável, conforme o [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md). A validação comprovará a base necessária para as Stories posteriores de publicação e consumo de `OrderConfirmed` v1, sem antecipar seus testes funcionais.

## Escopo

- Compose dedicado em `infra/event-platform/compose.yaml`.
- Broker `kafka` em nó único KRaft, sem ZooKeeper.
- Kafka UI para inspeção local.
- Rede `event-platform`, volume persistente `kafka-data`, listeners e portas contratados.
- Provisionamento explícito, idempotente e restrito ao tópico `mercadoaurora.order.order-confirmed.v1`.
- Propriedades iniciais do tópico: uma partição, replication factor 1, retenção de 7 dias e cleanup policy `delete`.
- Saúde, disponibilidade e evidências operacionais da plataforma.

## Serviços envolvidos

- Plataforma de eventos: Kafka e Kafka UI.
- Order Service e Inventory Service **não serão iniciados nem validados nesta Story**; a integração com seus perfis `kafka` pertence às Stories posteriores.

## Ambiente planejado

| Item | Valor esperado |
| --- | --- |
| Orquestração | Docker Compose Specification |
| Kafka | `apache/kafka:3.9.0` |
| Metadados | KRaft, nó único, sem ZooKeeper |
| Kafka UI | `provectuslabs/kafka-ui:v0.7.2` |
| Broker externo | `localhost:9094` |
| Broker interno | `kafka:9092`, somente na rede Compose |
| Controller | `9093`, interno e não publicado |
| UI | `http://localhost:8080` |

Os valores efetivos de host, Docker/Compose e commit serão registrados no relatório após a entrega.

## Pré-condições de entrada

- Implementação da Story entregue pela Engenharia e referência do commit/PR informada.
- Docker Engine e Docker Compose disponíveis para o QE.
- Portas locais `8080` e `9094` livres.
- Nenhuma instância prévia da plataforma usando a mesma rede, volume ou portas, ou procedimento de isolamento documentado.
- Contrato técnico vigente e sem divergências não aprovadas.

## Estratégia de validação

A execução seguirá esta ordem: inspeção estática do Compose, subida limpa, smoke operacional, inspeção do broker/tópico/rede/volume, reinicialização para persistência, validações negativas e coleta de evidências. A Kafka UI será usada como apoio de inspeção; a fonte de verdade para broker e tópico será a CLI do Kafka e a inspeção do Docker.

Não serão publicados eventos de negócio nem executados testes de Order/Inventory nesta fase. Os cenários de integração abaixo ficam planejados para o handoff das Stories dependentes e não integram o aceite desta Story.

## Cenários de teste planejados

| ID | Tipo | Cenário e critério esperado | Evidência planejada |
| --- | --- | --- | --- |
| EVT-INF-001 | Smoke | Subir `docker compose -f infra/event-platform/compose.yaml up -d`; `kafka` e `kafka-ui` ficam disponíveis, sem container ZooKeeper. | Saída do Compose e lista/status dos containers. |
| EVT-INF-002 | Configuração | Confirmar imagem e tag fixas, KRaft em papéis broker/controller, cluster ID estável, quorum de nó único e armazenamento formatado antes da inicialização. | Trecho do Compose e logs iniciais do broker sem ZooKeeper. |
| EVT-INF-003 | Conectividade | Broker responde pela CLI interna em `kafka:9092` e por cliente no host em `localhost:9094`; a porta 9092 não é publicada ao host. | Saída de listagem de tópicos e inspeção de portas/listeners. |
| EVT-INF-004 | Saúde | Kafka UI responde em `localhost:8080` e seu endpoint de saúde suportado responde; indisponibilidade da UI não interrompe o broker. | Resposta HTTP de saúde, status dos containers e logs. |
| EVT-INF-005 | Isolamento | A rede bridge nomeada `event-platform` existe e somente os containers da plataforma participam dela. Controller 9093 não é publicado. | Inspeção de rede e mapeamento de portas. |
| EVT-INF-006 | Provisionamento | Com `auto.create.topics.enable=false`, o provisionamento cria somente `mercadoaurora.order.order-confirmed.v1`; nova subida é idempotente. | Listagem antes/depois da reinicialização e logs de provisionamento. |
| EVT-INF-007 | Contrato do tópico | O tópico possui 1 partição, RF=1, retenção de 7 dias e cleanup policy `delete`. | Saída de descrição e configuração do tópico. |
| EVT-INF-008 | Persistência | Após reinicializar os containers sem remover o volume, o volume `kafka-data` e o tópico provisionado permanecem disponíveis. | Inspeção do volume, sequência de reinício e descrição do tópico posterior. |
| EVT-INF-009 | Negativo | Tentar criar/publicar em tópico não autorizado não resulta em criação automática; a falha é observável. | Comando executado, erro retornado e listagem final de tópicos. |
| EVT-INF-010 | Diagnóstico | Interromper o broker torna a indisponibilidade identificável por saúde/logs; Kafka UI não é tratada como fonte de verdade ou reparo. | Status/health, logs relevantes e procedimento de restauração. |
| EVT-INT-001 | Integração futura | Com perfil `kafka`, Order publica envelope `OrderConfirmed` v1 após confirmação e Inventory consome no grupo `mercadoaurora.inventory.v1`. | Planejado para Stories de publicação e consumo: IDs, tópico, partição, offset e logs estruturados. |
| EVT-INT-002 | Regressão futura | Sem perfil `kafka`, os fluxos REST existentes de Order e Inventory permanecem funcionais. | Planejado para a validação integrada posterior. |

## Critérios de aceite para esta Story

- EVT-INF-001 a EVT-INF-010 aprovados.
- Não há ZooKeeper, tópico adicional, listener interno exposto ao host, nem divergência de versão/imagem em relação ao contrato.
- O ambiente pode ser iniciado e reiniciado de modo reproduzível, mantendo os dados obrigatórios do broker.
- Nenhum defeito Critical ou High aberto relacionado à plataforma.
- As evidências listadas estão anexadas ou referenciadas no futuro `TEST_REPORT.md`.

## Estratégia de evidências

Cada evidência deve conter data/hora, ambiente, commit/PR, comando ou ação reproduzível, resultado esperado, resultado obtido e identificação do cenário. Serão preservados:

- Saídas sanitizadas de Docker Compose, CLI Kafka e HTTP health;
- Inspeções de containers, rede, volume, listeners e configuração do tópico;
- Logs relevantes de inicialização e indisponibilidade, sem credenciais, tokens ou payloads de negócio;
- Captura da Kafka UI apenas como apoio visual, nunca como única prova;
- Referência cruzada entre cenário, evidência e eventual bug report.

## Critérios de saída

- Todos os cenários no escopo executados e aprovados, ou bloqueios/defeitos documentados.
- `TEST_REPORT.md` criado com contagens de aprovado, falho e bloqueado.
- Evidências reproduzíveis disponíveis.
- A recomendação do QE limitada a `Validation Completed` ou `Validation Rejected`, após execução.

## Riscos conhecidos

- Conflito de portas, volume ou rede local pode simular falha de produto; o ambiente deve ser identificado na evidência.
- Erros de advertised listener podem permitir o container e falhar para aplicações no host; por isso EVT-INF-003 valida ambos os caminhos.
- Auto-criação acidental pode mascarar erro de nome de tópico; EVT-INF-006 e EVT-INF-009 são bloqueantes.
- A plataforma disponível não comprova publicação, consumo, idempotência ou ausência de regressão REST; essas verificações dependem das Stories posteriores.

## Fora de escopo

- Execução de testes agora, antes do handoff da Engenharia.
- Publicação/consumo de eventos por Order e Inventory, idempotência de consumer e retries de aplicação.
- Alteração de código, arquitetura, regras de domínio, Compose ou configuração de serviços.
- DLT, Schema Registry, ZooKeeper, múltiplos brokers, alta disponibilidade, produção, Kubernetes e exposição remota.

## Dependências para execução posterior

- Handoff da Engenharia com Compose implementado e instruções de execução.
- Ambiente Docker operacional para o QE.
- Para EVT-INT-001 e EVT-INT-002: entregas das Stories de publicação e consumo, além dos serviços Order e Inventory disponíveis.
