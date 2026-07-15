# Engineering Roadmap

**Status:** Living institutional document
**Owner:** Engineering Manager
**Maintainer:** Technical Writer
**Last reviewed:** 2026-07-15

## Purpose

This document is the official engineering view of the platform's evolution: where it started, its current state and the direction currently intended for future Sprints. It complements [Project History](PROJECT_HISTORY.md), which records completed events, and the individual Sprint Plans, which define approved scope.

## Roadmap Evolution

The roadmap is a living artifact, not a rigid delivery commitment. Future Sprints represent the best information available when this document was updated. They may be reordered, split, combined, removed or expanded as the team learns.

At the end of every Sprint, Engineering reviews this roadmap considering:

- outcomes and technical learnings;
- changes in priorities and business needs;
- risks, dependencies and capacity;
- opportunities revealed by the delivered increment.

Only approved Sprint Plans and technical contracts authorize work. A roadmap entry alone does not create Stories, change architecture or authorize implementation.

## Completed Evolution

### Sprint -1 — Project Bootstrap

**Objective:** Establish the repository, documentation foundation and GitHub governance needed to begin delivery with traceability.

**Key deliveries:** monorepo structure, initial README and changelog, Team Charter, roadmap, Project History, ADR-001/ADR-002, issues, milestones, Project Board and bootstrap release.

**Learnings:** governance and documentation must be established before service delivery so planning, decisions and releases remain traceable.

### Sprint 0 — Architectural Foundation

**Objective:** Establish the business and architectural baseline before implementation.

**Key deliveries:** Business Discovery, Domain Glossary, Business Flow, Context Map, Service Boundaries, Architecture Overview, Engineering Handbook and ADR organization.

**Learnings:** shared business language and explicit boundaries reduce ambiguity before services and contracts are introduced.

### Sprint 1 — REST Microservices

**Objective:** Deliver Catalog, Inventory and Order Services as the first functional platform baseline.

**Key deliveries:** three Spring Boot services, DDD and hexagonal architecture, PostgreSQL/Flyway persistence, synchronous Order-to-Inventory REST integration, `PaymentFakeAdapter`, ADR-004 to ADR-006, quality evidence and release `v0.3.0-order-service`.

**Learnings:** incremental integration preserved service boundaries and showed that observability, compensation and documentation must evolve alongside behavior.

## Previous Sprint

### Sprint 2 — Event-Driven Architecture

**Status:** Baseline delivered; administrative Sprint closure is governed separately.

**Objective:** Introduce the first asynchronous, event-driven increment between Order and Inventory while preserving the existing REST baseline.

**Approved scope:**

- `OrderConfirmed` v1 as the initial domain event.
- Order as producer and Inventory as the initial consumer.
- Local, reproducible event-platform environment and inspectable event flow.
- Traceable processing, protection against duplicate business effects and evidence without regression of relevant REST flows.

**Architectural decisions:** REST and events coexist; Kafka is introduced incrementally; the event contract and platform constraints are governed by the Event Catalog, ADR-007 and the Event Platform Technical Contract. Payment Service, Saga, API Gateway, Kubernetes, Schema Registry and a full REST migration remain outside this Sprint.

**Current Stories:** Story state is authoritative in the GitHub milestone and Project Board. This roadmap records the Sprint direction and does not duplicate operational status, which changes throughout delivery.

**Expected result:** a documented, reproducible end-to-end `OrderConfirmed` v1 flow with publication, consumption, traceability and quality evidence.

**Capacity decision (2026-07-15):** Story #34 was re-planned to Sprint 3 and
is not part of the Sprint 2 baseline. The baseline records the delivered
limited retry behavior and explicitly excludes DLT and additional observable
reliability evolution.

References: [Sprint 2 Product Plan](sprints/SPRINT_2_PRODUCT_PLAN.md), [Event Catalog](../architecture/events/EVENT_CATALOG.md), [ADR-007](../architecture/ADR/ADR-007-incremental-event-driven-architecture.md) and [Project History](PROJECT_HISTORY.md).

### Sprint 2 — Organizational evolution

The Repository Owner responsibilities and the Administrative Merge concept were institutionalized during Sprint 2. Technical approval remains with the engineering roles defined in the [Engineering Workflow](ENGINEERING_WORKFLOW.md); repository administration and the definitive GitHub merge belong exclusively to the Repository Owner.

## Current Sprint

### Sprint 3 — Initial Reliability

**Status:** Final Engineering Review approved — Story #34 is technically complete and ready for Repository Owner administrative closure.

**Objective:** Make the Inventory processing of `OrderConfirmed` v1 recoverable after temporary failures while preserving the approved event and REST baseline.

**Delivered increment:** Inventory records a durable, idempotent processing pending item before acknowledgment, recovers pending work locally, and exposes `PENDING`/`COMPLETED` processing state for demonstration. The topic, envelope, producer, consumer group and existing REST behavior remain unchanged.

**Quality evidence:** the Quality Gate is **APPROVED WITH OBSERVATIONS**. The non-blocking observation is the Order Service Testcontainers follow-up; its equivalent cross-service flow was executed live. The Final Engineering Review classified this as future technical debt and authorized administrative closure.

References: [Sprint 3 Product Plan](sprints/SPRINT_3_PRODUCT_PLAN.md), [Story #34 Architecture Gate](../architecture/contracts/STORY_034_ARCHITECTURE_GATE.md), [Sprint 3 Quality Evidence](../quality/SPRINT_3_QUALITY_EVIDENCE.md) and [Final Engineering Review](sprints/SPRINT_3_FINAL_ENGINEERING_REVIEW.md).

## Current Engineering Outlook

The following Sprints are directional only. Their order and scope will be reviewed after each delivered increment.

| Sprint | Direction | High-level objective |
| --- | --- | --- |
| Sprint 4 | Observability | Improve visibility of platform behavior, dependencies and operational evidence. |
| Sprint 5 | Security | Establish the next security baseline appropriate to the evolved platform. |
| Sprint 6 | API Gateway | Evaluate and introduce an API entry-point strategy when justified by platform needs. |
| Sprint 7 | Payment Service | Explore an independent payment bounded context after the supporting platform capabilities are mature enough. |
| Sprint 8 | Saga | Evaluate distributed coordination only after event and payment boundaries have been validated. |
| Sprint 9 | Platform / Kubernetes | Evaluate platform orchestration and deployment capabilities based on operational needs. |

No row above constitutes a commitment, approved backlog or implementation authorization.

## Governance

Roadmap maintenance follows the [Engineering Workflow](ENGINEERING_WORKFLOW.md). The Technical Writer updates this document after Sprint closure; the Engineering Manager reviews strategic changes and approves any decision that changes architecture, priorities or delivery scope. Repository Owner operations do not change roadmap authority.
