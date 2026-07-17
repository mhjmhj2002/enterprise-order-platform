# ADR-008: Baseline de autenticação HTTP Basic stateless

## Status

Proposto para aprovação arquitetural da Story #46 — 2026-07-17.

## Contexto

A Story #46 exige que um consumidor de API demonstre autenticação válida antes
de criar, alterar ou consultar informações de negócio nos serviços Catalog,
Inventory e Order. Para o consumidor autenticado, os comportamentos funcionais
existentes devem ser preservados, inclusive o fluxo de pedido confirmado e o
processamento de `OrderConfirmed` v1.

A documentação de baseline identificou as superfícies REST de negócio e
encaminhou a decisão técnica ao [Architecture Gate da Story #46](../contracts/STORY_046_ARCHITECTURE_GATE.md).
O gate recomenda uma solução uniforme, pequena e verificável, sem criar
usuários, perfis, autorização por recurso ou gestão de identidade.

## Decisão

Adotar HTTP Basic stateless como barreira de autenticação comum aos serviços
Catalog, Inventory e Order.

- Spring Security protegerá todas as rotas de negócio sob `/api/v1/**`.
- Haverá uma única credencial técnica de consumidor de API por ambiente,
  fornecida exclusivamente por `SECURITY_API_USERNAME` e
  `SECURITY_API_PASSWORD`. As variáveis são obrigatórias, não possuem valor
  padrão e seus valores não serão versionados.
- A API será sem sessão e sem CSRF, pois não usa autenticação baseada em
  cookies. Falhas de autenticação ausente, inválida ou malformada retornarão
  `401 Unauthorized` genérico com `WWW-Authenticate`, antes de controller,
  validação, caso de uso ou acesso a dados.
- `GET /actuator/health`, OpenAPI, Swagger UI e seus recursos necessários são
  exceções técnicas públicas. Kafka, consumers e workers locais não fazem parte
  desta fronteira HTTP.
- O Order incluirá a mesma credencial configurada nas chamadas REST existentes
  ao Inventory. Não propagará a credencial recebida de um consumidor, e
  preservará `X-Correlation-Id`, métodos, paths, payloads e a semântica de
  erros de negócio para chamadas autenticadas.

## Alternativas consideradas

### API key customizada

Rejeitada porque exigiria protocolo e filtro proprietários, sem benefício para
a única categoria de acesso aprovada.

### JWT, OAuth2 ou provedor de identidade

Adiados: requerem emissor, ciclo de vida e decisões de identidade que excedem
o escopo da Story.

### Usuários persistidos, roles ou ACL

Rejeitados porque introduzem regra de produto e autorização granular não
aprovadas.

### Bypass sem autenticação para chamadas Order → Inventory

Rejeitado: deixaria uma rota de negócio fora da barreira uniforme e criaria
uma exceção de confiança não necessária.

## Consequências

### Positivas

- A proteção uniforme por `/api/v1/**` reduz o risco de omitir uma rota de
  negócio atual ou futura.
- Uma chamada independente permite demonstrar de forma reproduzível tanto a
  recusa não autenticada quanto o comportamento autenticado preservado.
- A integração REST atual entre Order e Inventory e o contrato `OrderConfirmed`
  v1 permanecem compatíveis, sem nova dependência runtime, banco compartilhado
  ou evento.

### Limitações e riscos aceitos

- Uma credencial compartilhada não fornece identidade individual, auditoria,
  revogação granular, roles ou autorização por recurso.
- HTTP Basic somente é adequado fora do ambiente local sobre canal HTTPS
  confiável. TLS remoto, terminação de transporte, gateway, rotação de
  credenciais, tokens e federação de identidade exigem decisão futura de
  infraestrutura e segurança.
- Credenciais não podem ser registradas em repositório, logs, respostas,
  exemplos, coleções públicas ou evidências. A execução e os testes devem
  injetá-las somente no ambiente.

## Referências

- [Sprint 5 Product Plan](../../team/sprints/SPRINT_5_PRODUCT_PLAN.md)
- [Story #46 Documentation Baseline](../contracts/STORY_046_DOCUMENTATION_BASELINE.md)
- [Story #46 Architecture Gate](../contracts/STORY_046_ARCHITECTURE_GATE.md)
- [Service Boundaries](../SERVICE_BOUNDARIES.md)
- [Event Platform Technical Contract](../contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md)

## Institutional Handoff — Technical Writer → Engineering Manager

### Executive summary

O ADR obrigatório da decisão transversal de segurança foi publicado a partir
do insumo do Architecture Gate, sem alteração de mecanismo, superfícies,
compatibilidade ou escopo funcional.

### Published artifacts

- `docs/architecture/ADR/ADR-008-http-basic-authentication-baseline.md`
- `docs/architecture/ADR/README.md`
- `docs/architecture/README.md`
- `docs/team/sprints/sprint-005/STATUS.md`

### Evidence and constraints

- O ADR registra HTTP Basic stateless, credencial técnica por ambiente,
  proteção de `/api/v1/**`, exceções técnicas explícitas e compatibilidade
  Order → Inventory conforme o Architecture Gate recomendado.
- Mantém os contratos de negócio e `OrderConfirmed` v1; não autoriza
  implementação, Quality, TLS remoto, gestão de identidade ou autorização
  granular.

### Pending items

- Engineering Manager: executar nova Architecture Approval sobre este ADR e o
  Architecture Gate completo.

### Next authorized action

- **Next role:** Engineering Manager.
- **Required action:** revisar o ADR-008 e o Architecture Gate da Story #46 e
  registrar aprovação ou bloqueio arquitetural explícito.
- **Acceptance / stop criteria:** aprovar somente se a decisão preservar os
  limites de produto, contratos REST/Kafka e condições de evidência; parar e
  escalar qualquer demanda por infraestrutura remota, identidade ou
  autorização granular. Não autorizar implementação sem parecer `APPROVED`.

### Versioned reference

Registrada no `STATUS.md` após a publicação do commit desta documentação.
