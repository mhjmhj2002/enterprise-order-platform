# Story #34 — Architecture Gate: Confiabilidade Inicial do Processamento

**Status:** APPROVED WITH CONDITIONS — Engineering Manager authorization recorded on 2026-07-15

**Data:** 2026-07-15

**Baseline:** `v0.4.0-event-platform` (Sprint 2)
**Parecer do Software Engineer:** **RECOMMENDED**

## 1. Resumo e recomendação

Recomenda-se implementar uma recuperação durável, local ao **Inventory Service**, para o processamento de `OrderConfirmed` v1. Antes de confirmar o offset Kafka, o consumer registrará de forma idempotente uma pendência de processamento no banco do Inventory. O próprio serviço retomará periodicamente as pendências ainda não concluídas. Quando o tratamento for concluído, a pendência será marcada como concluída junto com a evidência de reconhecimento já existente.

Assim, uma falha temporária que ultrapasse o retry curto atual não descarta silenciosamente o fato: ele permanece identificável e apto a nova tentativa após a condição cessar. A solução não cria novo evento, tópico, consumer, regra de pedido ou regra de estoque.

A implementação só pode começar após a aprovação explícita deste Gate pelo Engineering Manager.

## 2. Estratégia arquitetural

### Fluxo proposto

```text
OrderConfirmed v1 no tópico aprovado
             |
             v
Inventory consumer valida o envelope e registra pendência durável por eventId
             |
             +-- falha antes do registro --> offset não é confirmado; Kafka reentrega
             |
             v
Worker de recuperação do Inventory tenta o reconhecimento
             |
             +-- falha temporária --> pendência continua identificável para nova tentativa
             |
             v
Evidência idempotente gravada + pendência marcada como concluída
```

1. O consumer continua recebendo exclusivamente `mercadoaurora.order.order-confirmed.v1`, validando o envelope v1 e mantendo o grupo `mercadoaurora.inventory.v1`.
2. Para cada `eventId`, o Inventory registra uma pendência contendo apenas os dados contratuais necessários ao reconhecimento e os metadados Kafka de rastreio. A criação é idempotente.
3. A confirmação do offset só ocorre após a pendência estar duravelmente registrada. Se o banco estiver indisponível antes disso, a exceção mantém a mensagem elegível à reentrega do Kafka; o retry curto já existente continua sendo a primeira tentativa de recuperação.
4. Um componente interno e agendado do Inventory processa pendências não concluídas. Falhas transitórias deixam a pendência ativa para a próxima execução; a recuperação não depende de nova publicação pelo Order.
5. A conclusão grava a evidência de reconhecimento e a transição da pendência para concluída na mesma unidade transacional local. A unicidade já existente da evidência por `eventId` permanece a proteção contra reentregas e contra uma tentativa repetida após sucesso parcial.
6. A consulta de evidências do Inventory deverá permitir distinguir, para demonstração, uma pendência aguardando recuperação de um reconhecimento concluído. A forma exata da exposição será definida na implementação, preservando compatibilidade com a API existente.

### Caracterização de falha temporária

Para esta Story, é temporária a falha técnica recuperável no caminho de consumo/reconhecimento que desapareça dentro da janela da demonstração — por exemplo, indisponibilidade breve da persistência do Inventory ou erro transitório do adaptador local. Eventos inválidos, mudança de contrato, indisponibilidade permanente e regra de negócio não são classificados como falha temporária.

Não há promessa de exatamente uma vez, disponibilidade contínua ou recuperação automática de falhas permanentes. A garantia visada é **pelo menos uma tentativa futura enquanto a pendência durável existir**, combinada à idempotência do resultado funcional.

## 3. Aderência arquitetural

- **Event-Driven preservado:** Order continua publicando o mesmo fato; Kafka continua sendo o canal assíncrono; Inventory continua sendo o único consumer inicial.
- **Contrato técnico preservado:** tópico, chave, envelope JSON v1, grupo de consumo, `acks=all`, perfil `kafka` e rastreabilidade por `eventId`, `correlationId`, `orderId`, tópico, partição e offset não mudam.
- **Responsabilidades preservadas:** Order não conhece tentativas nem estado de recuperação do Inventory. Inventory é owner exclusivo do seu resultado de processamento, conforme o Event Catalog e as convenções de eventos.
- **Baixo acoplamento preservado:** não há chamada síncrona adicional, contrato de compensação, novo tópico ou dependência do producer para recuperar uma falha do consumer.
- **Idempotência preservada:** `eventId` continua sendo a chave de deduplicação. A pendência e a evidência devem usar essa mesma identidade; reentregas e reprocessamentos não criam novo efeito funcional.

## 4. Impactos previstos

| Área | Impacto |
| --- | --- |
| Order Service | Nenhuma alteração. Continua producer de `OrderConfirmed` v1. |
| Kafka/infraestrutura | Nenhuma alteração: sem tópico, partição, grupo ou container novo. |
| Consumer Kafka do Inventory | Ajuste para persistir a pendência antes da confirmação do offset e manter retry curto para falhas antes dessa persistência. |
| Aplicação Inventory | Novo caso de uso interno de retomada e agendador/worker local para pendências abertas. Não cria regra de domínio de estoque. |
| Persistência Inventory | Nova estrutura de pendência de processamento, com `eventId` único, dados mínimos do envelope, metadados Kafka, estado e datas de criação/atualização/tentativa. A tabela de evidência atual permanece e continua sendo a fonte do reconhecimento concluído. |
| API de consulta Inventory | Evolução aditiva, se necessária, para expor o estado pendente/concluído da demonstração sem quebrar consumidores atuais. |
| Contrato de evento | Nenhuma alteração de tópico, chave, envelope ou versão. |
| Configuração | Novas propriedades externalizáveis somente do Inventory para habilitar/parametrizar a cadência da recuperação e o limite de trabalho por execução. Perfil `kafka` e configurações de datasource existentes permanecem. |
| Serviços e integrações REST | Nenhuma alteração; o fluxo REST da baseline continua ativo. |

## 5. Decisões e trade-offs

| Decisão | Justificativa | Alternativas consideradas e motivo da não adoção |
| --- | --- | --- |
| Pendência durável no consumer, seguida de recuperação local | Sobrevive a reinício e a uma falha além do retry curto, usando PostgreSQL e a arquitetura hexagonal já adotados. | **Somente aumentar retry/backoff:** não preserva retomada confiável para falha mais longa e pode bloquear o consumer. **Retry infinito no listener:** dificulta reinício, controle e demonstração. |
| Um único tópico e um único consumer | Mantém integralmente o Event Platform Technical Contract e o escopo da Sprint 2. | **Retry topics/DLT:** exigiriam tópicos e política operacional novos; DLT completa está explicitamente fora do escopo. |
| Recuperação por worker interno do Inventory | Isola a responsabilidade no owner do resultado e evita novo serviço ou dependência externa. | **Serviço central de reprocessamento:** amplia o acoplamento e a superfície operacional sem necessidade para um único consumer. |
| Idempotência por `eventId` em todas as etapas | Reutiliza a identidade já contratada e a restrição única existente, tolerando reentrega e sucesso parcial. | **Deduplicação por offset:** não representa a identidade do fato e não protege reenvios equivalentes. |

Limitações assumidas: uma pendência causada por falha permanente permanecerá aberta e exigirá decisão posterior; não haverá quarentena/DLT nem operação avançada. O banco do Inventory é dependência da durabilidade local: enquanto ele estiver indisponível antes de registrar a pendência, a proteção é a retenção e a reentrega Kafka, não uma nova garantia independente.

## 6. Riscos técnicos e controles

| Risco / dependência | Controle arquitetural |
| --- | --- |
| Falha entre receber a mensagem e gravar a pendência | Não confirmar offset; permitir retry/reentrega do Kafka. |
| Falha entre persistir evidência e concluir a pendência | Executar ambas as mudanças na mesma transação local; a chave única por `eventId` protege a retomada se houver incerteza. |
| Reprocessamento repetido | `eventId` único na pendência e na evidência; transição de estado idempotente. |
| Pendências permanentes acumularem | Fora do tratamento completo desta Story; manter estado consultável e registrar como limitação para evolução futura. |
| Recuperação agressiva pressionar banco/consumer | Cadência e lote externalizáveis, com valores conservadores definidos e testados na implementação. |
| A confirmação do offset ocorrer cedo demais | Testes de integração devem provar que não há confirmação antes de a pendência estar persistida. |
| Demonstração não comprovar estado intermediário | Critério de implementação: consulta deve mostrar a mesma identidade do fato em estado pendente e, após recuperação, concluído. |

Dependências: baseline Sprint 2 publicada, Kafka e PostgreSQL locais disponíveis, perfil `kafka` ativo para o cenário, e aprovação deste Gate pelo Engineering Manager.

## 7. Fora do escopo confirmado

Permanecem fora desta Story:

- observabilidade avançada;
- tracing distribuído;
- métricas operacionais;
- Dead Letter Queue completa;
- Saga;
- evolução arquitetural além da confiabilidade inicial;
- novo evento, novo tópico, novo consumer ou mudança do contrato `OrderConfirmed` v1;
- recuperação completa de falhas permanentes.

## 8. Critérios para iniciar a implementação

Após aprovação, a implementação deverá demonstrar que:

1. uma falha temporária antes da conclusão deixa o fato identificável como pendente;
2. removida a falha, a recuperação conclui o reconhecimento sem nova publicação do Order;
3. uma nova tentativa para o mesmo `eventId` não duplica a evidência nem altera pedido, reserva ou estoque;
4. tópico, envelope e grupos da Sprint 2 permanecem inalterados;
5. o fluxo REST sem perfil Kafka preserva o comportamento aprovado.

## 9. Parecer do Software Engineer

**RECOMMENDED.** A proposta é incremental, simples para o escopo atual e compatível com a arquitetura Event-Driven e com o Event Platform Technical Contract. Ela cobre a lacuna do retry limitado da baseline ao transformar uma falha temporária em uma pendência durável e recuperável, sem antecipar DLT, Saga ou uma plataforma de observabilidade.

Handoff: **Software Engineer → Engineering Manager → aprovação explícita do Architecture Gate → Software Engineer → implementação da Story #34**.

## 10. Decisão do Engineering Manager

**Parecer final: APPROVED WITH CONDITIONS**

O Architecture Gate está aprovado para implementação. A proposta atende ao refinamento funcional da Story #34 e é compatível com a baseline da Sprint 2, com o Event Catalog e com o Event Platform Technical Contract. O Software Engineer está autorizado a iniciar o desenvolvimento, observando as condições abaixo, que deverão ser demonstradas antes do Quality Gate.

### Aderência verificada

| Item | Decisão | Justificativa |
| --- | --- | --- |
| Refinamento funcional | Aprovado | A pendência identificável e a recuperação local atendem à recuperação de falha temporária, à ausência de perda silenciosa e à demonstração de estado, sem criar comportamento de negócio adicional. |
| Responsabilidade do serviço | Aprovado | Inventory permanece responsável pelo seu próprio resultado. Order continua apenas como producer e não conhece o estado interno nem a recuperação do consumer. |
| Pendência durável e recuperação local | Aprovado | A pendência representa adequadamente um tratamento ainda não concluído e a recuperação permanece dentro do limite do Inventory, sem orquestração distribuída ou Saga. |
| Idempotência | Aprovado sob condição | A identidade por `eventId` é preservada para pendência e evidência; a implementação deve comprovar que reentregas e retomadas concorrentes não produzem mais de um resultado funcional. |
| Compatibilidade da baseline | Aprovado | `OrderConfirmed` v1, tópico, envelope, grupo, Event Catalog, Kafka local e integração REST permanecem inalterados. |
| Escopo | Aprovado | DLT completa, observabilidade avançada, tracing, métricas amplas, Saga, novos eventos, alterações no Producer e reestruturação de Kafka continuam fora da Story. |

### Riscos aceitos e condições de implementação

| Risco aceito | Condição antes do Quality Gate |
| --- | --- |
| Incerteza entre recebimento, persistência e confirmação Kafka | A evidência de implementação deve comprovar que o fato não é considerado consumido antes de estar duravelmente identificável, e que uma falha nessa janela não resulta em perda silenciosa. |
| Retomadas simultâneas ou reentregas | A solução deve preservar um único efeito funcional por `eventId`, inclusive quando houver mais de uma tentativa de recuperação. |
| Estado intermediário não demonstrável | A evidência funcional deve distinguir a mesma identidade em pendência de recuperação e em conclusão, sem exigir diagnóstico técnico ao demonstrador. |
| Crescimento de pendências e pressão operacional | A recuperação deve permanecer limitada ao recorte de falhas temporárias e ter operação controlável no Inventory; tratamento de pendências permanentes continua explicitamente fora de escopo. |
| Estados indefinidos após sucesso parcial | A conclusão da pendência e o reconhecimento devem manter consistência local verificável; qualquer limitação remanescente deve ser registrada para a avaliação de qualidade. |

Não é autorizada qualquer alteração de contrato, tópico, producer ou escopo funcional. Uma necessidade dessa natureza exige novo Architecture Gate antes de entrar em código.

### Autorização e handoff

**Autorização explícita:** o Software Engineer pode iniciar a implementação da Story #34 sob as condições deste parecer.

```text
Engineering Manager — Architecture Gate approved with conditions
        ↓
Software Engineer — implementação da Story #34
        ↓
Quality Engineer — planejamento e execução autorizada após handoff de implementação
```

## Implementação da Story #34

Esta implementação realiza a estratégia aprovada exclusivamente no Inventory Service:

- a migração `V3__create_order_confirmation_processing.sql` cria a pendência durável, identificada por `eventId`, com estados `PENDING` e `COMPLETED`, tentativas e metadados Kafka;
- o consumer registra a pendência de modo idempotente antes de tentar o reconhecimento; se esse registro falhar, a exceção impede o acknowledgment e permite a reentrega Kafka;
- a conclusão persiste a evidência existente e marca a pendência como concluída em uma única transação local;
- o agendador local, ativo apenas no perfil `kafka`, retoma pendências em lote. A cadência e o tamanho do lote são externalizados por `ORDER_CONFIRMATION_RECOVERY_FIXED_DELAY_MS` e `ORDER_CONFIRMATION_RECOVERY_BATCH_SIZE`;
- inserções concorrentes da mesma pendência usam `ON CONFLICT (event_id) DO NOTHING`; a evidência mantém a mesma proteção por `eventId`.
- a consulta aditiva `GET /api/v1/inventory/order-confirmation-processings/{orderId}` expõe o estado suportado da pendência (`PENDING` ou `COMPLETED`) e sua rastreabilidade, sem alterar a resposta de evidências concluídas já existente.

Não foram alterados o contrato público HTTP, o catálogo de eventos, o envelope, o tópico, o producer nem a infraestrutura Kafka.
