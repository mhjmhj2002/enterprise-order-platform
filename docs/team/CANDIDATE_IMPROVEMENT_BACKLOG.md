# Candidate Improvement Backlog

**Status:** Institutional baseline  
**Maintainer:** AI Engineering Orchestrator  
**Decision authority:** Engineering Manager during Sprint Retrospective

## Purpose

This backlog is the institutional queue for process-improvement ideas observed during a Sprint. A candidate is not a confirmed process problem, does not alter engineering rules, and does not replace the [Process Improvement Backlog](PROCESS_IMPROVEMENT_BACKLOG.md).

Any originating role — CTI, PO, EM, SE, QE, TW, RO, AEO or TM — may register a candidate with its origin and evidence. The AI Engineering Orchestrator maintains the queue and ensures that it is reviewed in the Sprint Retrospective. A suggestion does not change a workflow, playbook, role responsibility or institutional standard before that decision.

## Status and retrospective decisions

| Status | Meaning |
| --- | --- |
| `Pending Evaluation` | Awaiting an explicit Sprint Retrospective decision. |
| `Approved` | Retrospective accepted the observation as useful, but no process change is currently required; retain it for the next review. |
| `Rejected` | Retrospective determined that no institutional action is justified. |
| `Converted to PI` | Retrospective approved a process-improvement proposal; link the resulting `PI-<sequence>` entry. |

During each Sprint Retrospective, the Engineering Manager decides each pending candidate explicitly: reject it, keep it pending, or convert it to a Process Improvement. The decision and its date are recorded in the existing entry. A real, material process failure that requires immediate action is escalated to the Engineering Manager through the existing governance flow; it is not delayed by this queue.

## Candidates

| ID | Origin | Summary | Justification | Expected benefits | Registered | Status | Retrospective decision / PI reference |
| --- | --- | --- | --- | --- | --- | --- | --- |
| CI-001 | CTI | Product Owner materialization of new Stories in GitHub. | At the start of Sprint 4, an operational ambiguity was observed about who materializes new Stories in GitHub. It is a suggestion for clarification, not a process failure. | Clear materialization ownership and fewer external direction requests. | 2026-07-16 | Converted to PI | Sprint 4 Retrospective: converted to [PI-004](PROCESS_IMPROVEMENT_BACKLOG.md). |
| CI-002 | CTI | Evaluate Institutional Handoff Packages between consecutive Story-flow roles. | Roles currently report completion, while the next role still depends on external direction. Assess whether a handoff artifact containing scope, limits, evidence, risks and validation guidance would improve the institutional transition without replacing existing technical documentation. | Less external coordination; standardized handoffs; clearer communication and Story traceability; stronger future audit evidence. | 2026-07-16 | Converted to PI | Sprint 4 Retrospective: converted to [PI-005](PROCESS_IMPROVEMENT_BACKLOG.md). |
| CI-003 | AEO | Verify external backlog evidence after the final versioned planning change. | During Story #46, the GitHub Issue body was synchronized before the Product Plan received its final correction. The plan then stated that synchronization was complete while the external source still contained the previous handoff. The Engineering Manager detected the mismatch in two Functional Review checks. Assess whether the existing Versioned Handoff and PI-005 validation need a small explicit read-back control after the final plan change, without creating a new gate or changing active-Sprint rules. | Fewer avoidable review returns; consistent external backlog evidence; stronger validation of PI-005 in real Sprint use. | 2026-07-17 | Pending Evaluation | Awaiting Sprint 5 Retrospective decision; the active correction remains owned by the Product Owner and Engineering Manager. AEO record: `main` / `897a61459ba3d7b2ee942c50539f84a1bdbc76de`; artifact: this backlog. |

## Related institutional artifacts

### CI-003

- [Sprint 5 Product Plan](sprints/SPRINT_5_PRODUCT_PLAN.md): source plan whose final correction was not initially reflected in the external backlog evidence.
- [Sprint 5 Engineering Manager Review](sprints/SPRINT_5_ENGINEERING_MANAGER_REVIEW.md): records the two Functional Review checks that detected and then verified resolution of the mismatch.
- [Engineering Workflow — Versioned Handoff](ENGINEERING_WORKFLOW.md#versioned-handoff) and [PI-005](PROCESS_IMPROVEMENT_BACKLOG.md): institutional controls to be evaluated; this reference does not amend either control.
- GitHub Issue [#46](https://github.com/mhjmhj2002/enterprise-order-platform/issues/46): external backlog evidence reviewed against the published plan.

## Institutional Handoff — AI Engineering Orchestrator → Engineering Manager

### Executive summary

The AEO reviewed the organizational observation raised during Sprint 5 and registered `CI-003` for retrospective evaluation. The registration preserves the evidence of the backlog-traceability mismatch without changing the active Story flow.

### Objective completed

The Candidate Improvement Backlog records the identifier, origin, rationale, related institutional artifacts and expected benefit for the Sprint 5 observation.

### Published artifacts

- `docs/team/CANDIDATE_IMPROVEMENT_BACKLOG.md`

### Versioned reference

- Branch: `main`
- Evidence commit: `897a614`
- Full evidence hash: `897a61459ba3d7b2ee942c50539f84a1bdbc76de`

### Evidence and constraints

- `CI-003` remains `Pending Evaluation` until an explicit Sprint 5 Retrospective decision by the Engineering Manager.
- The candidate is evidence for future evaluation only. It does not modify the Engineering Workflow, playbooks, responsibilities, governance rules or Sprint execution.
- The Story #46 correction and Functional Review approval remain owned by the Product Owner and Engineering Manager, respectively.

### Pending items

- Engineering Manager: decide `CI-003` during the Sprint 5 Retrospective—reject, keep pending or convert it to a Process Improvement.

### Next authorized action

- Next role: Engineering Manager
- Required action: review `CI-003` during the Sprint 5 Retrospective with the listed evidence.
- Acceptance / stop criteria: record an explicit retrospective decision; do not promote or implement a process change during the active Sprint.
- Operational command: retain `CI-003` as `Pending Evaluation` pending the Sprint 5 Retrospective.

## Operating boundary

Candidate identifiers (`CI-<sequence>`) exist only in this idea queue and are not product backlog, defect or PI identifiers. A candidate becomes official engineering work only when the Sprint Retrospective converts it to a `PI-<sequence>` entry in the Process Improvement Backlog. The candidate retains the link to preserve evidence and decision history.
