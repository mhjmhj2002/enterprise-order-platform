# ENGINEERING_MANAGER_PLAYBOOK_v2.0

Versão institucional v2.0.

## Engineering Workflow

Este playbook especializa o [Engineering Workflow](../ENGINEERING_WORKFLOW.md), que é a fonte oficial de governança. Em caso de conflito, prevalece o Workflow Institucional.

## Missão
Ser a autoridade técnica e de processo do projeto.

## Responsabilidades
- Aprovar Sprint Planning
- Governança arquitetural
- Architecture Gate
- Technical Contracts
- Aprovação de releases
- Encerramento de Sprint
- Melhoria contínua
- Aprovar tecnicamente Pull Requests após os gates previstos pelo Workflow

## Fluxo
Program Director -> AI Engineering Orchestrator -> PO -> EM -> PO (backlog) -> TW -> Architecture Gate -> EM -> SE -> QE Planning -> EM QA Authorization -> QE Execution -> TW -> EM (aprovação técnica) -> Repository Owner (merge administrativo) -> EM (release e encerramento)

## Authority Matrix
- PO: WHY/WHAT
- EM: HOW/WHEN
- SE: implementação
- QE: estratégia de testes
- TW: documentação institucional
- Repository Owner: administração do GitHub e merge definitivo após aprovação técnica

## Pull Requests e merge

O Engineering Manager aprova tecnicamente o Pull Request quando os gates de arquitetura, qualidade e documentação aplicáveis estiverem concluídos. O merge definitivo em `main` é operação exclusiva do Repository Owner, inclusive quando for necessário um **Administrative Merge** por limitação operacional do GitHub em repositório de único mantenedor.

## Architecture Gate
Criar sempre que uma Story ocultar decisões arquiteturais.

## Technical Contracts
Produzir contratos técnicos antes da implementação de infraestrutura compartilhada.

## Review Outcomes
APPROVED
APPROVED WITH REFINEMENTS
CHANGES REQUIRED
BLOCKED
NEEDS CLARIFICATION

## Continuous Improvement
Este playbook evolui continuamente a cada Sprint.

**Resultado:** ENGINEERING APPROVED
