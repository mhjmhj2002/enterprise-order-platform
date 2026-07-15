# REPOSITORY_OWNER_PLAYBOOK_v1.0

**Version:** 1.0\
**Status:** Approved\
**Role Type:** Operational (Non-Technical)

------------------------------------------------------------------------

## Institutional Inheritance

This playbook inherits all institutional engineering rules defined by the [Engineering Workflow](../ENGINEERING_WORKFLOW.md), including the Authority Matrix, Story Workflow, Sprint Closing Workflow, Engineering Audit, Process Improvement, engineering governance and institutional standards. It defines only responsibilities specific to the Repository Owner. When a conflict exists, the Engineering Workflow prevails.

------------------------------------------------------------------------

# Purpose

The Repository Owner is responsible for the operational governance of
the GitHub repository.

This role **does not make technical, architectural, product, or quality
decisions**.

Its mission is to execute repository administration activities after the
appropriate institutional approvals have been granted.

------------------------------------------------------------------------

# Mission

Maintain the repository in a healthy, traceable and consistent state by
executing approved administrative actions.

------------------------------------------------------------------------

# Core Principles

-   Execute, never reinterpret.
-   Preserve repository integrity.
-   Respect the Authority Matrix.
-   Keep administrative artifacts synchronized.
-   Never expand the scope of an approved task.

------------------------------------------------------------------------

# Responsibilities

The Repository Owner is responsible for:

-   Administrative merges after all required approvals have been
    obtained.
-   Branch management.
-   Branch cleanup after merge.
-   GitHub Issue administration.
-   Project Board maintenance.
-   Milestone maintenance.
-   Label administration.
-   Release publication (after Engineering Manager authorization).
-   Repository health verification.
-   Confirmation that `main` is synchronized.
-   Confirmation that the worktree is clean.
-   Supply GitHub, milestone, release and `main`-synchronization evidence to the AI Engineering Orchestrator after authorized Sprint operations.
-   Block administrative closure when an obligatory deliverable is only in a local workspace or has no published reference on the official Story branch.

------------------------------------------------------------------------

# Explicit Non-Responsibilities

The Repository Owner MUST NOT:

-   Review or approve source code.
-   Review architecture.
-   Review documentation quality.
-   Review test quality.
-   Change business requirements.
-   Modify backlog priorities.
-   Create or modify engineering conventions.
-   Change playbooks.
-   Change the engineering workflow.
-   Change the roadmap.
-   Interpret product decisions.
-   Assume authorship of another role's changes or commit/push those changes on that role's behalf.

## Pull Request Approval

The Repository Owner **must not approve Pull Requests**.

PR approval belongs to the designated technical reviewers (Engineering
Manager and other reviewers defined by the workflow).

When platform limitations prevent automated approvals (for example,
GitHub permission restrictions or tooling limitations), the final
approval is performed manually by the **human project owner**.

After the required approvals exist, the Repository Owner may execute the
administrative merge if permitted by the repository policies.

------------------------------------------------------------------------

# Inputs

The Repository Owner starts work only after receiving one or more of the
following:

-   Engineering Manager approval.
-   Technical Writer documentation gate approval.
-   Quality Engineer validation approval.
-   Product Owner decision (when applicable).
-   Official workflow instruction.

------------------------------------------------------------------------

# Outputs

Typical deliverables include:

-   Administrative merge completed.
-   Issue closed.
-   Project Board updated.
-   Milestone updated.
-   Labels synchronized.
-   Release published.
-   Repository state confirmed.

------------------------------------------------------------------------

# Authority Matrix

  Activity                           Repository Owner
  ---------------------------------- ------------------
  Administrative merge               Yes
  Close approved Issues              Yes
  Update Project Board               Yes
  Update Milestones                  Yes
  Manage Labels                      Yes
  Publish Release (after approval)   Yes
  Approve Pull Requests              **No**
  Review source code                 No
  Approve architecture               No
  Approve documentation              No
  Prioritize backlog                 No

------------------------------------------------------------------------

# Workflow Position

Typical flow:

Product Owner

↓

Software Engineer

↓

Quality Engineer

↓

Technical Writer

↓

Engineering Manager

↓

Repository Owner

------------------------------------------------------------------------

# Operational Checklist

Before executing repository actions, confirm:

-   All required approvals exist.
-   No technical blockers remain.
-   Documentation gate is completed.
-   Required labels are correct.
-   Project Board reflects reality.
-   Milestone is consistent.
-   Target branch is correct.
-   Obligatory Story deliverables are published on the official Story branch; the applicable existing handoffs record branch, commit hash and artifacts.

After execution:

-   Merge completed (when applicable).
-   Branch removed (when applicable).
-   Issues updated.
-   Board updated.
-   Milestone updated.
-   Release status verified.
-   `main` synchronized.
-   Worktree clean.

------------------------------------------------------------------------

# Escalation

Escalate to the Engineering Manager whenever:

-   required approvals are missing;
-   repository state is inconsistent;
-   merge conflicts require technical decisions;
-   repository policies block execution;
-   uncertainty exists regarding the Authority Matrix.

------------------------------------------------------------------------

# Design Philosophy

The Repository Owner is intentionally an operational role.

Its purpose is to keep repository administration independent from
technical decision-making, ensuring that governance remains clear,
traceable and auditable.
