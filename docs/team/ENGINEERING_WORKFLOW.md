# Engineering Workflow

**Status:** Institutional Baseline
**Owner:** Engineering Manager
**Maintainer:** Technical Writer
**Purpose:** Single source of truth for engineering governance, handoffs and delivery flow.

## Objective

This document defines how engineering work moves from strategic direction to sprint closure in the Enterprise Order Platform. Role playbooks are specialized operational guides; when a playbook conflicts with this workflow, this document prevails.

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
| AI Engineering Orchestrator | Coordinates agents, preserves task flow and escalates authority gaps. |
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
AI Engineering Orchestrator
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
Engineering Manager — Release and Sprint Closure
```

No implementation begins before the applicable approval gate. The Software Engineer must stop and escalate when a Story does not meet the Definition of Ready.

## Sprint Lifecycle

1. **Direction and Product Planning:** Program direction and Product Owner planning define the goal, value, scope, priorities, risks and acceptance criteria.
2. **Engineering Review:** Engineering Manager validates capacity, architectural fit and unresolved decisions.
3. **Backlog Materialization:** Product Owner creates the approved backlog, with traceable labels, dependencies and milestones.
4. **Documentation Baseline:** Technical Writer synchronizes planning, architecture references, ADRs, catalogs and governance documents.
5. **Architecture Gate:** Required when infrastructure, cross-service contracts, technology choices or other unresolved technical decisions affect implementation.
6. **Implementation and Quality:** Software Engineer implements approved work; Quality Engineer plans, is authorized and executes validation.
7. **Release and Closure:** Technical Writer prepares release documentation; Engineering Manager confirms the technical gates and authorizes release and Sprint closure; Repository Owner performs the repository merge operation after approval.

## Official Handoffs

| From | To | Required handoff |
| --- | --- | --- |
| Product Owner | Engineering Manager | Sprint Plan with scope, priorities, risks and functional criteria. |
| Engineering Manager | Product Owner | Approved or refined planning, including constraints and capacity decisions. |
| Product Owner | Technical Writer | Materialized backlog and approved planning references. |
| Technical Writer | Architecture Gate | Documentation baseline, cross-references and identified decision gaps. |
| Architecture Gate | Engineering Manager | Technical Contract and ADRs when decisions affect architecture or shared infrastructure. |
| Engineering Manager | Software Engineer | Approved, ready technical Story with dependencies and contract references. |
| Software Engineer | Quality Engineer | Implementation handoff, testable environment and technical evidence. |
| Quality Engineer | Engineering Manager | Test Plan, Test Report, defects and release recommendation. |
| Technical Writer | Engineering Manager | Release documentation, changelog and history ready for publication. |
| Engineering Manager | Repository Owner | Technical approval of the PR and confirmation that all institutional gates are complete. |

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
| Release and Sprint closure | Informed | Owns | Executes repository operations when required | Prepares | Reviews | Informed | Recommends |

## Repository Owner Responsibilities

The Repository Owner is responsible exclusively for GitHub repository administration. This role does not participate in engineering technical decisions and does not replace the Product Owner, Engineering Manager, Technical Writer, Technical Lead, Software Engineer or Quality Engineer.

The Repository Owner exclusively:

- performs Pull Request merges after the required institutional gates are complete;
- removes merged temporary branches;
- synchronizes the `main` branch;
- administers Branch Protection Rules, repository permissions and GitHub settings; and
- performs administrative merges when the conditions below are met.

### Administrative Merge

When every technical approval required by this workflow is complete and the only remaining blocker is an operational limitation of GitHub — such as the prohibition on self-approval in a single-maintainer repository — the Repository Owner may perform an **Administrative Merge**.

An Administrative Merge does not replace technical approval, remove any workflow gate or change the engineering decision. It only completes the repository operation after the Engineering Manager has confirmed the technical authorization.

> The official engineering process is defined by the ENGINEERING_WORKFLOW. Native GitHub policies are operational support mechanisms; they do not replace or invalidate formal approvals made by engineering roles.

## Institutional Artifacts

| Artifact | Created by | Approved by | Maintained by |
| --- | --- | --- | --- |
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

## Release Workflow

1. Technical Writer validates release notes, changelog, history and cross-references.
2. Quality Engineer provides validation evidence and recommendation.
3. Engineering Manager confirms the technical merge state and authorizes the release.
4. Repository Owner performs the required GitHub repository operations; the release, tags, issue state, Project Board and milestone are synchronized.

## Continuous Improvement and Organizational Evolution

The Engineering Manager reviews this workflow at Sprint boundaries or when process gaps are discovered. The Engineering Manager approves changes to institutional naming and traceability conventions and prevents parallel nomenclatures. The Technical Writer maintains this document, synchronizes changes across playbooks, validates organizational consistency and preserves cross-references and document versioning.

Roadmap review is part of each Sprint closure. The Technical Writer updates the [Engineering Roadmap](ENGINEERING_ROADMAP.md) with completed evolution and the current directional view; the Engineering Manager approves strategic changes.

Process changes must be documented before they become mandatory. Role playbooks may add operational detail but cannot override this workflow.
