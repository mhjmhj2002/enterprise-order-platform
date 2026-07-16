# TEST PLAN — STORY #44

## Objective

Validate the local, additive operational observation for `OrderConfirmed` v1 in Inventory, including durable lifecycle history, safe temporary-failure visibility, recovery, idempotency and Sprint 3 compatibility.

## Scope

- Inventory Service, PostgreSQL, Kafka and the published operational endpoint.
- Existing Order → Kafka → Inventory and REST reservation/commit baseline.
- Story #44 branch `feature/story-044-platform-observability`, technical commit `4c7811f7d73b80fd2d73dfbea6a2f59edbdd2788`.

## Environment

Temurin 21.0.11, Maven 3.9.15, PostgreSQL 16, Kafka 3.9.0 and Docker Engine 29.3.1/API 1.54, 2026-07-16 America/Sao_Paulo.

## Scenarios

| ID | Scenario | Acceptance criterion |
| --- | --- | --- |
| INV-044-001 | Automated regression | `mvn -Pkafka test` completes with 35 tests, no failures/errors/skips. |
| INV-044-002 | Completed observation | A confirmed Order is associated with one completed `OrderConfirmed v1` observation. |
| INV-044-003 | Temporary failure | A post-registration temporary failure exposes only the safe category and lifecycle state. |
| INV-044-004 | Recovery and idempotency | Recovery reaches COMPLETED with one evidence/functional result per `eventId`, including concurrent attempts. |
| INV-044-005 | Contract regression | Existing REST reserve/commit and Kafka envelope/topic behavior remain intact. |
| INV-044-006 | Safe exposure and limits | Response contains contracted identity/tracing/status fields only; it contains no payload, credentials, stack trace or raw exception. |

## Out of scope

DLQ, new events/topics/consumers, producer changes, Inventory → Order calls, dashboards, alerts, metrics, tracing, reprocessing API and corporate retention policy.
