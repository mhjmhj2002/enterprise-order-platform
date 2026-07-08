# Engineering Guide — Mercado Aurora

Este documento define os padrões técnicos e de processo usados em todos os
microsserviços da plataforma. Ele existe para que qualquer pessoa do time
consiga abrir um serviço novo — ou revisar um PR de outro serviço — sem
precisar perguntar "como a gente faz isso aqui?".

Se uma decisão aqui parecer arbitrária, ela provavelmente é uma convenção
(o valor está em todos seguirem a mesma, não em qual foi escolhida). Decisões
de arquitetura com trade-offs reais vivem em ADRs, não aqui.

---

## 1. Stack

| Item | Versão/Escolha | Justificativa |
|---|---|---|
| Linguagem | Java 21 (LTS) | Última LTS estável; virtual threads e records maduros o suficiente para reduzir boilerplate |
| Framework | Spring Boot 3.3.x | Compatível com Java 21, ecossistema maduro, Spring Data JPA + Spring Web cobrem 100% da Sprint 0 |
| Build | Maven | Padrão do time, convenção sobre configuração |
| Banco | PostgreSQL 16 | Um schema por serviço (nunca banco compartilhado) |
| Testes | JUnit 5 + Mockito + Testcontainers | Testcontainers garante que testes de integração rodem contra Postgres real, não H2 |
| Documentação de API | springdoc-openapi | Gera OpenAPI a partir do código, evita doc desatualizada |

> Atualizar esta tabela é uma mudança de ADR, não um PR silencioso.

---

## 2. Estrutura de pacotes

Pacote raiz: `com.mercadoaurora.<nome-do-servico>`

```
com.mercadoaurora.order
├── controller/       // Controllers REST (entrada HTTP)
├── service/          // Regras de negócio
├── repository/       // Acesso a dados (Spring Data JPA)
├── domain/           // Entidades e agregados de domínio
├── dto/              // Request/Response — nunca expõe entidade de domínio
├── client/           // Clients HTTP para outros serviços (Inventory, etc.)
├── exception/        // Exceptions customizadas + handler global
└── config/           // Beans de configuração
```

**Regra de ouro:** `controller` nunca fala com `repository` diretamente, e
`domain` nunca é serializado direto como resposta HTTP. Sempre passa por
`dto`.

---

## 3. Nomenclatura

### Controllers
- Sufixo `Controller`. Ex: `OrderController`, `InventoryController`.
- Um controller por recurso/agregado, não por caso de uso.
- Endpoint no plural: `/orders`, `/products`, não `/order`.

### DTOs
- Sufixo indica direção e propósito, sempre:
  - `CreateOrderRequest` — dado que entra para criar algo
  - `UpdateOrderRequest` — dado que entra para atualizar
  - `OrderResponse` — dado que sai
  - Nunca um DTO genérico tipo `OrderDTO` sem indicar se é entrada ou saída.
- DTOs são `record`, não classes com getters/setters manuais.

### Services
- Sufixo `Service` para a interface (se houver) e `ServiceImpl` só quando
  existir mais de uma implementação. Na Sprint 0, sem múltiplas
  implementações, é só `OrderService` mesmo (classe concreta, sem interface
  desnecessária — YAGNI).

### Clients (chamadas entre serviços)
- Sufixo `Client`. Ex: `InventoryClient` dentro do Order Service.
- Interfaces de adapter (como pagamento) usam sufixo `Adapter`:
  `PaymentFakeAdapter implements PaymentGateway`.

### Entidades de domínio
- Sem sufixo. `Order`, `Product`, `StockItem` — nome do conceito de negócio,
  puro.

---

## 4. Exceptions

- Vivem em `exception/` dentro de cada serviço.
- Toda exception de negócio estende uma base própria do serviço, ex:
  `OrderDomainException`.
- Um único `@RestControllerAdvice` por serviço:
  `GlobalExceptionHandler`, responsável por traduzir exception → resposta
  HTTP padronizada (ver seção 6, formato de erro).
- Nomenclatura: `<Motivo>Exception`. Ex: `OrderNotFoundException`,
  `InsufficientStockException`. Nunca `Exception1` ou nomes genéricos tipo
  `BusinessException` sem contexto.

---

## 5. Configs

- Vivem em `config/`.
- Um `@Configuration` por preocupação, não um arquivo gigante:
  `WebClientConfig`, `OpenApiConfig`, `DataSourceConfig` (quando precisar
  fugir do default).
- Nada de lógica de negócio dentro de uma classe de config.

---

## 6. Como escrevemos APIs

- REST, recursos no plural, verbos HTTP com significado real
  (`POST /orders`, `GET /orders/{id}`, não `POST /orders/create`).
- Versionamento por path desde o início: `/api/v1/orders`. Mesmo sem
  previsão de v2, evita quebra de contrato no futuro.
- Todo erro segue o mesmo formato de corpo, produzido pelo
  `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-07-06T14:32:00Z",
  "status": 404,
  "error": "ORDER_NOT_FOUND",
  "message": "Pedido a1b2c3 não encontrado",
  "path": "/api/v1/orders/a1b2c3"
}
```

- OpenAPI é gerado automaticamente via springdoc — **não escrevemos YAML de
  API na mão**. Anotar o controller corretamente já é suficiente.
- Toda API pública documenta no mínimo: request/response de sucesso, e os
  principais erros esperados (404, 400, 409 quando aplicável).

---

## 7. Como organizamos testes

```
src/test/java/com/mercadoaurora/order/
├── unit/            // Testes de service/domain, tudo mockado
└── integration/      // Testes de controller + repository, via Testcontainers
```

- Testes unitários: rápidos, sem Spring context, sem banco. Mockito para
  dependências externas.
- Testes de integração: sobem o contexto Spring + Postgres real via
  Testcontainers. Usados para validar controller → service → repository →
  banco de ponta a ponta.
- Nome do teste descreve comportamento, não implementação:
  `deveRetornarErroQuandoEstoqueInsuficiente`, não `testMethod2`.
- Cobertura não é meta em si — o critério é: toda regra de negócio e todo
  fluxo de erro relevante tem teste. Não perseguimos % de cobertura por
  vaidade.

---

## 8. Como escrevemos README

Todo serviço tem um `README.md` na raiz com esta estrutura mínima:

```markdown
# <Nome do Serviço>

## O que este serviço faz
(2-3 frases, sem jargão)

## Responsabilidades
- ...

## O que NÃO é responsabilidade deste serviço
- ...

## Como rodar localmente
(comandos reais, testados)

## Endpoints principais
(link para o Swagger UI gerado, não lista manual)

## Dependências
- Bancos, outros serviços que este chama, variáveis de ambiente obrigatórias
```

A seção "O que NÃO é responsabilidade" é obrigatória — ela é o que evita
que o serviço vá absorvendo responsabilidade de outros com o tempo.

---

## 9. Como escrevemos ADR

- Local: `/docs/architecture/ADR/ADR-NNN-titulo-curto.md`, numeração sequencial, nunca
  reaproveitada mesmo se um ADR for depois superado.
- Template:

```markdown
# ADR-NNN: Título da decisão

## Status
Proposto | Aceito | Superado por ADR-XXX

## Contexto
Qual problema estamos resolvendo? Que restrições existem?

## Decisão
O que foi decidido, de forma direta.

## Consequências
O que ganhamos, o que pagamos como custo/dívida técnica.

## Alternativas consideradas
O que mais foi cogitado e por que foi descartado.
```

- Uma ADR documenta uma decisão, não uma sprint inteira. Se a reunião gerou
  3 decisões, são 3 ADRs.

---

## 10. Como criamos novas Stories

- Local: board do time (Jira/Linear/etc.), título no formato
  `[SERVICE] Ação objetiva` — ex: `[Order] Criar endpoint de criação de pedido`.
- Toda story precisa, antes de entrar em desenvolvimento
  (**Definition of Ready**):
  - Descrição no formato `Como <papel>, quero <ação>, para <benefício>`
  - Critérios de aceite claros e testáveis
  - Dependências externas identificadas (outro serviço, ADR relacionado)
- Toda story só é considerada concluída (**Definition of Done**) quando:
  - Código revisado (PR aprovado por ao menos 1 pessoa)
  - Testes unitários e de integração cobrindo os critérios de aceite
  - Endpoint documentado (OpenAPI gerado e conferido)
  - README do serviço atualizado, se a story mudou comportamento externo

---

## Sobre este documento

Este guia é vivo. Mudanças de convenção (nomenclatura, estrutura de pastas)
são atualizadas aqui via PR normal. Mudanças de **arquitetura** (nova
tecnologia, novo padrão de comunicação entre serviços) exigem uma ADR além
da atualização deste guia.

---

## 11. Engineering Principles

- Clareza antes de esperteza: o código precisa ser legível por qualquer pessoa do time.
- Coesão alta e acoplamento baixo entre módulos e serviços.
- Preferir soluções simples e evolutivas (YAGNI e KISS).
- Contrato explícito entre camadas e entre serviços.
- Testabilidade é requisito de design, não atividade posterior.

> Java 21 LTS e Spring Boot 3.3.x permanecem como baseline oficial até nova ADR.

---

## 12. Flyway

- Todo serviço com banco versiona schema com migrações Flyway.
- Nunca alterar migração já aplicada em ambiente compartilhado.
- Convenção de nome: `V<numero>__<descricao>.sql`.
- Uma migração deve ser pequena, reversível por estratégia de rollout e auditável.

---

## 13. MapStruct

- Usar MapStruct para mapeamentos repetitivos entre `domain` e `dto`.
- Regras de negócio não ficam no mapper.
- Mapeamentos complexos devem ter testes unitários dedicados.

---

## 14. Logging Guidelines

- Logging estruturado com contexto mínimo: `service`, `traceId`, `operation`.
- `INFO` para eventos de negócio relevantes, `DEBUG` para diagnóstico local, `ERROR` para falhas.
- Nunca registrar segredos, tokens, senhas ou dados pessoais sensíveis.
- Mensagens de log devem ser objetivas e acionáveis.

---

## 15. Code Style

- Seguir convenções Java do time com formatação automática no commit.
- Métodos curtos, nomes sem abreviações obscuras e sem comentários redundantes.
- Evitar classes utilitárias genéricas sem dono de domínio.
- Preferir imutabilidade sempre que viável.

---

## 16. Branch Strategy

- `main`: apenas código estável e pronto para release.
- `feature/<escopo-curto>` para desenvolvimento de histórias.
- Pull Request obrigatório para merge em `main`.
- Branches longas devem ser evitadas; integração frequente é padrão.

---

## 17. Semantic Versioning

- Releases seguem SemVer: `MAJOR.MINOR.PATCH`.
- `MAJOR`: quebra de compatibilidade.
- `MINOR`: funcionalidade nova compatível.
- `PATCH`: correções sem quebra.

---

## 18. Dependency Guidelines

- Preferir bibliotecas maduras, com manutenção ativa e adoção ampla.
- Toda dependência nova deve ter justificativa técnica no PR.
- Evitar sobreposição de bibliotecas para a mesma responsabilidade.
- Atualizações de segurança têm prioridade sobre novas features.

---

## 19. Code Review Checklist

- Regras de negócio e critérios de aceite foram atendidos.
- Contratos de API e tratamento de erro estão consistentes.
- Testes cobrem casos principais e falhas esperadas.
- Sem vazamento de detalhes internos entre camadas.
- Sem débito técnico crítico introduzido sem plano explícito.

---

## 20. Performance Guidelines

- Evitar N+1 em acesso a dados.
- Paginação obrigatória para listagens potencialmente grandes.
- Operações externas devem ter timeout definido.
- Medir antes de otimizar: decisões de performance precisam de evidência.

---

## 21. Security Guidelines

- Validar entradas em toda fronteira externa.
- Princípio do menor privilégio para acesso a recursos.
- Dependências e imagens devem ser atualizadas para correções de segurança.
- Não expor stacktrace e detalhes internos em respostas de erro públicas.
