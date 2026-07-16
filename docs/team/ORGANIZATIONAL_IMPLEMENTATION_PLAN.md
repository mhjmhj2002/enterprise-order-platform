# Organizational Implementation Plan

**Owner:** AI Engineering Orchestrator  
**Decision authority:** Engineering Manager  
**Status:** Implemented — pending Engineering Manager Organizational Validation
**Target:** Complete implementation and organizational validation before Sprint 5 kickoff

## 1. Purpose and implementation boundary

This plan translates the approved Sprint 4 retrospective decisions — PI-004, PI-005, the Sprint Execution Protocol and Organizational Freeze — into an ordered implementation plan. Its source is the [Sprint 4 Engineering Retrospective](SPRINT_4_ENGINEERING_RETROSPECTIVE.md).

The planned organizational artifacts are implemented; the Engineering Manager must approve the implemented result before the protocol becomes mandatory or Sprint 5 may begin. This document does not alter product scope.

## 2. Target operating model

The target model makes the authorized next action visible and published at every institutional stage:

```text
Organizational Validation → Organizational Freeze
  → Sprint Initiation Request → PMO Sprint Bootstrap → initial STATUS.md / materialization → Product Owner → EM functional review
  → Technical Lead / Architecture Gate → EM architecture approval
  → Software Engineer → Quality Engineer → Technical Writer
  → EM final review → Repository Owner → AEO audit → EM closure
```

Before acting, a role reads the current Sprint state and its inbound published handoff section. After acting, it updates the state, publishes its own artifact containing the outbound handoff section and stops. This adds operational clarity to the existing workflow; it does not add product gates, parallel trackers or new roles.

## 3. Sprint workspace

### Proposed structure

For each new Sprint, create a predictable documentation package at `docs/team/sprints/sprint-<number>/`:

```text
sprint-<number>/
├── STATUS.md
├── REPORTS/
├── AUDIT/
└── RETROSPECTIVE/
```

Existing Sprint 0–4 documents remain where they are; no historical migration is required. The implementation should define a stable numerical convention (for example, `sprint-005`) before Sprint 5 is created.

| Location | Purpose | Updating / use responsibility |
| --- | --- | --- |
| `STATUS.md` | Living, authoritative operational state for the current Sprint and Story. | Current responsible role updates it before publishing its handoff; every receiving role reads it at startup. |
| `REPORTS/` | Sprint-level reports that do not belong to a role-owned gate artifact, such as closure-readiness evidence. | Producing role; TW validates links and document consistency. |
| `AUDIT/` | Engineering Audit report, acceptance and supporting audit evidence. | AEO produces audit artifacts; EM records acceptance. |
| `RETROSPECTIVE/` | Engineering Retrospective and its decision record. | AEO prepares process evidence; EM records decisions. |

Every Institutional Handoff Package is a standardized Markdown section of the role's existing institutional artifact or PR: for example, the Product Plan, Engineering Review, Architecture Gate, Test Report, Documentation Review, Final Engineering Review, Engineering Audit or GitHub Issue/PR. No `HANDOFFS/` directory, standalone handoff document or parallel handoff record is created.

## 4. Sprint Execution Protocol

### Mandatory startup sequence

On an explicit invocation such as “Sua vez”, “Start” or “Iniciar”, every participating role must:

1. read the current Sprint `STATUS.md`;
2. confirm that the `Role responsible` and `Current step` authorize that role to act;
3. if not authorized, state the role and step that must act next, then end execution without changing artifacts;
4. read the applicable inbound Institutional Handoff Package section and its published reference;
5. if the handoff is absent, unpublished or inconsistent, stop and request return to the producing role;
6. execute only the authorized activity within the inherited Engineering Workflow and role playbook;
7. update `STATUS.md` with the resulting state, evidence and next authorized action;
8. add the outbound Institutional Handoff Package section in Markdown to the role's own institutional artifact or PR;
9. complete the Versioned Handoff by committing, pushing and recording the published branch and commit; and
10. end execution after naming the next role and handoff location.

The implementation must place this protocol once in the Engineering Workflow. Each playbook receives only the role-specific outbound and inbound handoff obligations that differ from the shared protocol.

### Publication ownership

Every role produces, versions, publishes and records the Versioned Handoff for its **own** artifact. A receiving role must not publish, amend or take ownership of another role's unpublished deliverable. The Repository Owner remains exclusively responsible for the GitHub administrative operations assigned by the Engineering Workflow; it publishes only its own administrative evidence and never publishes an artifact on another role's behalf.

## 5. STATUS.md standard

### Required header fields

| Field | Required content |
| --- | --- |
| Sprint | Sprint identifier, goal and workspace path. |
| Current Story | GitHub Issue number, title and approved scope reference. |
| Current step | Institutional stage currently authorized to act. |
| Previous step | Last completed stage and published handoff reference. |
| Next step | Next authorized stage and receiving role. |
| Role responsible | Only role permitted to act now. |
| Flow status | Visual sequence using `TODO`, `DOING`, `DONE`, `BLOCKED` or `N/A`. |
| Current branch | Official branch and base branch. |
| Pull Request | PR number/URL/status, or `N/A` with rationale. |
| Current gate | Applicable gate, decision and blocking conditions. |
| Last updated | ISO date/time, producing role and published commit. |

### Visual flow

The template should show the institutional chain in a compact table. A recommended baseline is:

| Step | Role | State | Published handoff |
| --- | --- | --- | --- |
| PMO materialization | PMO | TODO / DOING / DONE | |
| Product refinement | Product Owner | TODO / DOING / DONE | |
| Functional review | Engineering Manager | TODO / DOING / DONE | |
| Architecture Gate | Technical Lead | TODO / DOING / N/A | |
| Architecture approval | Engineering Manager | TODO / DOING / N/A | |
| Implementation | Software Engineer | TODO / DOING / DONE | |
| Quality | Quality Engineer | TODO / DOING / DONE | |
| Documentation | Technical Writer | TODO / DOING / DONE | |
| Final review | Engineering Manager | TODO / DOING / DONE | |
| Administrative closure | Repository Owner | TODO / DOING / DONE | |
| Engineering Audit | AEO | TODO / DOING / DONE | |
| Institutional acceptance | Engineering Manager | TODO / DOING / DONE | |

The status file is a living operational artifact and is expressly excluded from the Organizational Freeze.

The PMO creates the initial status only after Organizational Validation, Organizational Freeze and the authorized Sprint Initiation Request. Its flow records that sequence before PMO Sprint Bootstrap and the normal Story stages.

## 6. Institutional Handoff Package standard

Every package is Markdown and uses the same section template embedded in the producing role's existing institutional artifact or PR. A package is never a standalone document.

```markdown
## Institutional Handoff — <source role> → <destination role>

### Executive summary
<what is ready and why>

### Objective completed
<approved objective / scope completed>

### Published artifacts
- <artifact path or PR>

### Versioned reference
- Branch: `<branch>`
- Commit: `<short hash>`
- Full hash: `<full hash>`

### Evidence and constraints
<evidence, known limits, risks and applicable gate conditions>

### Pending items
<none, or explicit owner and blocking/non-blocking status>

### Next authorized action
- Next role: `<role>`
- Required action: `<action>`
- Acceptance / stop criteria: `<criteria>`
- Operational command: `<precise command or instruction to continue>`
```

The package must not restate the entire Story, replace a gate decision or authorize work beyond the workflow. Its purpose is to identify the published state, constraints and next authorized action.

## 7. Engineering Workflow implementation map

The implementation should update only the following workflow areas:

| Workflow area | Planned change |
| --- | --- |
| Institutional Artifacts | Add the Sprint workspace and STATUS.md concepts with ownership; retain existing artifact ownership and embed handoffs in those artifacts. |
| Sprint Lifecycle | Add the PMO materialization / entry-state prerequisite before Product refinement. |
| Official Handoffs / Versioned Handoff | Require the startup check and an outbound handoff section in the producing role's existing artifact or PR. Require every role to publish and record its own artifact. |
| Pull Request Workflow | Require the active `STATUS.md` and applicable published handoffs in the PR evidence; do not create a new review gate. |
| Sprint Closing Workflow | Add the closure-readiness state and Repository Owner handoff section as required evidence before audit starts. |
| Continuous Improvement | Add Organizational Freeze and the Candidate Improvement route for ideas discovered during a Sprint. |

The workflow must define the common protocol once. It must not repeat role-specific operational detail or create a separate approval sequence.

## 8. Playbook implementation map

All existing playbooks continue to inherit the common protocol. Update a playbook only to specify its own handoff responsibilities.

| Role | Planned role-specific addition |
| --- | --- |
| PMO | Materialize the official Issue and publish its PMO → PO handoff section before any Product refinement. |
| Product Owner | Confirm entry state; publish the refined plan with its PO → EM handoff section. |
| Engineering Manager | Publish Functional Review, Architecture Approval, Final Review and closure/acceptance artifacts with the applicable outbound handoff section; decide emergency freeze exceptions. |
| Technical Lead | Publish Architecture Gate with its handoff section containing recommendation, conditions and stop criteria. |
| Software Engineer | Publish implementation/PR with its handoff section containing scope, tests, constraints and QE start conditions. |
| Quality Engineer | Publish Quality Gate artifact with its handoff section containing evidence, recommendation, defects and TW requirements. |
| Technical Writer | Publish Documentation Gate with its handoff section containing synchronization result, remaining documentation limits and EM requirements. |
| Repository Owner | Perform only assigned GitHub administration and publish its own closure-readiness evidence section with PR, issue, Board, milestone, release, branch, `main` and worktree state for AEO. |
| AI Engineering Orchestrator | Maintain workspace conventions; validate startup/handoff evidence in the audit; publish the Engineering Audit with its AEO → EM handoff section. |
| Program / TM role, if represented by a playbook | Provide approved strategic direction to PMO before Story materialization. |

The implementation should also add the PMO playbook to the role index if it is intended to remain an active institutional role, but must not invent any new role.

## 9. Engineering Audit implementation map

Extend the existing checklist with only the following verifications:

| Audit verification | Evidence source |
| --- | --- |
| `STATUS.md` exists, is published, current and names the correct responsible role and next action. | Sprint workspace status and commit reference. |
| Each applicable institutional transition has a published handoff section in the producing role's existing artifact or PR. | Linked Issue, plan, review, gate, report, audit or PR. |
| Section source/destination, branch, commit and artifacts match the Versioned Handoff. | Existing artifact/PR and official Git history. |
| Receiving role acted only after the inbound section was published. | Section timestamp/commit and existing handoff chronology evidence. |
| Startup Protocol is evidenced by the status transition and inbound-section reference. | STATUS.md stage record and source artifact/PR. |
| Repository Owner closure-readiness section records required release, milestone, `main`, branch and worktree state before audit. | Repository Owner's existing closure-readiness artifact and GitHub evidence. |
| Organizational Freeze was observed, or any EM emergency exception is recorded with rationale, scope and effective date. | STATUS.md and Candidate Improvement Backlog / exception record. |

These checks validate evidence already produced by roles; they do not cause the AEO to reperform reviews, quality validation or repository administration.

## 10. Templates to create during implementation

| Proposed location | Template | Purpose |
| --- | --- | --- |
| `docs/team/sprints/templates/STATUS_TEMPLATE.md` | Sprint status | Creates the living state and visual flow for each new Sprint. |
| `docs/team/sprints/templates/SPRINT_INITIATION_REQUEST_TEMPLATE.md` | Sprint Initiation Request | First Versioned Handoff from Sponsor / Program Direction to PMO; authorizes and bounds Sprint Bootstrap. |
| `docs/team/sprints/templates/INSTITUTIONAL_HANDOFF_SECTION.md` | Handoff section | Provides the common Markdown package section for existing artifacts/PRs. |
| `docs/team/sprints/templates/ARCHITECTURE_GATE_TEMPLATE.md` | Architecture Gate extension | Adds the standard inbound/outbound package section to the existing gate format. |
| `docs/team/sprints/templates/QUALITY_GATE_TEMPLATE.md` | Quality Gate extension | Adds the standard handoff section to Test Plan/Report or quality consolidation. |
| `docs/team/sprints/templates/DOCUMENTATION_GATE_TEMPLATE.md` | Documentation Gate extension | Adds synchronized-document status and EM handoff content. |
| `docs/team/sprints/templates/FINAL_ENGINEERING_REVIEW_TEMPLATE.md` | Final review extension | Adds Repository Owner authorization and next-state handoff content. |
| `docs/team/sprints/templates/CLOSURE_READINESS_TEMPLATE.md` | Closure readiness | Captures RO evidence needed before audit. |

Templates are reusable formatting aids, not gates or new systems of record. Their implementation must preserve existing Story/Issue, test-scenario, evidence and PI identifiers.

## 11. Organizational Freeze plan

### Frozen from Sprint kickoff through institutional closure

- `docs/team/ENGINEERING_WORKFLOW.md`;
- role playbooks under `docs/team/roles/`;
- `docs/team/ENGINEERING_AUDIT_CHECKLIST.md`;
- Sprint Execution Protocol text and mandatory templates under `docs/team/sprints/templates/`; and
- workspace-structure conventions.

### Not frozen

- current Sprint `STATUS.md` and published handoffs;
- Story plans, gates, reports and closure evidence, which evolve through the approved workflow;
- product backlog and technical artifacts under their existing authority; and
- Candidate Improvement Backlog entries, which may be recorded but cannot change active Sprint rules.

New process ideas are recorded in the Candidate Improvement Backlog, reviewed exclusively in the Engineering Retrospective, converted to PI only by an explicit EM decision, and implemented only before the next Sprint. An emergency exception requires EM authorization and a published STATUS.md record containing rationale, scope and effective date.

## 12. Sequential implementation checklist

| Order | Document / action | Responsible | Impact | Dependencies |
| ---: | --- | --- | --- | --- |
| 1 | Confirm PMO role/index status and approved Sprint-5 workspace naming. | EM + AEO | Prevents ambiguous entry ownership and paths. | Retrospective approval. |
| 2 | Add PI-004 and PI-005 with approved decision, evidence and Sprint-5 validation target. | AEO | Makes authorized work traceable. | Orders 1 and retrospective record. |
| 3 | Add workspace and template structure. | TW, with AEO design input | Establishes reusable artifacts without changing active Sprint. | Order 1. |
| 4 | Update Engineering Workflow with shared Sprint Execution Protocol, workspace ownership, handoff rule and freeze. | AEO; TW consistency review | Establishes the sole common rule source. | Orders 2–3. |
| 5 | Update Engineering Audit Checklist with protocol/freeze evidence checks. | AEO | Enables Sprint-5 validation. | Order 4. |
| 6 | Update role playbooks only with role-specific handoff duties. | AEO; TW consistency review | Makes execution responsibility explicit without duplicating workflow. | Order 4. |
| 7 | Update Candidate Improvement Backlog decisions and link PI-004/PI-005. | AEO | Closes retrospective decision traceability. | Order 2. |
| 8 | Produce a Sprint-5 workspace from templates and validate the STATUS.md plus handoff sections in existing artifacts before kickoff. | AEO + TW | Proves the structure is usable before it becomes mandatory. | Orders 3–6. |
| 9 | Engineering Manager Organizational Validation. | EM | Authorizes application of Organizational Freeze. | Orders 1–8. |
| 10 | Apply Organizational Freeze, publish Sprint Initiation Request, execute PMO Sprint Bootstrap, create initial `STATUS.md`, then authorize Product Owner entry. | EM; Sponsor / Program Direction; PMO | Establishes the approved opening order without transferring artifact ownership. | Order 9. |

## 13. Acceptance evidence for this plan

Implementation is ready for Engineering Manager validation only when every item in the sequential checklist is published, the Sprint 5 workspace has a valid STATUS.md, each applicable existing artifact/PR contains its handoff section, no shared institutional rule is duplicated in playbooks, and the audit checklist can verify the new protocol using published evidence.

## 14. Handoff

**AI Engineering Orchestrator → Engineering Manager:** review and approve this Organizational Implementation Plan. Approval authorizes controlled implementation of PI-004 and PI-005; it does not start Sprint 5 or make the planned protocol effective by itself.

## Institutional Handoff — AI Engineering Orchestrator → Engineering Manager

### Executive summary

The approved Organizational Implementation Plan, including the approved Sprint Initiation Request, is implemented and published. PI-004 and PI-005, the Sprint Execution Protocol, Organizational Freeze, templates, Sprint 5 workspace, audit controls and role-specific responsibilities are ready for Organizational Validation.

### Objective completed

Implemented the approved organizational decisions and Sprint Initiation Request before Sprint 5 kickoff without starting Sprint 5 or changing product scope.

### Published artifacts

- [Engineering Workflow](ENGINEERING_WORKFLOW.md), Engineering Audit Checklist, PI/Candidate backlogs and institutional history;
- role playbooks and PMO role index;
- Sprint templates under `sprints/templates/`, including the Sprint Initiation Request template; and
- Sprint 5 workspace and blocked [STATUS.md](sprints/sprint-005/STATUS.md).

### Versioned reference

- Branch: `main`
- Commit: `42e929a`
- Full hash: `42e929a4c3743b403326785d85b50ca942038985`

### Evidence and constraints

The implementation follows the [Sprint 4 Engineering Retrospective](SPRINT_4_ENGINEERING_RETROSPECTIVE.md), this approved plan and the approved Sprint Initiation Request decision. The Sprint 5 workspace remains blocked pending Engineering Manager Organizational Validation; no Sprint Bootstrap, Story materialization, Product refinement or Sprint execution is authorized.

### Pending items

Engineering Manager Organizational Validation and application of Organizational Freeze, followed by the Sprint Initiation Request, PMO Sprint Bootstrap, initial `STATUS.md` and Product Owner entry in that order.

### Next authorized action

- Next role: Engineering Manager
- Required action: perform Organizational Validation and, if approved, apply Organizational Freeze before any Sprint Initiation Request is published.
- Acceptance / stop criteria: do not authorize Sprint 5 initiation unless Validation and Freeze are complete; do not authorize PMO Bootstrap before the published Request.
- Operational command: review the published `main` commit `42e929a4c3743b403326785d85b50ca942038985` and the blocked Sprint 5 STATUS.
