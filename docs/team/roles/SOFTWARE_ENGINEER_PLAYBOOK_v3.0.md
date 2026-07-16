
# SOFTWARE_ENGINEER_PLAYBOOK
**Version:** 3.0
**Status:** Baseline
**Owner:** Engineering Manager

# Purpose

This document defines the mandatory engineering process for every Software Engineer working on the Enterprise Order Platform.

Following this Playbook is mandatory.

---

## Institutional Inheritance

This playbook inherits all institutional engineering rules defined by the [Engineering Workflow](../ENGINEERING_WORKFLOW.md), including the Authority Matrix, Story Workflow, Sprint Closing Workflow, Engineering Audit, Process Improvement, engineering governance and institutional standards. It defines only responsibilities specific to the Software Engineer. When a conflict exists, the Engineering Workflow prevails.

Implementation may begin only after the applicable Architecture Gate and Technical Contract have been approved.

## Sprint Execution Responsibility

The Software Engineer acts only when `STATUS.md` authorizes implementation and the Engineering Manager → SE handoff section is published. The implementation PR contains the SE → QE handoff section with delivered scope, tests, constraints, known risks and QE acceptance/stop criteria; the Software Engineer publishes its own PR and evidence before Quality execution.

---

# Golden Rules

1. Never commit directly to `main`.
2. Never develop on `main`.
3. Always synchronize `main` before creating a feature branch.
4. Every Story MUST have its own feature branch.
5. Every Story MUST generate one Pull Request.
6. Never merge your own PR.
7. Never move a Story to **Done**.
8. Never publish a Release before the PR is approved and merged.
9. Never change architecture, stack or business rules without approval.
10. If something is unclear: **STOP and ask**.

Violation of any rule means the Story is **not delivered**.

For every implementation handoff, publish your own cohesive commit to the remote Story branch after validating the intended diff. Record the branch, commit hash and published code, tests and documentation in the existing implementation handoff or PR; an unpushed workspace is not ready for Quality Engineer review. See the [Versioned Handoff](../ENGINEERING_WORKFLOW.md#versioned-handoff) rule.

---

# Official Stack

- Java 21 LTS
- Spring Boot 3.x
- Maven
- PostgreSQL
- Flyway
- Spring Data JPA
- MapStruct
- JUnit 5
- Mockito
- Testcontainers
- Docker
- REST
- DDD
- Hexagonal Architecture

Any change requires an approved ADR.

---

# End-to-End Workflow

```text
Receive Story
      │
      ▼
Read Story + Playbook
      │
      ▼
Move Story → In Progress
      │
      ▼
Sync main
      │
      ▼
Create Feature Branch
      │
      ▼
Implement
      │
      ▼
Run Tests
      │
      ▼
Update Documentation
      │
      ▼
Push Branch
      │
      ▼
Open Pull Request
      │
      ▼
Move Story → Review
      │
      ▼
Request Review
      │
      ▼
Changes Requested?
   │           │
  Yes          No
   │           │
Update PR      ▼
   └──────► Merge (Repository Owner)
                 │
                 ▼
             Publish Release
                 │
                 ▼
          Engineering Manager
             closes Story
```

---

# Mandatory Startup Procedure

Before writing any code:

1. Read the Story.
2. Read this Playbook.
3. Verify acceptance criteria.
4. Verify milestone.
5. Move Story from **Backlog** to **In Progress**.
6. Assign the Story to yourself (if applicable).
7. Synchronize local repository:

```bash
git checkout main
git pull origin main
```

8. Create the feature branch:

```bash
git checkout -b feature/story-XXX-short-description
```

Only after these steps may implementation begin.

---

# Branch Naming

Examples:

```text
feature/story-009-bootstrap-order-service
fix/story-009-review-adjustments
docs/story-010-update-architecture
```

---

# Engineering Standards

Mandatory:

- SOLID
- Clean Code
- DDD
- Hexagonal Architecture
- DRY
- KISS
- YAGNI

Business rules belong to the Domain.

Controllers must be thin.

Infrastructure must not own business rules.

---

# Testing

Every Story must include:

- Domain Tests
- Application Tests
- Integration Tests

If integration tests cannot execute:

- document the reason;
- include evidence in the PR.

---

# Documentation

Whenever applicable update:

- README
- CHANGELOG
- PROJECT_HISTORY
- ADR
- Architecture documents
- Service README
- Postman Collection
- OpenAPI

Documentation is part of the deliverable.

---

# Pull Request

Every Story MUST generate exactly one Pull Request.

The PR must contain:

- Summary
- Scope
- Out of Scope
- Tests Executed
- Documentation Updated
- Known Limitations

After opening the PR:

- Move Story to **Review**
- Request review from Tech Lead
- Request review from Engineering Manager

Never assume approval.

---

# Responsibilities

| Activity | Engineer |
|---|:---:|
| Move Backlog → In Progress | ✅ |
| Sync main | ✅ |
| Create Feature Branch | ✅ |
| Implement | ✅ |
| Run Tests | ✅ |
| Update Docs | ✅ |
| Open PR | ✅ |
| Move In Progress → Review | ✅ |
| Merge PR | ❌ |
| Publish Release | ❌ (unless delegated) |
| Move Review → Done | ❌ |

---

# Stop Rules

Stop immediately if:

- Story is ambiguous;
- Architecture conflicts with the task;
- A new dependency is required;
- Stack changes are needed;
- A feature branch does not exist;
- You cannot create a PR;
- Domain rules are unclear.

---

# Definition of Ready

Before coding:

- Story understood
- Acceptance criteria clear
- Story moved to In Progress
- Local main synchronized
- Feature branch created

---

# Definition of Done (Engineer)

The Engineer may only report:

**Ready for Review**

Checklist:

- Build passes
- Unit tests pass
- Integration tests pass or limitation documented
- Documentation updated
- Cohesive commit pushed to the official Story branch, with published reference recorded
- PR opened
- Story moved to Review

The Engineering Manager is the role authorized to approve the PR technically and publish the official release (unless delegated). The Repository Owner is exclusively responsible for the definitive merge; the Story moves to Done after the workflow gates and merge are complete.

---

# Final Principle

Process quality is part of product quality.

Working code delivered outside the agreed engineering workflow is considered an incomplete delivery.
