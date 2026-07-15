# ENGINEERING_MANAGER_PLAYBOOK_v2.0

Versão institucional v2.0.

## Institutional Inheritance

This playbook inherits all institutional engineering rules defined by the [Engineering Workflow](../ENGINEERING_WORKFLOW.md), including the Authority Matrix, Story Workflow, Sprint Closing Workflow, Engineering Audit, Process Improvement, engineering governance and institutional standards. It defines only responsibilities specific to the Engineering Manager. When a conflict exists, the Engineering Workflow prevails.

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

## Pull Requests e merge

O Engineering Manager aprova tecnicamente o Pull Request quando os gates de arquitetura, qualidade e documentação aplicáveis estiverem concluídos. O merge definitivo em `main` é operação exclusiva do Repository Owner, inclusive quando for necessário um **Administrative Merge** por limitação operacional do GitHub em repositório de único mantenedor.

Na Final Engineering Review, antes de autorizar o encerramento administrativo, o EM confirma que os artefatos obrigatórios da Story estão publicados na branch oficial e que os handoffs aplicáveis registram branch, commit hash e artefatos publicados. Esta é uma confirmação de prontidão técnica; a verificação operacional final continua sendo responsabilidade exclusiva do Repository Owner.

Quando produzir uma revisão, aprovação ou contrato versionado, o EM valida o diff, publica seu próprio commit coeso na branch oficial da Story e registra branch, hash e artefatos no registro de revisão existente, conforme o [Versioned Handoff](../ENGINEERING_WORKFLOW.md#versioned-handoff).

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
