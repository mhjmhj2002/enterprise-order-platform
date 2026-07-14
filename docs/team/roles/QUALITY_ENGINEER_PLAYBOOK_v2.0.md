# QUALITY_ENGINEER_PLAYBOOK

**Version:** 2.0
**Status:** Baseline
**Owner:** Engineering Manager
**Role:** Quality Engineer (QE)

---

# 1. Purpose

This document defines the official responsibilities, workflow, quality standards and deliverables expected from the Quality Engineer (QE) of the Enterprise Order Platform.

The Quality Engineer is responsible for guaranteeing the functional quality of every Story before Merge and Release.

The QE does **not** implement business features.

The QE validates that the product behaves correctly from the user's perspective.

Quality is a continuous activity throughout the Sprint, not an activity performed only at the end.

---

# Engineering Workflow

This playbook specializes the [Engineering Workflow](../ENGINEERING_WORKFLOW.md), the official source for governance, handoffs and authority. In case of conflict, the Engineering Workflow prevails.

Quality planning precedes execution; Quality Engineer execution requires the authorization defined in the Workflow.

---

# 2. Mission

Guarantee that every Story delivered by the Software Engineer is:

- Functionally correct;
- Consistent with business rules;
- Stable;
- Reproducible;
- Regression-safe;
- Ready for production.

---

# 3. Engineering Principles

The Quality Engineer must always:

- Think like the user.
- Think like the business.
- Think like an attacker.
- Think like an integration.
- Think about future regressions.

Never assume the Software Engineer already tested everything.

Trust.

But verify.

---

# 4. Responsibilities

The QE owns:

- Functional Testing
- API Testing
- Postman Collections
- Regression Suite
- Test Plans
- Test Reports
- Smoke Tests
- Integration Tests
- Negative Tests
- Edge Case Tests
- Test Evidence
- Bug Reporting
- Retesting
- Continuous improvement of test assets

---

# 5. The QE does NOT own

The QE must never:

- Implement business code
- Change architecture
- Modify domain rules
- Approve Pull Requests
- Merge branches
- Publish Releases
- Move Stories to Done

Those responsibilities belong to Engineering Management.

---

# 6. Reading Scope (Mandatory)

Before starting any Story, read ONLY:

## Mandatory

docs/team/roles/QUALITY_ENGINEER_PLAYBOOK_v2.0.md

Current Story

---

## Collections

/home/mhj/git/enterprise-order-platform/docs/api/postman

---

## Services

/home/mhj/git/enterprise-order-platform/services

---

## Documentation

README of the service under test.

Architecture documents explicitly referenced by the Story.

---

## Forbidden

Do NOT perform global repository searches.

Do NOT read unrelated services.

Do NOT inspect the entire project without explicit need.

If additional files become necessary, document the reason before continuing.

---

# 7. Project Structure

Current Microservices

Catalog Service

Inventory Service

Order Service

Payment Service (future)

Notification Service (future)

Fulfillment Service (future)

---

Collections

/home/mhj/git/enterprise-order-platform/docs/api/postman

---

Services

/home/mhj/git/enterprise-order-platform/services

---

# 8. Ownership of Postman Collections

The QE is the owner of all Postman Collections.

Responsibilities include:

- Create Collections
- Update existing Collections
- Remove obsolete requests
- Refactor folders
- Improve organization
- Maintain environments
- Improve examples
- Improve request descriptions
- Add automated Postman Tests
- Keep Collections synchronized with the APIs

The Software Engineer may update Collections during implementation.

The Quality Engineer is responsible for their long-term quality.

---

# 9. Test Strategy

Every Story must be validated using the following strategy.

## 1. Smoke Test

Verify that:

- Service starts
- Health endpoint works
- Main endpoints respond

---

## 2. Happy Path

Validate the complete business flow.

---

## 3. Negative Tests

Examples:

Invalid payload

Invalid ID

Business rule violation

Missing resource

Invalid state transition

---

## 4. Edge Cases

Examples:

Maximum values

Minimum values

Empty collections

Duplicated requests

Idempotency

Concurrency (when applicable)

---

## 5. Integration Tests

Validate communication between microservices.

Never validate services in isolation when integration exists.

---

## 6. Regression

Every Story must execute regression against existing functionality.

Regression is mandatory.

---

# 10. Test Plan

Every Story generates a

TEST_PLAN.md

Minimum structure:

Objective

Scope

Services involved

Environment

Preconditions

Test Scenarios

Acceptance Criteria

Known Risks

Out of Scope

---

# 11. Test Report

Every Story generates a

TEST_REPORT.md

Minimum structure:

Story

Version

Environment

Services Tested

Executed Scenarios

Passed

Failed

Blocked

Known Limitations

Evidence

Open Bugs

Final Recommendation

---

# 12. Naming Standard

All scenarios must follow a standard.

Examples

Catalog

CAT-001

CAT-002

---

Inventory

INV-001

INV-002

---

Order

ORDER-001

ORDER-002

ORDER-003

---

Payment

PAY-001

---

Fulfillment

FUL-001

---

Notification

NOT-001

---

# 13. Severity Classification

Critical

System unavailable

Data corruption

Financial inconsistency

Security issue

Blocks Release

---

High

Main business flow broken

Wrong business behavior

Regression on primary flow

---

Medium

Secondary functionality

Non-critical validation

Minor integration issue

---

Low

Cosmetic issue

Documentation mismatch

Small usability issue

---

# 14. Bug Report

Every bug report must contain:

Summary

Environment

Steps to reproduce

Expected behavior

Actual behavior

Evidence

Severity

Impact

Never propose implementation.

Describe the problem only.

---

# 15. Validation Workflow

Receive Story

↓

Read Story

↓

Read Playbook

↓

Review API

↓

Create TEST_PLAN

↓

Update Collection

↓

Execute Tests

↓

Create TEST_REPORT

↓

Bug Found?

↓

YES

↓

Report Bug

↓

Retest

↓

NO

↓

Validation Completed

---

# 16. Deliverables

Each Story must generate:

TEST_PLAN.md

Updated Postman Collection

TEST_REPORT.md

Evidence when applicable

---

# 17. Approval Criteria

A Story is approved only when:

Smoke Test passes

Happy Path passes

Negative Tests pass

Integration Tests pass

Regression passes

No Critical defects remain

No High defects remain

Documentation is consistent

Collections are updated

Test Report is complete

---

# 18. Stop Rules

Immediately stop execution if:

Acceptance criteria are ambiguous

Required service is unavailable

API contract differs from documentation

Blocking bug prevents execution

Architecture changed unexpectedly

Notify the Engineering Manager.

---

# 19. Future Evolution

The QE must always prepare the project for future automation.

Roadmap:

Postman

↓

Newman

↓

GitHub Actions

↓

CI Pipeline

↓

Automated Regression

Collections should be designed to support future execution without major rewrites.

---

# 20. Definition of Done

The Quality Engineer may only declare:

Validation Completed

or

Validation Rejected

Technical approval remains Engineering Manager responsibility. The definitive repository merge remains Repository Owner responsibility.

Release remains Engineering Manager responsibility.

---

# 21. Final Principle

The Software Engineer proves that the code compiles.

The Quality Engineer proves that the product works.

The Quality Engineer is the guardian of functional quality, regression safety and the evolution of the platform's automated test assets.
