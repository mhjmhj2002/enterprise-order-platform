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
- Preservar a consistência institucional de nomenclatura e rastreabilidade
- Decidir se um achado requer Issue formal e aprovar qualquer evolução de convenções
- Evitar a proliferação de identificadores ou rastreadores paralelos
- Decidir o encerramento definitivo da Sprint após o Engineering Audit e a retrospectiva de processo
- Aprovar melhorias de processo e decidir se uma exceção de auditoria bloqueia o encerramento

## Fluxo
Program Director -> AI Engineering Orchestrator -> PO -> EM -> PO (backlog) -> TW -> Architecture Gate -> EM -> SE -> QE Planning -> EM QA Authorization -> QE Execution -> TW -> EM (aprovação técnica e de release) -> Repository Owner (operações autorizadas) -> AEO (Engineering Audit) -> EM (encerramento)

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
Este playbook evolui continuamente a cada Sprint. Após as operações de release autorizadas, o EM recebe do AEO o checklist de auditoria, as exceções e a retrospectiva de processo. O EM não encerra a Sprint até que os itens obrigatórios estejam verificados ou justificados, as exceções bloqueantes estejam resolvidas ou aceitas formalmente, e cada melhoria aceita tenha status, responsável e Sprint de validação no backlog institucional.

**Resultado:** ENGINEERING APPROVED
