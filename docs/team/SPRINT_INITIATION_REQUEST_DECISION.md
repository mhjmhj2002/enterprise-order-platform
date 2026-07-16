# Organizational Decision — Sprint Initiation Request

**Parecer do Engineering Manager:** APPROVED WITH COMMENTS

**Data:** 2026-07-16

## Decision

The Sprint Initiation Request is approved as the mandatory external input that
precedes the Sprint Bootstrap. It resolves the institutional gap between an
external instruction to start a Sprint and the PMO's first operational action.

The artifact establishes the first handoff of the Sprint lifecycle:

```text
Sprint sponsor / approved program direction
        ↓ Sprint Initiation Request
PMO
        ↓ Sprint Bootstrap and official materialization
Sprint Execution Protocol
```

This decision creates no new role, product tracker, technical gate or approval
sequence. The sponsor is the source of external direction, not a new engineering
role. The PMO remains responsible for executing the Bootstrap, and Product Owner
authority over functional scope remains unchanged.

## Minimum mandatory content

The request must use the Sprint identifier as its traceable reference and record:

| Field | Required meaning |
| --- | --- |
| Target Sprint | The Sprint identifier and intended start context. |
| Sponsor and date | The accountable source of the approved external direction and when it was issued. |
| Macro objective | The business or organizational outcome that justifies initiating the Sprint. |
| Initial Story direction | The initial Story/Issue reference when it already exists, or the explicit direction that the PMO must materialize before any Product refinement. |
| Base branch | The approved branch from which the Sprint's official Story branch will be created. |
| Initial boundaries | Known scope limits, non-goals, dependencies, risks or constraints needed to avoid ambiguity during Bootstrap. |
| Supporting references | Applicable roadmap, prior Sprint evidence, decisions or external direction references. |
| Authorized next action | Explicit instruction for the PMO to execute the Sprint Bootstrap. |

## Institutional comments

1. A Sprint Initiation Request authorizes only Sprint Bootstrap. It does not
   authorize Product refinement, Architecture Gate, implementation, release or
   any change to product scope.
2. If no official Story Issue exists, the PMO must materialize the approved
   direction before transferring the Sprint to Product Owner. A role must stop if
   the request lacks the minimum content or conflicts with current institutional
   decisions.
3. The request becomes the first published reference in the future Sprint
   `STATUS.md` and the first inbound handoff read by the PMO under the Agent
   Startup Protocol.
4. Its template must reuse existing Sprint and GitHub Issue identifiers; no new
   local identifier format is approved.

## Authorization and implementation boundary

The AI Engineering Orchestrator is authorized to incorporate this decision into
the Organizational Implementation Plan and the organizational implementation
already approved for Sprint 5 preparation. The implementation must add the
artifact definition, template, workflow handoff and PMO startup obligation before
the Organizational Freeze of Sprint 5.

This decision itself does not modify the Engineering Workflow, playbooks,
templates or active Sprint rules. Those changes require the existing AEO
implementation handoff and Engineering Manager organizational validation.
