# Engineering Audit Checklist

**Version:** 1.1
**Status:** Institutional baseline  
**Owner:** AI Engineering Orchestrator  
**Closure authority:** Engineering Manager

## Purpose

This is a lightweight evidence-based audit performed once per Sprint, after authorized release and GitHub operations and before the definitive Sprint closure. It verifies that the engineering process produced the expected institutional artifacts; it does not repeat technical, quality, documentation or repository reviews.

For each applicable line, link the evidence and record one result: `Verified`, `Not applicable` (with rationale), or `Exception` (with owner and Engineering Manager decision). An exception blocks closure only when the Engineering Manager classifies it as blocking.

## Audit record

| Field | Record |
| --- | --- |
| Sprint / date | |
| Auditor (AEO) | |
| Engineering Manager decision | |
| Release / milestone | |
| Overall result | |

## Mandatory verification areas

| Area | Verify | Evidence / result |
| --- | --- | --- |
| Product | Main README reflects the delivered baseline and links work. | |
| Product | Affected service or component READMEs reflect the delivered baseline. | |
| Product | Event Catalog reflects applicable event contracts; otherwise record N/A. | |
| Architecture | Architecture Overview reflects the delivered baseline. | |
| Architecture | Required ADRs are present and cross-referenced; otherwise record N/A. | |
| Quality | Applicable `TEST_PLAN` documents exist and match the validated scope. | |
| Quality | Applicable `TEST_REPORT` documents and evidence support the release recommendation. | |
| Governance | `CHANGELOG` records the relevant change. | |
| Governance | `PROJECT_HISTORY` records the Sprint outcome. | |
| Governance | Engineering Roadmap was reviewed and updated when the outcome affects its direction. | |
| Governance | **Workflow separation:** Story Workflow, Release Workflow and Sprint Closing Workflow are independent, each has explicit entry criteria, exit criteria and closure authority. | |
| Governance | **Operational authority:** every required closing operation has an explicit owner, corresponding playbook, defined evidence handoff and authority. This includes release, GitHub, milestones, Project Board, branch cleanup and repository administration when applicable. | |
| GitHub | Issues have the approved final disposition and traceability. | |
| GitHub | Labels and Project Board reflect that disposition. | |
| GitHub | Milestone and release/tag reflect the authorized Sprint state. | |
| Sprint | Sprint Review evidence or decision is available. | |
| Sprint | Engineering process retrospective is recorded below or linked. | |
| Sprint | Sprint closure decision is recorded by the Engineering Manager. | |

## Process retrospective

Answer concisely and link evidence:

1. What escaped the engineering process?
2. At which handoff or gate should it have been detected?
3. Was the expected responsibility explicit?
4. Is a process change proportionate and worth adopting?
5. Which existing institutional document should evolve, or why is a new artifact necessary?

If an improvement is accepted, create or update its entry in the [Process Improvement Backlog](PROCESS_IMPROVEMENT_BACKLOG.md). This checklist is retained as Sprint evidence; it is not a parallel tracker.
