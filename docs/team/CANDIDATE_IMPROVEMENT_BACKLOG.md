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
| `Pending Retro` | Awaiting an explicit Sprint Retrospective decision. |
| `Approved` | Retrospective accepted the observation as useful, but no process change is currently required; retain it for the next review. |
| `Rejected` | Retrospective determined that no institutional action is justified. |
| `Converted to PI` | Retrospective approved a process-improvement proposal; link the resulting `PI-<sequence>` entry. |

During each Sprint Retrospective, the Engineering Manager decides each pending candidate explicitly: reject it, keep it pending, or convert it to a Process Improvement. The decision and its date are recorded in the existing entry. A real, material process failure that requires immediate action is escalated to the Engineering Manager through the existing governance flow; it is not delayed by this queue.

## Candidates

| ID | Origin | Summary | Justification | Registered | Status | Retrospective decision / PI reference |
| --- | --- | --- | --- | --- | --- | --- |
| CI-001 | CTI | Product Owner materialization of new Stories in GitHub. | At the start of Sprint 4, an operational ambiguity was observed about who materializes new Stories in GitHub. It is a suggestion for clarification, not a process failure. | 2026-07-16 | Pending Retro | Review in Sprint 4 Retrospective. |

## Operating boundary

Candidate identifiers (`CI-<sequence>`) exist only in this idea queue and are not product backlog, defect or PI identifiers. A candidate becomes official engineering work only when the Sprint Retrospective converts it to a `PI-<sequence>` entry in the Process Improvement Backlog. The candidate retains the link to preserve evidence and decision history.
