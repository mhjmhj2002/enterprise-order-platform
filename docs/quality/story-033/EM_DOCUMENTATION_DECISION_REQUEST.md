# EM Decision Request — Story #33 Documentation

**Status:** APPROVED — Alternative B
**From:** Technical Writer
**To:** Engineering Manager
**Related review:** [TW Documentation Review](TW_DOCUMENTATION_REVIEW.md)

## Decisão solicitada

Autorizar a resolução da divergência documental do cenário `INV-033-013` antes da segunda revisão do Technical Writer.

O [Test Plan](TEST_PLAN.md) define `INV-033-013` como regressão sem perfil Kafka. O [Test Report](TEST_REPORT.md) o declara aprovado com evidência de `mvn -Pkafka test` e informa que `mvn test` sem perfil é inaplicável à estrutura de testes atual.

Essa diferença impede que plano, critério de aceite e relatório descrevam a mesma validação.

## Alternativas para decisão

### Alternativa A — Manter regressão sem perfil Kafka

- O Quality Engineer executa e registra uma evidência reproduzível do fluxo REST sem perfil Kafka.
- `INV-033-013` permanece com a redação atual.
- O Test Report é corrigido para referenciar a evidência correspondente, separada de `mvn -Pkafka test`.

### Alternativa B — Refinar o cenário para a estratégia atual

- O Engineering Manager aprova que o perfil Maven `kafka` é obrigatório para a suíte atual da Story #33.
- O Quality Engineer atualiza Test Plan e Test Report para descrever o comando aplicável, a justificativa e o alcance de regressão efetivamente demonstrado.
- A redação “regressão sem perfil Kafka” deixa de ser usada para um cenário executado somente com `-Pkafka`.

## Recomendação do Technical Writer

**Alternativa B**, desde que o EM confirme formalmente que ela preserva o gate de qualidade da Story. O relatório já registra que as dependências Kafka dos testes são ativadas intencionalmente pelo profile Maven `kafka`; a decisão elimina a divergência sem criar uma evidência retroativa.

Caso o EM exija prova independente da regressão REST sem Kafka, deve aprovar a **Alternativa A** e solicitar a execução correspondente pelo QE.

## Resultado esperado do EM

Registrar uma das alternativas como:

- **APPROVED — Alternative A**; ou
- **APPROVED — Alternative B**; ou
- **CHANGES REQUIRED**, com orientação adicional.

Após a decisão, o QE atualiza os artefatos sob seu ownership e devolve o handoff ao Technical Writer para a segunda revisão documental.

## Decisão do Engineering Manager

**APPROVED — Alternative B.**

O profile Maven `kafka` é aprovado como obrigatório para a suíte atual da Story #33, pois disponibiliza as dependências Kafka exigidas pelos testes. Portanto, `INV-033-013` deve ser descrito exclusivamente como regressão dos fluxos de estoque executada por `mvn -Pkafka test`; a expressão “regressão sem perfil Kafka” não deve ser usada para esse cenário.

Esta decisão preserva o gate de qualidade porque a evidência aprovada permanece limitada ao que foi efetivamente demonstrado: os fluxos de estoque da suíte Inventory continuam aprovados com as dependências de teste habilitadas. Ela não transforma essa execução em evidência de comportamento runtime sem o profile Spring `kafka`, nem altera o caráter opt-in definido no Event Platform Technical Contract.

O QE deve manter alinhados Test Plan e Test Report quanto ao comando, justificativa e alcance de `INV-033-013`, registrar Java 21 como baseline de aprovação e preservar a limitação Testcontainers–Docker como não bloqueante. Após essa conferência, o handoff segue para a segunda revisão do Technical Writer.

## Limites

Esta solicitação não altera arquitetura, regras de negócio, Event Platform Technical Contract, escopo da Story ou resultado de qualidade sem a decisão formal do Engineering Manager.
