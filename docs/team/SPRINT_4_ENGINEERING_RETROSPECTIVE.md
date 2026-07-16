# Sprint 4 — Engineering Retrospective

**Decision authority:** Engineering Manager

**Date:** 2026-07-16

**Status:** ORGANIZATIONAL DECISIONS APPROVED — implementation authorized before Sprint 5 kickoff

## Scope and inputs

This retrospective evaluates the Sprint 4 engineering process only. It does not
reopen Story #44, change its technical decisions, or implement process changes.

Inputs considered:

- [Sprint 4 Engineering Audit Report](SPRINT_4_ENGINEERING_AUDIT_REPORT.md)
  and [Audit Acceptance](SPRINT_4_ENGINEERING_AUDIT_ACCEPTANCE.md);
- [Candidate Improvement Backlog](CANDIDATE_IMPROVEMENT_BACKLOG.md), including
  the CTI candidates CI-001 and CI-002;
- Sprint 4 Quality Evidence and the non-blocking Order Testcontainers
  observation;
- the Story #44 Documentation Gate;
- Repository Owner evidence reported by the audit for PR, branch, issue, board,
  milestone, release and `main`; and
- AEO evidence on versioned-handoff chronology and closure readiness.

## Lessons learned and audit decisions

| Audit finding | Root cause | Impact | Retrospective decision |
| --- | --- | --- | --- |
| PMO materialization occurred after Product, Functional Review and Architecture Gate work. | The Sprint had no mandatory, living entry-state check that stopped work until the official Story and its first handoff were materialized. | The published handoff chronology became non-conformant and cannot be repaired retroactively. | Corrective action approved through the Sprint Execution Protocol. Every role must read the official Sprint state and its required inbound handoff before acting. |
| Release/milestone evidence was absent before the audit. | Closure readiness was not represented as an explicit, shared operational state with a required Repository Owner handoff. | The Sprint Closing Workflow precondition was not evidenced; Sprint closure remained blocked. | Corrective action approved through the Sprint Execution Protocol, with a visible closure-readiness state and mandatory Repository Owner package. |
| README and Roadmap were stale after Final Engineering Review. | Documentation synchronization depended on implicit coordination rather than an explicit downstream handoff and state transition. | Executive documentation did not fully represent the published Sprint state. | Corrective action approved through standard handoff content, templates and a documented documentation-to-closure transition. |

The Quality Engineer observation about three skipped Order Testcontainers
integrations remains non-blocking technical debt. It is not a process-improvement
decision and does not alter this retrospective.

## Candidate Improvement decisions

| Candidate | Decision | Rationale | Authorized follow-up |
| --- | --- | --- | --- |
| CI-001 — Product Owner materialization of new Stories | Converted to Process Improvement | The audit confirmed that authoritative materialization must precede all Story work; this is now a demonstrated institutional gap, not merely an ambiguity. | AEO creates PI-004 for the Sprint-entry and materialization controls below. |
| CI-002 — Institutional Handoff Packages | Converted to Process Improvement | The audit confirmed that published artifacts alone did not make the next action, constraints and closure dependencies sufficiently explicit. | AEO creates PI-005 for the Sprint Execution Protocol below. |

No candidate is rejected or retained as pending. The AEO must update the
Candidate Improvement Backlog and create the authorized Process Improvement
entries as part of implementation; this retrospective does not itself alter
those backlogs.

## Organizational Decisions effective after implementation

### 1. Sprint entry and official materialization

Before any Product refinement, Architecture Gate or implementation work begins,
the official Sprint state must identify the approved Sprint goal, the official
Story Issue, the current responsible role and the valid inbound handoff. A role
must stop and escalate when that state or handoff is absent, unpublished or
inconsistent.

The Product Owner owns product materialization in the official backlog once the
Sprint direction is approved. This does not change portfolio authority: PMO or
Program direction still supplies the approved direction before materialization.

### 2. Sprint Execution Protocol

The AEO is authorized to implement a single Sprint Execution Protocol in the
Engineering Workflow and supporting role playbooks. It must include all of the
following without creating a parallel backlog, defect tracker or approval gate:

- a living `STATUS.md` in the Sprint documentation package, identifying Sprint
  goal, current stage, Story/Issue, owner, official branch, latest published
  handoff, blockers and the next authorized action;
- an Agent Startup Protocol requiring every participating role to read the
  current Sprint `STATUS.md`, the Engineering Workflow, its role playbook and
  the applicable inbound handoff before work begins;
- Institutional Handoff Packages expressed as a standardized section of the
  existing handoff artifact or PR, not as a new parallel document. Each package
  must state source and destination roles, branch, commit hash, published
  artifacts, scope, constraints, evidence, known risks, required next action and
  acceptance/stop criteria;
- mandatory publication of that package with the producing role's Versioned
  Handoff before the receiving role acts;
- templates for Sprint state, handoff-package sections, Architecture Gate,
  Quality Gate, Documentation Gate, Final Engineering Review and closure
  readiness, reusing existing institutional identifiers;
- a Sprint documentation package with a predictable, documented structure for
  status, planning, gates, handoffs and closure evidence; and
- a closure-readiness transition that makes explicit the Repository Owner's
  required release, milestone, `main` and worktree evidence before the audit is
  started.

### 3. Organizational Freeze

After Sprint kickoff, the Workflow, role playbooks, mandatory templates and
Sprint Execution Protocol are frozen for that Sprint. An emergency exception may
be authorized only by the Engineering Manager, recorded in the living Sprint
state with rationale, scope and effective date. Candidate observations continue
to be recorded but cannot change active Sprint rules.

### 4. Validation and effective date

PI-004 and PI-005 must be implemented by the AEO on `main`, then submitted to
the Engineering Manager for organizational validation. The new protocol becomes
mandatory only after that validation and before the Sprint 5 kickoff. Its first
real-use validation is Sprint 5; the AEO will assess it in the subsequent audit
and retrospective without duplicating technical or product gates.

## Explicit non-decisions

- No new engineering role, product tracker, defect tracker or approval gate is
  approved.
- No change to Story #44, its release disposition or its technical gates is
  approved.
- No organizational change is effective merely because this retrospective is
  published; implementation and Engineering Manager organizational validation
  remain mandatory.

## Authorization and handoff

**Engineering Manager → AI Engineering Orchestrator:** implementation of
PI-004 and PI-005 is authorized within the decisions above. The AEO must publish
only the organizational implementation, evidence and the updated existing
backlogs; it must not begin Sprint 5 or alter active product scope.

After implementation, the AEO hands the result to the Engineering Manager for
organizational validation and application of the Organizational Freeze.
