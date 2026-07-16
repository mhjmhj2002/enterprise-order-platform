# Story #44 — Architecture Gate: Visibilidade Operacional de `OrderConfirmed` v1

**Status:** PROPOSED — awaiting explicit Engineering Manager approval

**Data:** 2026-07-16

**Baseline:** Sprint 3 — confiabilidade inicial do processamento no Inventory

**Parecer do Technical Lead:** **RECOMMENDED**

## 1. Decisão proposta

Recomenda-se criar uma **visão operacional de consulta, aditiva e local ao Inventory Service**, para cada fato `OrderConfirmed` v1 já recebido pelo serviço. A visão reunirá, sob a identidade `orderId` e `eventId`, o fato recebido, o estado atual do processamento, a evidência de reconhecimento e um histórico mínimo de interrupção temporária e recuperação quando esse percurso tiver ocorrido.

O Inventory continua owner exclusivamente do seu resultado de processamento. O Order continua owner do pedido e da semântica de `OrderConfirmed`. Portanto, a consulta operacional não lerá o banco do Order nem introduzirá chamada REST Inventory → Order. A confirmação comercial continua verificável na API já existente do Order; a visão do Inventory estabelece a relação com o fato recebido e com o resultado local do seu tratamento.

Nenhuma implementação é autorizada por este documento. A implementação só poderá começar após aprovação explícita do Engineering Manager.

## 2. Fluxo e responsabilidades

```text
Operador autorizado
  ├─ GET Order /orders/{orderId}
  │      └─ confirma o pedido comercial (owner: Order)
  │
  └─ GET Inventory /order-confirmation-observations/{orderId}
         └─ associa o mesmo orderId ao OrderConfirmed v1, percurso e resultado
            (owner: Inventory)

OrderConfirmed v1 → Inventory: pendência durável → tentativas locais
                                                 ├─ interrupção temporária
                                                 └─ evidência única + COMPLETED
```

| Contexto / componente | Responsabilidade nesta Story |
| --- | --- |
| Order Service | Nenhuma mudança. Continua fonte do pedido e producer/owner de `OrderConfirmed` v1. Sua consulta existente informa o estado comercial `CONFIRMED`. |
| Kafka | Nenhuma mudança. Continua transportando exclusivamente o tópico, chave, envelope e grupo já aprovados. |
| Inventory consumer e recovery worker | Mantêm o registro durável, a recuperação local e a idempotência existentes; passam a registrar marcos operacionais mínimos necessários para tornar uma interrupção temporária recuperada consultável. |
| Inventory persistence | Mantém `order_confirmation_processing` e `order_confirmation_evidence` como fontes locais. Acrescenta um histórico imutável e limitado de ciclo de processamento, indexado por `eventId`, sem armazenar payload completo nem dados comerciais. |
| Inventory API | Adiciona uma consulta de leitura específica para a visão operacional. Os endpoints de evidência e processamento da Story #34 permanecem compatíveis e inalterados. |
| Orientação operacional | Documenta a sequência de duas consultas, os estados suportados e os limites para que uma pessoa não envolvida na implementação interprete a evidência. |

## 3. Contrato de consulta proposto

Será adicionado, de forma aditiva, o endpoint:

```text
GET /api/v1/inventory/order-confirmation-observations/{orderId}
```

Ele retornará uma coleção (uma observação por `eventId`) com, no mínimo:

- a correlação `orderId`, `eventId` e `correlationId`;
- a identificação do fato (`eventType=OrderConfirmed`, versão, `occurredAt`) e os metadados de rastreio já aprovados (tópico, partição e offset);
- o estado atual do processamento suportado (`PENDING` ou `COMPLETED`), as datas relevantes e a evidência de reconhecimento quando concluído;
- uma linha de ciclo de vida com marcos `REGISTERED`, `TEMPORARY_FAILURE` e `COMPLETED`, quando aplicáveis; e
- uma indicação derivada de resultado único para um item concluído, sustentada pela unicidade atual de `eventId` na pendência e na evidência.

`TEMPORARY_FAILURE` registrará apenas classificação operacional segura e data; não exporá stack trace, credenciais, payload completo ou detalhes internos de infraestrutura. A ausência de `TEMPORARY_FAILURE` não é evidência de que nunca houve indisponibilidade antes do registro durável: nessa janela a garantia continua sendo a reentrega Kafka. O endpoint não afirma que um `orderId` sem observação não foi confirmado; ele apenas declara que o Inventory ainda não tem um fato observável para ele.

O formato exato de DTO e códigos para coleção vazia/ausência serão definidos na implementação, desde que preservem esses significados e não alterem contratos HTTP existentes. A API não se torna dashboard, mecanismo de alerta ou API de reprocessamento.

## 4. Persistência e consistência

O histórico proposto é uma evidência operacional local, não um novo evento de domínio nem um tópico Kafka. Cada marco conterá somente `eventId`, tipo do marco, instante e, quando aplicável, categoria segura da falha; a ordenação será estável por identidade e instante/sequência.

A gravação do marco de falha deve sobreviver à falha da tentativa que ele representa. A implementação deverá delimitar a transação para que o rollback do reconhecimento não elimine essa evidência; o resultado final (`COMPLETED`) e a evidência única de reconhecimento permanecem consistentes na mesma unidade transacional local, como definido na Story #34.

Não se altera a garantia arquitetural: o processamento é idempotente por `eventId`, com resultado funcional único, e não exatamente uma vez. A visão é projeção de leitura dos dados proprietários do Inventory; não duplica estado de pedido nem estoque.

## 5. Aderência à arquitetura vigente

| Referência | Aderência |
| --- | --- |
| Architecture Overview | Preserva arquitetura híbrida REST + Kafka, processamento durável local e consulta aditiva de processamento do Inventory. |
| Context Map e Service Boundaries | Não cria dependência runtime inversa do Inventory para Order; cada contexto continua dono de seus próprios dados e regras. |
| ADR-007 | Mantém `OrderConfirmed` como único fato assíncrono, com Order producer e Inventory consumer de reconhecimento, sem migrar REST. |
| Event Catalog e Event Platform Technical Contract | Não muda tópico, chave, envelope v1, `acks=all`, grupo `mercadoaurora.inventory.v1`, perfil Kafka, consumer ou retenção. Não há novo evento, consumer ou infraestrutura. |
| Engineering Roadmap | Implementa o recorte incremental da Sprint 4: visibilidade de comportamento e evidência operacional, não observabilidade corporativa. |

## 6. Alternativas e trade-offs

| Alternativa | Decisão | Fundamentação |
| --- | --- | --- |
| Compor uma visão local no Inventory a partir da pendência, evidência e histórico mínimo | **Escolhida** | Mantém ownership, usa a persistência já existente e fornece uma leitura interpretável do percurso, inclusive após recuperação. |
| Usar somente os dois endpoints existentes (`processings` e `order-confirmations`) | Rejeitada | Expõe registros técnicos separados e não prova de modo autossuficiente o percurso de interrupção/recuperação para um operador. |
| Chamada síncrona Inventory → Order para uma API única | Rejeitada | Introduz acoplamento temporal e uma dependência não necessária; Inventory não deve projetar ou decidir o estado comercial do Order. |
| Nova projeção/serviço central de observabilidade | Rejeitada | Cria infraestrutura, ownership e operação além do incremento de um único consumer. |
| Logs/Kafka UI como evidência principal | Rejeitada | Não constituem fonte de verdade estável, reproduzível e acessível sem conhecimento técnico; Kafka UI já é explicitamente ferramenta de inspeção. |
| Métricas, tracing distribuído ou alertas | Adiada | São capacidades corporativas fora do escopo funcional aprovado. |

Trade-off aceito: o histórico aumenta a escrita local do Inventory e não cobre falhas anteriores ao primeiro registro durável. Essa limitação será exibida na orientação de consulta e não deve ser mascarada como observabilidade completa.

## 7. Riscos e controles

| Risco | Controle / condição arquitetural |
| --- | --- |
| Marco de falha perdido pelo rollback da tentativa | Persistir a evidência de interrupção em limite transacional que sobreviva ao rollback do reconhecimento; provar o cenário em integração. |
| Histórico revelar dados sensíveis ou detalhes instáveis | Persistir/expor apenas classificação segura, identidade e datas; não incluir payload, exceção bruta ou credenciais. |
| Consulta induzir conclusão errada sobre pedido inexistente ou não confirmado | Orientação obrigatória: Order é a fonte do estado comercial; Inventory reporta somente fatos observados e seu processamento. |
| Reentrega criar múltiplos resultados ou marcos ambíguos | Conservar chave `eventId` para pendência/evidência e definir marcos idempotentes ou deduplicáveis por tentativa/semântica. |
| Crescimento indefinido do histórico | Escopo limitado ao fluxo v1 e retenção corrente do banco local; política corporativa de retenção e observabilidade fica para Story futura. |
| Expansão para operação corporativa | Sem alertas, dashboards, SLO, tracing, DLT, reprocessamento manual ou novos serviços nesta Story. |

## 8. Escopo

### Incluído

- Uma visão de consulta operacional do Inventory por `orderId`, que relacione o fato recebido `OrderConfirmed` v1, seu processamento e a evidência final.
- Histórico mínimo de interrupção temporária e recuperação, quando efetivamente registrados após a pendência durável.
- Contrato HTTP aditivo no Inventory e orientação de consulta para operadores.
- Proteções de compatibilidade, idempotência, privacidade de dados e regressão dos fluxos REST/Kafka existentes.

### Fora do escopo

- Alterar pedido, estoque, reservas, regras de negócio ou o significado de `OrderConfirmed`.
- Alterar producer, tópico, envelope, versão, consumidores, grupos, Kafka ou infraestrutura compartilhada.
- Chamada REST Inventory → Order, banco compartilhado, API Gateway, novo serviço ou base central de observabilidade.
- Alertas, dashboards avançados, métricas/SLO, tracing distribuído, análise de causa, gestão de incidentes, DLT e tratamento de falha permanente.
- Promessa de rastrear falha anterior ao registro durável ou de processamento exatamente uma vez.

## 9. Evidências requeridas antes do Quality Gate

A implementação e a validação deverão comprovar, de forma reproduzível:

1. um `GET` no Order mostra o pedido comercialmente `CONFIRMED` e a consulta operacional do Inventory retorna o mesmo `orderId` associado a um único `OrderConfirmed` v1 e ao resultado `COMPLETED`;
2. a visão concluída contém a mesma identidade e metadados de rastreio da pendência/evidência existentes, sem alterar os endpoints legados;
3. uma falha temporária após o registro durável é consultável como interrupção e, quando a condição desaparece, como recuperação seguida de `COMPLETED`;
4. a recuperação produz exatamente uma evidência e um efeito funcional por `eventId`, inclusive sob reentrega/tentativas concorrentes;
5. não há payload completo, dados sensíveis ou detalhes de exceção na resposta nem no histórico persistido;
6. tópico, envelope, producer, consumer group e REST de reserva/commit mantêm o comportamento aprovado; e
7. uma orientação independente permite interpretar estados, ausência de dados e as limitações conhecidas sem relato da pessoa implementadora.

## 10. Parecer e handoff

**RECOMMENDED.** A proposta resolve a lacuna de visibilidade com uma projeção de leitura mínima no contexto que já é dono do resultado de processamento. Ela preserva as fronteiras entre Order e Inventory, reutiliza `eventId` como identidade de idempotência e evita transformar a Story em plataforma de observabilidade. O único acréscimo de persistência é justificado porque o estado final atual, por si só, não permite demonstrar uma interrupção temporária já recuperada.

### Handoff institucional — Architecture Gate → Engineering Manager

O Engineering Manager deve decidir sobre este contrato técnico antes de qualquer implementação. A aprovação deverá autorizar somente o escopo das seções 2 a 9. Qualquer necessidade de contrato entre serviços, evento/tópico novo, alteração de regra de negócio, armazenamento compartilhado ou capacidade de observabilidade mais ampla exige novo refinamento e Architecture Gate.

```text
Technical Lead — proposta RECOMMENDED
        ↓
Engineering Manager — aprovação explícita do Architecture Gate
        ↓
Software Engineer — implementação autorizada somente se aprovada
```
