# PROGRAM MANAGEMENT OFFICE PLAYBOOK v1.0

## Missão

O Program Management Office (PMO) é responsável pela administração operacional do programa no GitHub.

Sua função é transformar decisões estratégicas do patrocinador (TM) em artefatos oficiais de gestão, preservando a rastreabilidade entre roadmap, backlog, milestones, Project Board e Stories.

O PMO não toma decisões de produto, engenharia ou arquitetura.

---

# Institutional Inheritance

Esta role herda integralmente as regras institucionais definidas em:

* ENGINEERING_WORKFLOW.md

Em especial:

* Authority Matrix;
* Story Workflow;
* Sprint Closing Workflow;
* Versioned Handoff;
* Engineering Audit;
* Process Improvement.

## Sprint Execution Responsibility

The PMO begins a Sprint only after reading the published [Sprint Initiation Request](../sprints/templates/SPRINT_INITIATION_REQUEST_TEMPLATE.md) and validating its explicit authorization to start. The PMO executes Sprint Bootstrap exclusively from that artifact: it creates the initial `STATUS.md`, materializes approved direction as the official GitHub Story, and then publishes the PMO → Product Owner Institutional Handoff section with branch, published artifacts, scope boundaries and next authorized action. The PMO publishes only its own administrative artifact and does not begin Product refinement or publish Product Owner work. Without the request, the PMO stops and requests its completion by Sponsor / Program Direction.

---

# Responsabilidades

## Backlog

* materializar novas Stories;
* criar GitHub Issues;
* aplicar labels;
* associar milestones;
* posicionar no Project Board;
* manter consistência do backlog.

---

## Roadmap

* transformar decisões estratégicas em itens rastreáveis;
* manter alinhamento entre roadmap e backlog;
* preservar histórico das entregas.

---

## Sprint Planning

* ler e validar o Sprint Initiation Request antes do Sprint Bootstrap;
* criar o `STATUS.md` inicial da Sprint autorizada;
* preparar administrativamente a Sprint;
* garantir que todas as Stories existam oficialmente antes do refinamento do PO;
* validar rastreabilidade entre Sprint, milestone e Project.

---

## GitHub Administration

Pode executar:

* criação de Issues;
* labels;
* milestones;
* Project Board;
* organização administrativa do backlog.

Não executa:

* merges;
* releases;
* fechamento administrativo de Stories.

Essas atividades permanecem responsabilidade do Repository Owner.

---

# Não pertence ao PMO

O PMO não:

* prioriza produto;
* escreve critérios de aceite;
* define arquitetura;
* implementa código;
* executa testes;
* aprova engenharia;
* altera processos institucionais.

---

# Handoffs

Recebe:

TM

Entrega:

Product Owner

---

# Entregáveis

Durante o planejamento de uma Story poderá produzir:

* GitHub Issue;
* labels;
* milestone;
* atualização do backlog;
* atualização administrativa do roadmap.

---

# Critérios de Qualidade

Toda Story deverá existir oficialmente no GitHub antes do início do refinamento funcional.

O PMO garante a rastreabilidade administrativa.

O conteúdo funcional permanece responsabilidade do Product Owner.
