# TEST REPORT — STORY #46

## Story and version

Story #46 — Baseline inicial de segurança. Executed against
`feature/story-046-security-baseline` / `9063416`, with implementation
`a7dc0d5ea3e3ac7d2f5b198595a6ad4e805d735b`.

## Environment

2026-07-17, America/Sao_Paulo; Java 21.0.11, Maven 3.9.15, Docker 29.3.1/API
1.54, PostgreSQL 16 and Kafka 3.9.0. Test credentials were injected through
environment variables and are not recorded here.

## Services tested

Catalog, Inventory and Order automated suites; no HTTP service was accepted as
validated because the required integration execution did not complete.

## Executed scenarios

| ID | Result | Evidence |
| --- | --- | --- |
| CAT-046-001 | BLOCKED | `mvn test`: 18 unit/domain tests passed; 6 integration tests skipped. Catalog Testcontainers 1.20.1 uses Docker API 1.32, below the daemon minimum 1.40. |
| CAT-046-002 | BLOCKED | Requires Catalog HTTP/integration evidence; unavailable under the preceding Testcontainers incompatibility. |
| CAT-046-003 | BLOCKED | Requires authenticated integration evidence; unavailable. |
| INV-046-001 | BLOCKED | Requires stateful HTTP/integration evidence. |
| INV-046-002 | BLOCKED | Requires stateful HTTP/integration evidence. |
| ORD-046-001 | BLOCKED | Order integration execution did not begin because the combined regression stopped at Inventory. |
| ORD-046-002 | BLOCKED | Same limitation. |
| ORD-046-003 | BLOCKED | Same limitation. |
| E2E-046-001 | BLOCKED | Kafka end-to-end integration was not completed. |
| SEC-046-001 | BLOCKED | Startup/configuration and redacted HTTP evidence not completed. |
| SEC-046-002 | FAILED | `services/inventory-service: mvn test` fails test discovery: `ClassNotFoundException: org.apache.kafka.clients.consumer.ConsumerRecord` in `KafkaOrderConfirmedEventConsumerTest`. With `-Pkafka`, the selected Inventory unit/Kafka tests execute without failures in the standard environment; this does not repair the required default regression command. |

## Passed / failed / blocked

- Passed: 18 Catalog unit/domain tests; selected Inventory unit/Kafka tests.
- Failed: the required Inventory default automated-regression command.
- Blocked: all HTTP, state-change, cross-service and Kafka end-to-end evidence;
  Catalog Testcontainers/Docker API incompatibility also prevents its
  integration suite.

## Known limitations and open bugs

- Docker is available, but Catalog Testcontainers 1.20.1 negotiates API 1.32
  while the Docker daemon requires at least 1.40.
- Inventory's default test command discovers a Kafka test without the Kafka
  client classpath. This is a reproducible build/test regression, not a pass.
- The Engineering Manager must decide whether either finding requires a formal
  GitHub Issue. QE creates no parallel defect tracker.

## Final recommendation

**VALIDATION REJECTED.** No merge or release recommendation is made. The
mandatory negative, authenticated, integration and end-to-end scenarios lack
completed reproducible evidence, and automated regression is failing.

## Retest update — 2026-07-17

The remediated automated suites passed: Catalog (24 tests), Inventory/Kafka
and Order (29 tests) had no failures, errors or skips. HTTP smoke evidence
also passed for all services: health `200`, business route without credentials
`401`, and authenticated representative calls returned their expected status.
An unauthenticated Catalog write returned `401`.

The authenticated manual Order → Inventory flow was then executed with a new
inventory item and order. `POST /api/v1/orders/{orderId}/reserve-stock`
returned **502**, preventing payment, confirmation and `OrderConfirmed`
evidence. This is a reproducible blocking integration defect in scenario
`ORD-046-003` / `E2E-046-001`.

**Updated recommendation: VALIDATION REJECTED.** The Engineering Manager must
route the finding to Software Engineering; no merge, release or documentation
advance is recommended.

## Institutional Handoff — Quality Engineer → Engineering Manager

### Published artifacts

- `docs/quality/story-046/TEST_REPORT.md`
- `docs/team/sprints/sprint-005/STATUS.md`

### Evidence and constraints

- Test execution remained within the approved Test Plan and did not expose a
  credential.
- This rejection does not prescribe an implementation. Retesting requires a
  published corrective handoff and new Quality authorization if the branch
  changes.

### Next authorized action

- Next role: Engineering Manager.
- Required action: assess the reported regressions/limitations, decide formal
  tracking and route corrective work or environment remediation.
- Acceptance / stop criteria: do not advance to documentation, merge or release
  until all blocked scenarios execute and the automated regression passes.
