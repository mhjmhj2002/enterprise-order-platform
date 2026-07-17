# Story #46 — Documentation Baseline: Segurança Inicial

**Status:** READY FOR ARCHITECTURE GATE

**Data:** 2026-07-17

**Story:** [#46 — Baseline inicial de segurança](https://github.com/mhjmhj2002/enterprise-order-platform/issues/46)

**Fonte funcional:** [Sprint 5 Product Plan](../../team/sprints/SPRINT_5_PRODUCT_PLAN.md), aprovado pelo Engineering Manager.

## 1. Objetivo documental

Esta baseline torna explícita a documentação que o Architecture Gate deve usar
para decidir uma barreira inicial de autenticação nas APIs de negócio. Ela não
escolhe protocolo, provedor, biblioteca, modelo de credencial, armazenamento,
configuração, endpoint técnico ou política de autorização.

O resultado funcional a preservar é simples: uma solicitação sem autenticação
válida não acessa operações ou informações de negócio e não produz efeito de
negócio; o consumidor autenticado mantém os comportamentos funcionais já
aprovados.

## 2. Referências sincronizadas

| Referência | Relevância para a Story #46 | Situação |
| --- | --- | --- |
| [Sprint 5 Product Plan](../../team/sprints/SPRINT_5_PRODUCT_PLAN.md) | Fonte de escopo, critérios funcionais, riscos e limites. | Sincronizada; não há divergência identificada. |
| [Architecture Overview](../ARCHITECTURE.md) | Define os contextos Catalog, Inventory e Order e a baseline híbrida REST + Kafka. | A barreira não pode alterar a semântica de negócio, `OrderConfirmed` v1 ou o fluxo já entregue. |
| [Service Boundaries](../SERVICE_BOUNDARIES.md) | Define ownership e dependência REST existente do Order para o Inventory. | Não autoriza banco compartilhado, nova dependência runtime ou mudança de ownership. |
| [Event Platform Technical Contract](EVENT_PLATFORM_TECHNICAL_CONTRACT.md) | Define o contrato vigente de `OrderConfirmed` v1. | Não há mudança funcional de evento, tópico, envelope, producer ou consumer nesta baseline. |
| [Story #34 Architecture Gate](STORY_034_ARCHITECTURE_GATE.md) e [Story #44 Architecture Gate](STORY_044_ARCHITECTURE_GATE.md) | Registram garantias de confiabilidade e consulta operacional já aprovadas. | Suas APIs de negócio e evidências devem permanecer acessíveis ao consumidor autenticado. |
| OpenAPI local e controllers dos três serviços | Fonte técnica atual das superfícies REST expostas. | Requer inventário e decisão de proteção no Architecture Gate. |

## 3. Superfícies documentadas a considerar

As superfícies de negócio atualmente expostas pertencem aos três contextos:

| Contexto | Superfícies REST de negócio existentes | Resultado documental requerido |
| --- | --- | --- |
| Catalog | `/api/v1/products`, `/api/v1/skus` e operações de produto/SKU. | Definir no contrato técnico como a barreira cobre leitura e escrita sem alterar contratos de negócio. |
| Inventory | `/api/v1/inventory`, reservas, ajustes, processamento e observações de `OrderConfirmed`. | Preservar operações de estoque e as consultas operacionais aprovadas para consumidor autenticado. |
| Order | `/api/v1/orders`, comandos de checkout e `/api/v1/customers/{customerId}/orders`. | Preservar o fluxo de pedido confirmado e a integração já aprovada com Inventory. |

Esta tabela é um inventário de impacto, não uma decisão de que toda rota
técnica, operacional ou de framework deve receber o mesmo tratamento. A
classificação de superfícies que não sejam de negócio é uma decisão pendente do
Architecture Gate.

## 4. Lacunas que exigem decisão no Architecture Gate

1. Mecanismo de autenticação e forma verificável de demonstrar uma identidade
   válida, incluindo o ciclo de vida mínimo necessário para execução local e
   testes independentes.
2. Fronteira precisa entre APIs de negócio e endpoints técnicos/operacionais,
   incluindo health, documentação OpenAPI e demais superfícies de framework.
3. Aplicação consistente da barreira nos três serviços, sem introduzir nova
   regra de negócio, autorização granular, acesso a dados de outro contexto ou
   acoplamento runtime adicional.
4. Semântica HTTP para autenticação ausente ou inválida e requisito de que a
   recusa ocorra antes de qualquer efeito ou exposição de informação de
   negócio, preservando os erros de negócio existentes para chamadas
   autenticadas.
5. Estratégia de compatibilidade para as chamadas REST internas já existentes
   entre Order e Inventory e para o fluxo `OrderConfirmed` v1, caso sejam
   afetadas pela fronteira de segurança escolhida.
6. Forma de documentar e reproduzir evidências independentes de recusa e de
   sucesso autenticado, sem registrar segredos em repositório, exemplos ou
   relatórios.

## 5. Limites documentais e de escopo

- Não há ADR nesta etapa: ainda não existe decisão arquitetural aprovada para
  registrar. O Architecture Gate deverá indicar se a decisão aprovada exige
  ADR.
- Esta baseline não altera OpenAPI, coleções Postman, README, contratos de
  evento, changelog ou release notes. Esses artefatos só serão atualizados
  quando a implementação e os contratos aprovados determinarem conteúdo
  verificável.
- Papéis, permissões granulares, usuários, gestão de credenciais, API Gateway,
  SSO, federação, auditoria de acesso, alertas e resposta a incidentes seguem
  fora do escopo da Story #46.
- Nenhuma implementação, Quality ou autorização de implementação decorre deste
  documento.

## 6. Critérios documentais para a próxima etapa

O Architecture Gate deve publicar um contrato técnico que, no mínimo:

- delimite as superfícies de negócio protegidas e as exceções técnicas, se
  existirem, com justificativa;
- escolha e justifique o mecanismo técnico sem converter a escolha em regra de
  produto;
- preserve explicitamente os contratos REST e `OrderConfirmed` v1 que não
  forem alterados;
- registre os riscos de compatibilidade das chamadas Order → Inventory;
- defina evidência reproduzível para uma recusa não autenticada, uma operação
  autenticada e o fluxo autenticado de pedido confirmado; e
- indique ADRs e atualizações de API/documentação que passarão a ser exigidos
  após a decisão.

## Institutional Handoff — Technical Writer → Architecture Gate

### Executive summary

A documentação de produto, arquitetura e contratos vigentes foi sincronizada
para a Story #46. A Story está pronta para análise arquitetural, com as
lacunas técnicas registradas sem ampliar o escopo funcional aprovado.

### Published artifacts

- `docs/architecture/contracts/STORY_046_DOCUMENTATION_BASELINE.md`
- `docs/architecture/README.md`
- `docs/team/sprints/sprint-005/STATUS.md`

### Scope and constraints

- A exigência funcional é autenticação para operações e informações de negócio
  nos contextos Catalog, Inventory e Order.
- A baseline preserva os comportamentos existentes do consumidor autenticado,
  inclusive o fluxo `OrderConfirmed` v1.
- Mecanismos técnicos, fronteiras de endpoints e compatibilidade de integrações
  internas são decisões exclusivas do Architecture Gate.
- Não autoriza implementação, Quality nem alteração de escopo de produto.

### Evidence

- Plano funcional aprovado e Issue #46 equivalentes, conforme handoff do
  Product Owner.
- Referências de arquitetura, limites de serviço, contrato de eventos e gates
  anteriores revisados; não foi identificada contradição funcional.
- Controllers atuais confirmam superfícies REST de negócio em Catalog,
  Inventory e Order que precisam ser classificadas pelo contrato técnico.

### Risks and pending items

- Uma proteção parcial, uma exceção técnica não documentada ou uma mudança nas
  chamadas Order → Inventory pode violar os critérios de aceite.
- O Technical Lead deve publicar o Architecture Gate e encaminhá-lo ao
  Engineering Manager para aprovação explícita antes de implementação.

### Next authorized action

- **Next role:** Technical Lead.
- **Required action:** produzir o Architecture Gate e o contrato técnico da
  Story #46 a partir das lacunas desta baseline.
- **Acceptance / stop criteria:** interromper e devolver ao Product Owner se a
  decisão exigir novo requisito de produto; interromper e escalar ao
  Engineering Manager se não for possível preservar os contratos e limites
  aprovados. Não autorizar implementação.

### Versioned reference

Será preenchida com branch e commit após a publicação desta baseline.
