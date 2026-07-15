# TEST PLAN — STORY #34

## Objective

Validate the initial reliability behavior for `OrderConfirmed` in Inventory: durable pending registration, local recovery, idempotency, local consistency, and compatibility with the Sprint 2 event baseline.

## Scope

- Inventory Service with the `kafka` profile, PostgreSQL and local Kafka.
- `OrderConfirmed v1` in `mercadoaurora.order.order-confirmed.v1`.
- Existing Inventory REST evidence endpoint and the persistence state required by the Architecture Gate.

## Environment

Java Temurin 21.0.11, Maven 3.9.15, PostgreSQL 16 (`eop-postgres`), Kafka 3.9.0 (`event-platform-kafka-1`), 2026-07-15 America/Sao_Paulo.

## Preconditions

Kafka and PostgreSQL are running locally; Inventory starts with Maven/Spring profile `kafka`; Flyway migration V3 is applied.

## Test scenarios

| ID | Scenario | Acceptance criterion |
| --- | --- | --- |
| INV-034-001 | Smoke and migration | Inventory health is UP and V3 creates a durable processing ledger. |
| INV-034-002 | Durable pending | A pending fact is persisted with identity and traceability before recovery. |
| INV-034-003 | Local recovery | The Inventory-local scheduler completes an eligible pending record without a new Order publication. |
| INV-034-004 | State distinction | The demonstrator can distinguish the same fact as pending and completed through the supported evidence consultation. |
| INV-034-005 | Reprocessing | Two valid deliveries with the same `eventId` create one ledger record and one functional evidence. |
| INV-034-006 | Concurrent recovery | Concurrent recovery attempts preserve one functional effect and consistent final state. |
| INV-034-007 | Automated regression | `mvn -Pkafka test` passes with no failures or errors. |
| INV-034-008 | Contract and scope regression | Verify OrderConfirmed v1, existing REST behavior, producer baseline and no out-of-scope behavior through executed platform regression. |

## Known risks

Inventory Testcontainers 1.21.4 is compatible with Docker API 1.54 and its integration suite executes. Order Service still uses Testcontainers 1.20.1, whose three integration tests are skipped against that Docker API; the equivalent Order → Kafka → Inventory flow is included in the executed QE regression.

## Out of scope

DLQ, Saga, new events/topics, producer changes, advanced observability, distributed tracing and additional metrics.
