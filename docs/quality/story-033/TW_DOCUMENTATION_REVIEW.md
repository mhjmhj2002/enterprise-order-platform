# TW Documentation Review — Story #33

**Status:** CHANGES REQUIRED
**From:** Technical Writer
**To:** Quality Engineer
**Scope:** Correções de documentação de qualidade antes da segunda revisão do TW.

## Contexto

O Technical Writer sincronizou os documentos institucionais com a implementação validada do consumer `OrderConfirmed` v1. Permanecem três ajustes sob ownership do Quality Engineer nos artefatos de teste da Story #33.

Este parecer não solicita alteração de código, arquitetura, contrato de evento ou regras de negócio.

## Ajustes obrigatórios

### 1. Corrigir rastreabilidade da plataforma

**Documento:** [TEST_PLAN.md](TEST_PLAN.md)

Na seção **Serviços envolvidos**, substituir a referência à “Kafka local da Story #20” pela Story #37, que é a plataforma local de eventos aprovada para a Sprint 2.

**Critério de aceite:** a dependência de infraestrutura do plano aponta para a Story #37 e permanece consistente com o Event Platform Technical Contract.

### 2. Resolver a divergência de `INV-033-013`

**Documentos:** [TEST_PLAN.md](TEST_PLAN.md) e [TEST_REPORT.md](TEST_REPORT.md)

O plano define `INV-033-013` como regressão **sem** perfil Kafka. O relatório a marca como aprovada, mas informa que `mvn test` sem perfil é inaplicável e usa `mvn -Pkafka test` como evidência.

Escolher e registrar uma única situação:

1. Executar e registrar evidência reproduzível da regressão sem perfil Kafka; ou
2. Refinar formalmente o cenário e seu critério de aceite para a estratégia de execução aprovada, com justificativa de por que o perfil Maven `kafka` é obrigatório para os testes atuais.

**Critério de aceite:** plano e relatório descrevem o mesmo cenário, o mesmo comando aplicável e a mesma evidência. Não é permitido manter o cenário como “sem perfil Kafka” e aprová-lo somente com execução `-Pkafka`.

### 3. Consolidar a baseline Java da evidência

**Documento:** [TEST_REPORT.md](TEST_REPORT.md)

O relatório identifica Java 25 como ambiente observado, mas registra a execução aprovada com Temurin 21.0.11 e determina Java 21 como baseline do projeto.

Atualizar a seção de referência para distinguir claramente:

- **Baseline suportada e usada para aprovação:** Temurin 21.0.11 / Java 21, conforme `.sdkmanrc`.
- **Ambiente adicional observado:** Eclipse Adoptium 25.0.3+9-LTS, quando aplicável, sem elevar Java 25 a baseline nem usar essa execução como fundamento da aprovação.

Se não houver evidência de execução em Java 25 relevante para a validação, remover essa referência da tabela de ambiente observado.

**Critério de aceite:** não há ambiguidade sobre qual JDK sustenta o resultado aprovado.

## Preservar

- Resultado técnico validado e as evidências de consumo, idempotência, rejeições e ausência de efeitos sobre estoque e reservas.
- Registro da limitação Testcontainers–Docker como conhecida e não bloqueadora, com a execução integrada local como evidência complementar.
- Escopo sem DLT, novos tópicos, novos eventos ou mudanças em regras de estoque.

## Handoff para segunda revisão

Após atualizar os dois documentos, o QE deve informar:

- os arquivos alterados;
- a decisão/evidência adotada para `INV-033-013`; e
- a baseline Java registrada no Test Report.

O Technical Writer então executará a segunda revisão de consistência documental.
