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
Engineering Manager — Merge, Release and Sprint Closure
```

No implementation begins before the applicable approval gate. The Software Engineer must stop and escalate when a Story does not meet the Definition of Ready.

## Sprint Lifecycle

1. **Direction and Product Planning:** Program direction and Product Owner planning define the goal, value, scope, priorities, risks and acceptance criteria.
2. **Engineering Review:** Engineering Manager validates capacity, architectural fit and unresolved decisions.
3. **Backlog Materialization:** Product Owner creates the approved backlog, with traceable labels, dependencies and milestones.
4. **Documentation Baseline:** Technical Writer synchronizes planning, architecture references, ADRs, catalogs and governance documents.
5. **Architecture Gate:** Required when infrastructure, cross-service contracts, technology choices or other unresolved technical decisions affect implementation.
6. **Implementation and Quality:** Software Engineer implements approved work; Quality Engineer plans, is authorized and executes validation.
7. **Release and Closure:** Technical Writer prepares release documentation; Engineering Manager merges, publishes releases and closes the Sprint after gates are satisfied.

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

## Authority Matrix

| Decision or action | PO | EM | TW | LT | SE | QE |
| --- | :---: | :---: | :---: | :---: | :---: | :---: |
| Sprint goal, value and business scope | Owns | Reviews | Supports | Consults | Consults | Consults |
| Backlog priorities and functional criteria | Owns | Approves | Supports | Consults | Consults | Consults |
| Architecture, stack and Technical Contract | Consults | Approves | Documents | Refines | Implements | Consults |
| ADR | Consults | Approves | Maintains | Proposes | Contributes | Consults |
| Code and service configuration | Consults | Governs | Documents | Reviews | Owns | Validates |
| Test strategy and evidence | Consults | Authorizes | Documents | Reviews | Contributes | Owns |
| Merge, release and Sprint closure | Informed | Owns | Prepares | Reviews | Never self-merges | Recommends |

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
3. Reviews address code, architecture and quality criteria.
4. Only the Engineering Manager, or an explicitly delegated authority, merges an approved PR.
5. The Story moves to Done only after the defined governance and quality gates are met.

## Release Workflow

1. Technical Writer validates release notes, changelog, history and cross-references.
2. Quality Engineer provides validation evidence and recommendation.
3. Engineering Manager confirms merge state and authorizes the release.
4. The release, tags, issue state, Project Board and milestone are synchronized.

## Continuous Improvement and Organizational Evolution

The Engineering Manager reviews this workflow at Sprint boundaries or when process gaps are discovered. The Technical Writer maintains this document, synchronizes changes across playbooks, validates organizational consistency and preserves cross-references and document versioning.

Roadmap review is part of each Sprint closure. The Technical Writer updates the [Engineering Roadmap](ENGINEERING_ROADMAP.md) with completed evolution and the current directional view; the Engineering Manager approves strategic changes.

Process changes must be documented before they become mandatory. Role playbooks may add operational detail but cannot override this workflow.
