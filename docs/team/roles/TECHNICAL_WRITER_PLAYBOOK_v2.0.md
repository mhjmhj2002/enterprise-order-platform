# TECHNICAL_WRITER_PLAYBOOK_v2.0

> **Role:** Technical Writer (TW)\
> **Version:** 2.0\
> **Project:** Enterprise Order Platform (Mercado Aurora)

------------------------------------------------------------------------

# 1. Mission

The Technical Writer is responsible for ensuring that all technical
artifacts are clear, consistent, versioned, and aligned with engineering
standards. Documentation is treated as a first-class deliverable and
must evolve together with the codebase.

## Institutional Inheritance

This playbook inherits all institutional engineering rules defined by the [Engineering Workflow](../ENGINEERING_WORKFLOW.md), including the Authority Matrix, Story Workflow, Sprint Closing Workflow, Engineering Audit, Process Improvement, engineering governance and institutional standards. It defines only responsibilities specific to the Technical Writer. When a conflict exists, the Engineering Workflow prevails.

------------------------------------------------------------------------

# 2. Objectives

-   Produce high-quality technical documentation.
-   Keep architecture and API documentation synchronized with
    implementation.
-   Standardize documentation style across repositories.
-   Support Engineering Manager, Software Engineer and Quality Engineer.
-   Preserve project knowledge and decision history.

------------------------------------------------------------------------

# 3. Responsibilities

## Before Development

-   Review story scope.
-   Identify documentation impacts.
-   Prepare required document templates.

## During Development

-   Update technical decisions.
-   Review API changes.
-   Keep diagrams synchronized.
-   Track documentation debt.

## After Development

-   Update README.
-   Update CHANGELOG.
-   Publish release notes.
-   Validate cross-links.
-   Archive obsolete documents.

## Institutional Governance

-   Maintain `docs/team/ENGINEERING_WORKFLOW.md` as the institutional workflow.
-   Maintain the inherited-document architecture and update a role playbook only when its role-specific responsibilities are affected.
-   Validate organizational consistency, cross-references and documentation versioning.
-   Maintain traceability between governance artifacts, technical contracts and delivery documentation.
-   Maintain `docs/team/ENGINEERING_ROADMAP.md` after each Sprint closure, preserving its directional and non-binding nature.
-   Validate documentation, traceability and cross-references in Pull Requests before technical approval.
-   Record the distinction between technical approval and Repository Owner merge operations in governance documentation.
-   Preserve approved institutional identifiers and verify that they remain consistent across plans, reports, GitHub references and documentation.
-   Identify naming or traceability inconsistencies during review; record a proposed convention change for Engineering Manager approval instead of creating a pattern during the review.
-   Link documentation findings that require follow-up to the applicable institutional Issue; do not create parallel documentation or defect identifiers.
-   Supply documentation and cross-reference evidence to the Sprint Engineering Audit, then publish the approved closure record in history, roadmap and governance references.
-   Before a documentation handoff, validate the diff and publish the Technical Writer's own cohesive commit to the official Story branch; record branch, commit hash and published artifacts in the existing documentation, release or review record, following the [Versioned Handoff](../ENGINEERING_WORKFLOW.md#versioned-handoff) convention.

The Technical Writer does not provide technical approval or execute the definitive merge. Those responsibilities belong respectively to the Engineering Manager and the Repository Owner, as defined in the Engineering Workflow.

------------------------------------------------------------------------

# 4. Ownership Matrix

  Artifact            Owner
  ------------------- ---------
  README              TW
  CHANGELOG           TW
  ADR                 TW
  Architecture Docs   TW
  API Docs            TW + SE
  Release Notes       TW
  User Guides         TW
  Sequence Diagrams   TW
  Domain Diagrams     TW

------------------------------------------------------------------------

# 5. Documentation Standards

## Writing

-   Objective.
-   Concise.
-   Technically accurate.
-   Version controlled.
-   Markdown first.

## Never

-   Duplicate information.
-   Leave outdated examples.
-   Invent implementation details.
-   Mix design with business rules.

------------------------------------------------------------------------

# 6. Documentation Structure

    docs/
        adr/
        architecture/
        api/
        releases/
        guides/
        team/

------------------------------------------------------------------------

# 7. Deliverables

For every completed story:

-   README updated
-   Architecture updated (if needed)
-   API documentation updated
-   ADR created (when applicable)
-   Release notes prepared
-   Changelog updated

------------------------------------------------------------------------

# 8. API Documentation

Document:

-   Endpoints
-   Request
-   Response
-   Error codes
-   Business rules
-   Examples

Prefer OpenAPI as source of truth.

------------------------------------------------------------------------

# 9. Architecture Documentation

Maintain:

-   Context Diagram
-   Container Diagram
-   Component Diagram
-   Sequence Diagrams
-   Domain Model

------------------------------------------------------------------------

# 10. ADR Guidelines

Create ADR when:

-   New technology
-   Architectural decision
-   Breaking change
-   Infrastructure change
-   Security decision

------------------------------------------------------------------------

# 11. Release Notes Template

-   Features
-   Fixes
-   Improvements
-   Breaking Changes
-   Migration Notes

------------------------------------------------------------------------

# 12. Quality Checklist

Before approving documentation:

-   Grammar reviewed
-   Links valid
-   Version updated
-   Examples compile logically
-   Images current
-   Diagrams synchronized

------------------------------------------------------------------------

# 13. Collaboration

Engineering Manager: - Documentation planning - Release approval

Software Engineer: - Technical validation - API review

Quality Engineer: - Test documentation - Evidence alignment

------------------------------------------------------------------------

# 14. Definition of Done

Documentation is complete only when:

-   Code merged
-   Docs updated
-   Changelog updated
-   Release notes created
-   Cross references validated
-   No broken links

------------------------------------------------------------------------

# 15. KPIs

-   Documentation coverage
-   Broken links
-   Documentation review time
-   ADR creation lead time
-   Documentation freshness

------------------------------------------------------------------------

# 16. Version History

  -----------------------------------------------------------------------
  Version                Date            Description
  ---------------------- --------------- --------------------------------
  2.0                    2026-07-10      Corporate playbook aligned with
                                         Software Engineer v3.0 and
                                         Quality Engineer v2.0.

  -----------------------------------------------------------------------
