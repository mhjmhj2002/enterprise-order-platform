# Engineering Workflow

**Status:** Institutional Baseline
**Owner:** Engineering Manager
**Maintainer:** Technical Writer
**Purpose:** Single source of truth for engineering governance, handoffs and delivery flow.

## Objective

This document defines how engineering work moves from strategic direction to sprint closure in the Enterprise Order Platform. Role playbooks are specialized operational guides; when a playbook conflicts with this workflow, this document prevails.

## Institutional Document Hierarchy

The engineering documentation follows a hierarchy to keep shared rules in one authoritative place and role guidance focused.

1. **[Engineering Workflow](ENGINEERING_WORKFLOW.md)** is the root institutional document. It defines shared engineering rules, governance, Authority Matrix, Story Workflow, Release Workflow, Sprint Closing Workflow, Sprint Execution Protocol, Engineering Audit, Process Improvement and institutional standards.
2. **[Role Playbooks](roles/README.md)** automatically inherit every rule from this document. They define only role-specific responsibilities, operating detail and role-owned deliverables; they must link to, rather than duplicate, shared institutional rules.
3. **Product Documentation** records approved product scope, business rules and acceptance context. It does not redefine engineering governance.
4. **Project Documentation** records architecture, quality, releases, history and other evidence. It implements or evidences the institutional rules but does not override them.

When documents conflict, the highest applicable level prevails. Future institutional changes must be made in this root document first; a role playbook changes only when its own specific responsibilities change.

## Engineering Philosophy

- Deliver increments that are valuable, testable and traceable.
- Keep business scope, architectural decisions, implementation and validation accountable to distinct roles.
- Decide architecture before implementation when a Story contains unresolved technical choices.
- Treat documentation, quality evidence and release records as deliverables.
- Prefer explicit gates and recorded trade-offs over assumptions.

## Organizational Structure

| Role | Primary accountability |
| --- | --- |
| Program Director | Strategic direction and portfolio-level sponsorship. |
| Program Management Office | Materializes approved program direction as traceable GitHub backlog administration before Product refinement. |
| AI Engineering Orchestrator | Improves engineering processes and governance; coordinates the institutional Sprint audit and escalates authority gaps. |
| Product Owner | Defines why and what: value, scope, priorities and functional acceptance criteria. |
| Engineering Manager | Owns technical governance, capacity, architecture approval, release approval and sprint closure. |
| Repository Owner | Administers the GitHub repository and executes repository operations after the defined technical gates. |
| Technical Writer | Maintains institutional documentation, cross-references and process consistency. |
| Technical Lead | Refines technical direction, identifies delivery risks and reviews technical readiness. |
| Software Engineer | Implements approved technical work and produces code-level evidence. |
| Quality Engineer | Plans and executes functional validation, test evidence and regression analysis. |

## Official Engineering Flow

```text
Program Director
        ↓
Sprint Initiation Request — Sponsor / Program Direction → PMO
        ↓
Program Management Office — Sprint Bootstrap and Story Materialization
        ↓
Product Owner
        ↓
Engineering Manager Review
        ↓
Product Owner — Backlog Materialization
        ↓
Technical Writer — Documentation Baseline
        ↓
Architecture Gate (when applicable)
        ↓
Engineering Manager Approval
        ↓
Software Engineer
        ↓
Quality Engineer — Planning
        ↓
Engineering Manager — QA Authorization
        ↓
Quality Engineer — Execution
        ↓
Technical Writer — Release Documentation
        ↓
Engineering Manager — Technical Approval
        ↓
Repository Owner — Administrative Merge
        ↓
Engineering Manager — Release Authorization
        ↓
Repository Owner — Release and GitHub Synchronization
        ↓
AI Engineering Orchestrator — Engineering Audit
        ↓
Engineering Manager — Sprint Closure
```

No implementation begins before the applicable approval gate. The Software Engineer must stop and escalate when a Story does not meet the Definition of Ready.

## Sprint Lifecycle

1. **Sprint Initiation and Materialization:** Sponsor / Program Direction publishes a [Sprint Initiation Request](sprints/templates/SPRINT_INITIATION_REQUEST_TEMPLATE.md) as the first Versioned Handoff of the Sprint to the Program Management Office. It records the authorized Sprint, macro objective, initial Story or materialization direction, base branch, boundaries, institutional references and explicit authorization to begin. The PMO reads and validates this request before running Sprint Bootstrap; only then may it materialize the official Story, labels, milestone and Project Board state before Product refinement. The Product Owner owns the resulting product backlog content.
2. **Product and Engineering Review:** Product Owner planning defines value, scope, priorities, risks and acceptance criteria; Engineering Manager validates readiness, capacity and unresolved decisions.
3. **Documentation Baseline:** Technical Writer synchronizes planning, architecture references, ADRs, catalogs and governance documents.
4. **Architecture Gate:** Required when infrastructure, cross-service contracts, technology choices or other unresolved technical decisions affect implementation.
5. **Implementation and Quality:** Software Engineer implements approved work; Quality Engineer plans, is authorized and executes validation.
6. **Release Readiness:** Technical Writer prepares release documentation; Quality Engineer provides the final recommendation; Engineering Manager authorizes release readiness; Repository Owner performs the approved repository operations.
7. **Engineering Audit and Sprint Closure:** after repository operations are complete, the AI Engineering Orchestrator performs the lightweight institutional audit using the [Engineering Audit Checklist](ENGINEERING_AUDIT_CHECKLIST.md). The Engineering Manager decides closure only after the audit result and the process retrospective are recorded.

## Sprint Execution Protocol

Every Sprint uses a workspace at `docs/team/sprints/sprint-<number>/` containing a living `STATUS.md`, `REPORTS/`, `AUDIT/` and `RETROSPECTIVE/`. `STATUS.md` is the authoritative operational state: it identifies the Sprint, current Story, current/previous/next step, responsible role, visual flow state, branch, Pull Request, gate, latest published handoff, blockers and last update.

When explicitly invoked to act, every role must:

1. read the current Sprint `STATUS.md`, this workflow, its role playbook and the applicable inbound handoff section;
2. confirm that `Role responsible` and `Current step` authorize it; otherwise identify the authorized role and stop without changing artifacts;
3. stop and return to the producing role if the inbound handoff is absent, unpublished or inconsistent;
4. execute only its authorized activity;
5. update `STATUS.md` with the completed state, evidence and next authorized action;
6. add an **Institutional Handoff** Markdown section to its own existing institutional artifact or PR, including source/destination roles, branch, commit, artifacts, scope, constraints, evidence, risks, pending items, next action and acceptance/stop criteria;
7. publish its own artifact through the Versioned Handoff; and
8. end after identifying the next role and published artifact.

An Institutional Handoff is never a standalone document or parallel tracker. A receiving role never publishes or amends another role's unpublished artifact. The Repository Owner remains exclusively responsible for the authorized GitHub administrative operations and publishes only its own administrative evidence.

### PMO Sprint Bootstrap

Before Sprint Bootstrap, the PMO must: (1) read the published Sprint Initiation Request; (2) validate its explicit authorization to start the Sprint; (3) execute only the authorized Sprint Bootstrap; (4) create the initial `STATUS.md` from the institutional template; and (5) publish its administrative artifacts and PMO → Product Owner Institutional Handoff. Without a published, authorized Sprint Initiation Request, the PMO must stop and request Sponsor / Program Direction completion; it cannot initialize the Sprint from informal direction.

## Official Handoffs

### Versioned Handoff

A **Versioned Handoff** is the mandatory form of every handoff that produces versioned artifacts, including code, tests, documentation, plans, reports and governance records. Local workspace changes do not characterize a delivery and must not be used as the sole evidence for a handoff, review, approval or administrative closure.

Before such a handoff, the producing role must synchronize the official Story branch as appropriate, inspect the intended diff, create a cohesive commit, push it to the remote Story branch and record the published reference in its existing handoff record. The official Story branch is the shared state of the implementation; GitHub must continuously reflect the work available to the receiving role.

The existing handoff record (for example, a PR, Story plan, test report, engineering review or audit evidence) records, when applicable: the branch, published commit hash and published artifacts. No additional handoff document or gate is created by this rule. A receiving role works only from published artifacts and escalates a missing or divergent publication to the producing role.

Roles publish their own artifacts. No role may take authorship of, commit, or publish another role's uncommitted deliverable in order to complete a handoff.

| From | To | Required handoff |
| --- | --- | --- |
| Sponsor / Program Direction | Program Management Office | Published Sprint Initiation Request, using the institutional template, with explicit authorization for Sprint Bootstrap. |
| Program Management Office | Product Owner | Official GitHub Story materialization and PMO → PO handoff section before Product refinement. |
| Product Owner | Engineering Manager | Sprint Plan with scope, priorities, risks and functional criteria, including PO → EM handoff section. |
| Engineering Manager | Product Owner | Approved or refined planning, including constraints and capacity decisions. |
| Product Owner | Technical Writer | Materialized backlog and approved planning references. |
| Technical Writer | Architecture Gate | Documentation baseline, cross-references and identified decision gaps. |
| Architecture Gate | Engineering Manager | Technical Contract and ADRs when decisions affect architecture or shared infrastructure. |
| Engineering Manager | Software Engineer | Approved, ready technical Story with dependencies and contract references. |
| Software Engineer | Quality Engineer | Implementation handoff, testable environment and technical evidence. |
| Quality Engineer | Engineering Manager | Test Plan, Test Report, defects and release recommendation. |
| Technical Writer | Engineering Manager | Release documentation, changelog and history ready for publication. |
| Engineering Manager | Repository Owner | Technical approval of the PR and confirmation that all institutional gates are complete. |
| Repository Owner | AI Engineering Orchestrator | Evidence that the authorized GitHub operations, release and `main` synchronization are complete. |
| AI Engineering Orchestrator | Engineering Manager | Completed audit checklist, exceptions, process retrospective and any process-improvement proposal. |

## Authority Matrix

| Decision or action | PO | EM | Repository Owner | TW | LT | SE | QE |
| --- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Sprint goal, value and business scope | Owns | Reviews | Informed | Supports | Consults | Consults | Consults |
| Backlog priorities and functional criteria | Owns | Approves | Informed | Supports | Consults | Consults | Consults |
| Architecture, stack and Technical Contract | Consults | Approves | Informed | Documents | Refines | Implements | Consults |
| ADR | Consults | Approves | Informed | Maintains | Proposes | Contributes | Consults |
| Code and service configuration | Consults | Governs | Informed | Documents | Reviews | Owns | Validates |
| Test strategy and evidence | Consults | Authorizes | Informed | Documents | Reviews | Contributes | Owns |
| Technical approval of a PR | Informed | Owns | Informed | Validates documentation | Verifies operational conformity | Never self-approves | Recommends |
| GitHub merge and branch administration | Informed | Authorizes technically | Owns | Informed | Verifies | Never self-merges | Informed |
| Release readiness | Informed | Owns | Executes authorized repository operations | Prepares | Reviews | Informed | Recommends |
| Engineering audit and process retrospective | Consults | Decides closure / approves improvement | Supplies GitHub evidence | Supplies documentation evidence | Consults | Consults | Supplies quality evidence |

## Repository Owner Responsibilities

The Repository Owner is responsible exclusively for GitHub repository administration. This role does not participate in engineering technical decisions and does not replace the Product Owner, Engineering Manager, Technical Writer, Technical Lead, Software Engineer or Quality Engineer.

The Repository Owner exclusively:

- performs Pull Request merges after the required institutional gates are complete;
- removes merged temporary branches;
- synchronizes the `main` branch;
- administers Branch Protection Rules, repository permissions and GitHub settings; and
- performs administrative merges when the conditions below are met.

The Repository Owner does not assume authorship of work produced by other roles and does not create commits on their behalf. It blocks administrative closure when an obligatory Story deliverable exists only in a local workspace or lacks a published reference on the official Story branch.

### Administrative Merge

When every technical approval required by this workflow is complete and the only remaining blocker is an operational limitation of GitHub — such as the prohibition on self-approval in a single-maintainer repository — the Repository Owner may perform an **Administrative Merge**.

An Administrative Merge does not replace technical approval, remove any workflow gate or change the engineering decision. It only completes the repository operation after the Engineering Manager has confirmed the technical authorization.

> The official engineering process is defined by the ENGINEERING_WORKFLOW. Native GitHub policies are operational support mechanisms; they do not replace or invalidate formal approvals made by engineering roles.

## Institutional Artifacts

| Artifact | Created by | Approved by | Maintained by |
| --- | --- | --- | --- |
| Sprint Initiation Request | Sponsor / Program Direction | Sponsor / Program Direction | Sponsor / Program Direction until PMO acknowledges it through Sprint Bootstrap |
| Sprint Plan | Product Owner | Engineering Manager | Product Owner |
| Technical Contract | Engineering Manager / Technical Lead | Engineering Manager | Technical Writer |
| Event Catalog | Product Owner with Technical Writer | Engineering Manager | Technical Writer |
| ADR | Technical Writer with Engineering input | Engineering Manager | Technical Writer |
| Context Map and C4 | Technical Writer with architecture input | Engineering Manager | Technical Writer |
| Architecture Overview | Technical Writer with architecture input | Engineering Manager | Technical Writer |
| Test Plan and Test Report | Quality Engineer | Engineering Manager for authorization/recommendation | Quality Engineer |
| Release Notes and Changelog | Technical Writer | Engineering Manager | Technical Writer |
| Project History | Technical Writer | Engineering Manager when closure-related | Technical Writer |
| Engineering Roadmap | Technical Writer | Engineering Manager for strategic changes | Technical Writer |
| Sprint workspace and STATUS.md | Current responsible role | Engineering Manager for Organizational Validation / emergency exception | AEO maintains structure; current role maintains state |

## Institutional Naming and Traceability

Use the smallest identifier that preserves an unambiguous link to its institutional source. A label in a report is not a parallel backlog or defect tracker.

| Artifact or reference | Official identifier and rule | Creation authority |
| --- | --- | --- |
| Story, defect or action pending | GitHub Issue `#<number>`; this is the system of record for work that requires prioritization, ownership or follow-up beyond the current validation. | Product Owner materializes approved backlog items; the Engineering Manager decides whether a reported finding requires formal tracking. |
| Test scenario | `<scope>-<story>-<sequence>` (for example, `INV-033-001`), defined in the Story Test Plan and repeated unchanged in its Test Report. | Quality Engineer. |
| Evidence | The applicable Story/Issue reference and test-scenario ID, plus enough context to reproduce it (such as commit or PR, environment, date and command). No separate evidence numbering is required unless an approved artifact already defines it. | Producing role; Quality Engineer for test evidence. |
| Institutional document | Its approved filename or existing document identifier (for example, `ADR-001`). A documentation finding that needs follow-up is linked to its GitHub Issue; otherwise it is described with the Story and scenario/evidence that exposed it. | Technical Writer maintains identifiers already established for documentation. |

Roles may create identifiers only in the artifact they own and only according to this table. They must not invent local prefixes, parallel defect numbers or naming patterns. A new identifier format is allowed only when an existing artifact cannot provide traceability; the Technical Writer records the proposal and the Engineering Manager approves it before use. Existing official references must be preserved consistently across plans, reports, documentation and GitHub.

## Architecture Gates and Technical Contracts

An Architecture Gate is mandatory before implementation when a Story leaves technical decisions unresolved, including shared infrastructure, event platforms, contracts between services, versioning, runtime topology, security boundaries or reliability behavior.

The gate produces a Technical Contract when applicable. A minimum contract records the approved decision, scope, dependencies, configuration boundaries, interfaces or event contract, operational constraints, acceptance evidence and explicit out-of-scope items. It does not authorize deviations beyond the approved architecture.

## Quality Gates

- Quality planning occurs before execution for every Story that needs functional validation.
- The Engineering Manager authorizes Quality Engineer execution when the implementation handoff is ready.
- Test plans, reports, defects, known limitations and evidence must be traceable.
- A release recommendation is not a merge or release authorization.

## Pull Request Workflow

1. Software Engineer creates a Story branch from synchronized `main`.
2. The PR includes implementation scope, tests, documentation updates and known limitations.
3. Quality Engineer completes the applicable validation and provides evidence.
4. Technical Writer validates the documentation impact and institutional traceability.
5. Engineering Manager approves the PR technically after the required reviews and gates; the Technical Lead verifies operational conformity when assigned.
6. Repository Owner executes the merge operation, including an Administrative Merge when applicable.
7. The Story moves to Done only after the defined governance and quality gates are met and the merge is complete.

All role handoffs within this workflow that contain versioned artifacts follow the Versioned Handoff rule. Publication is progressive throughout the Story; it is not deferred until the final Pull Request step.

## Release Workflow

1. Technical Writer validates release notes, changelog, history and cross-references.
2. Quality Engineer provides validation evidence and recommendation.
3. Engineering Manager confirms the technical merge state and authorizes the release.
4. Repository Owner performs the required GitHub repository operations; the release, tags, issue state, Project Board and milestone are synchronized.

## Sprint Closing Workflow

The **Story Workflow** delivers and accepts one Story: planning, applicable architecture gate, implementation, quality, documentation, technical approval, merge and Story state. Its completion does not close the Sprint.

The **Sprint Closing Workflow** verifies that the completed set of Stories produced a coherent institutional result. It starts only after every in-scope Story has reached its approved disposition (Done, formally re-planned or explicitly removed) and release operations authorized by the Engineering Manager have finished.

| Activity | Responsible | Authority / handoff |
| --- | --- | --- |
| Supply documentation evidence | Technical Writer | Maintains evidence and publishes approved closure references. |
| Supply quality evidence | Quality Engineer | Recommends; does not authorize closure. |
| Supply GitHub and release evidence | Repository Owner | Executes only authorized operations; does not decide closure. |
| Run audit and retrospective | AI Engineering Orchestrator | Records evidence, exceptions and proposals; does not approve other roles' work. |
| Decide exceptions, improvements and closure | Engineering Manager | Sole closure authority. |

1. **Closure readiness — Repository Owner:** publishes its closure-readiness handoff section containing authorized release, milestone, Issue, Board, branch, `main` and worktree evidence before audit begins.
2. **Evidence assembly — Technical Writer, Quality Engineer and Repository Owner:** provide the existing documentation, quality and GitHub/release evidence; no role repeats another role's review.
3. **Engineering audit — AI Engineering Orchestrator:** checks the expected artifacts and their cross-consistency through the [Engineering Audit Checklist](ENGINEERING_AUDIT_CHECKLIST.md), including separation of Story, Release and Sprint Closing Workflows and explicit operational authority for required closing operations. Each item is marked verified, not applicable with rationale, or exception with evidence.
4. **Process retrospective — AI Engineering Orchestrator:** records what escaped the process, the expected detection point, whether accountability was explicit, proportionality of a change and the institutional document that should evolve. It is a retrospective of engineering process, not product scope.
5. **Improvement decision — Engineering Manager:** accepts the audit result, decides whether an exception blocks closure, and approves any process change. Accepted improvements are recorded in the [Process Improvement Backlog](PROCESS_IMPROVEMENT_BACKLOG.md).
6. **Closure decision — Engineering Manager:** closes the Sprint only when there are no unaccepted blocking exceptions, mandatory audit items are verified or justified as not applicable, and all accepted improvements have an owner, status and validation Sprint.
7. **Publication — Technical Writer:** updates the appropriate history, roadmap and governance references after closure. The Repository Owner makes only the GitHub operations authorized by the Engineering Manager.

The AI Engineering Orchestrator coordinates and records the audit; it does not approve documentation, quality, architecture, releases or GitHub operations. An audit finding never silently changes product scope or the product backlog.

## Continuous Improvement and Organizational Evolution

The Engineering Manager reviews this workflow at Sprint boundaries or when process gaps are discovered. Ideas observed during a Sprint are recorded first in the [Candidate Improvement Backlog](CANDIDATE_IMPROVEMENT_BACKLOG.md); they do not alter the process until the Sprint Retrospective explicitly rejects, retains or converts them to a PI. The AI Engineering Orchestrator maintains the process-improvement cycle and the [Process Improvement Backlog](PROCESS_IMPROVEMENT_BACKLOG.md); its identifiers use `PI-<sequence>` solely within that backlog and are not product backlog identifiers. Each improvement records the observed problem, root cause, decision, status, evidence and validation Sprint. The Engineering Manager approves changes to institutional naming and traceability conventions and prevents parallel nomenclatures. The Technical Writer maintains this document, validates inherited-document consistency and cross-references, and updates a playbook only when its role-specific responsibilities are affected.

Roadmap review is part of each Sprint closure. The Technical Writer updates the [Engineering Roadmap](ENGINEERING_ROADMAP.md) with completed evolution and the current directional view; the Engineering Manager approves strategic changes.

## Organizational Freeze

From Sprint kickoff through institutional closure, this workflow, role playbooks, the Engineering Audit Checklist, mandatory Sprint templates and Sprint workspace conventions are frozen. `STATUS.md`, published handoff sections, Story artifacts and closure evidence remain living artifacts under their existing owners.

New process ideas are recorded in the [Candidate Improvement Backlog](CANDIDATE_IMPROVEMENT_BACKLOG.md) and are evaluated only in the Engineering Retrospective; they cannot change active Sprint rules. An emergency exception requires Engineering Manager authorization and a published `STATUS.md` record with rationale, scope and effective date.

Process changes must be documented before they become mandatory. Role playbooks may add operational detail but cannot override this workflow.
