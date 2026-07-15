# Process Improvement Backlog

**Status:** Institutional baseline  
**Owner:** AI Engineering Orchestrator  
**Decision authority:** Engineering Manager

## Purpose

This backlog records evidence-based improvements to the engineering system. It is independent of the product backlog: it does not prioritize product work, create product Stories or replace GitHub Issues used for work requiring product ownership.

Each entry uses `PI-<sequence>` and records the observed problem, root cause, decision, status, evidence and validation Sprint. Status values are `Proposed`, `Approved`, `Implemented`, `Validated` and `Rejected`. An implemented change remains open until it is validated in a subsequent Sprint.

## Entries

| ID | Problem observed | Root cause | Decision | Status | Evidence | Dependencies | Validation / next assessment |
| --- | --- | --- | --- | --- | --- | --- | --- |
| PI-001 | The final Sprint 2 state of the main README was found inconsistent only after administrative Sprint closure. | No institutional step audited the completeness and cross-consistency of closure artifacts before definitive closure. | Add the Sprint Closing Workflow, Engineering Audit Checklist and process retrospective; evolve role handoffs without duplicating operational reviews. | Implemented | [Sprint 2 Engineering Audit Report](SPRINT_2_ENGINEERING_AUDIT_REPORT.md) found incomplete workflow/authority coverage. | PI-002 implemented; Engineering Manager institutional review. | Real Sprint 3 audit, after Sprint Closing Workflow execution. |
| PI-002 | The first audit checklist does not explicitly verify workflow separation or GitHub/release operational authority. | Its Sprint section checks the existence of a closure decision, but not the workflow transition or the role/playbook that owns administrative operations. | Add the two focused checklist controls for workflow separation and operational authority; reference them from the Sprint Closing Workflow and AEO continuous-improvement cycle. | Implemented | [Sprint 2 Engineering Audit Report](SPRINT_2_ENGINEERING_AUDIT_REPORT.md); [Engineering Audit Checklist](ENGINEERING_AUDIT_CHECKLIST.md). | Engineering Manager institutional review; Technical Writer consistency verification; Repository Owner publication on `main`. | Real Sprint 3 audit, after Sprint Closing Workflow execution. |

## Operating rule

The AI Engineering Orchestrator proposes and maintains entries. The Engineering Manager approves decisions, decides blocking impact and confirms validation. The Technical Writer maintains links and document consistency. Other roles supply evidence within their authority. A rejected or superseded entry keeps its evidence and decision history.
