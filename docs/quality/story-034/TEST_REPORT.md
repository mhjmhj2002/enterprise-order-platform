# TEST REPORT — STORY #34

## Result

**APPROVED WITH OBSERVATIONS — final QE retest.** All Story #34 Architecture Gate conditions were observed in executable scenarios: durable recovery after temporary persistence failure, pending/completed consultation, concurrent recovery, and the Order → Kafka → Inventory baseline flow. The Order Testcontainers limitation remains documented, but its equivalent cross-service behavior was executed against local Kafka and PostgreSQL.

## Reference

| Item | Value |
| --- | --- |
| Story | #34 — Initial Reliability |
| Pull Request | #43 `feat(inventory): add durable OrderConfirmed recovery` |
| Remote branch at handoff | `feature/story-034-initial-reliability` @ `3342d97c0ff09eaf43789223cba61cd9fe27b53a` |
| Validated technical baseline | `6c1189f test(inventory): support current Docker API` (ancestor of remote handoff HEAD) |
| Date | 2026-07-15 (America/Sao_Paulo) |
| Service exercised | Inventory Service |
| Contract | [Event Platform Technical Contract](../../architecture/contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md) |
| Architecture Gate | [Story #34 Architecture Gate](../../architecture/contracts/STORY_034_ARCHITECTURE_GATE.md) |
| Environment | Temurin 21.0.11, Maven 3.9.15, PostgreSQL 16, Kafka 3.9.0 |

## Count

| Executed | Passed | Failed | Blocked |
| ---: | ---: | ---: | ---: |
| 9 | 9 | 0 | 0 |

## Executed scenarios and evidence

| ID | Result | Reproducible observed evidence |
| --- | --- | --- |
| INV-034-001 | Passed | Inventory started with profile `kafka`; `GET /actuator/health` returned `{"status":"UP"}`. Flyway applied V3 and `order_confirmation_processing` exists with primary key `event_id`, PENDING/COMPLETED constraint and pending index. |
| INV-034-002 | Passed | Inserted test identity `34000000-0000-0000-0000-000000000001` as `PENDING`; PostgreSQL returned the same `event_id`, `PENDING`, attempt count 0 and null `completed_at`. The record includes correlation ID, order ID, topic, partition and offset. |
| INV-034-003 | Passed | Without publishing from Order, the local Inventory scheduler changed that record to `COMPLETED`, attempt count 1, and one evidence row. `GET /api/v1/inventory/order-confirmations/34000000-0000-0000-0000-000000000003` then returned the same event identity and tracing fields. |
| INV-034-004 | Passed on retest | At `ec89ea0`, `GET /api/v1/inventory/order-confirmation-processings/34000000-0000-0000-0000-000000000023` returned the inserted identity as `PENDING`, attempt count 0 and Kafka traceability. After local recovery, the same endpoint returned `COMPLETED`, attempt count 1 and `completedAt`; the unchanged evidence endpoint returned one completed evidence. |
| INV-034-005 | Passed | Published two valid `OrderConfirmed v1` records with identical `eventId=34000000-0000-0000-0000-000000000011` to the approved topic. PostgreSQL returned `ledger_rows=1`, `evidence_rows=1`, `status=COMPLETED`; HTTP returned one evidence with offset 14. |
| INV-034-007 | Passed on final retest | `mvn -Pkafka test` at `6c1189f` completed `BUILD SUCCESS`: 34 tests, 0 failures, 0 errors and 0 skipped. Testcontainers 1.21.4 connected to Docker Engine 29.3.1/API 1.54. |
| INV-034-006 | Passed on final retest | All 8 `InventoryIntegrationTest` scenarios executed (0 skipped), including the delivered concurrent recovery scenario. The test suite completed with no failures/errors, demonstrating one evidence per `eventId` under concurrent recovery. |
| INV-034-008 | Passed | Live Order → Kafka → Inventory flow: order `87b7da13-16f2-4152-9e21-5ac1106e3f38` completed CREATED → STOCK_RESERVED → PAYMENT_PENDING → PAID → CONFIRMED; Inventory REST reservation `34000000-0000-0000-0000-000000000044` was COMMITTED and physical stock changed 20 → 18. Inventory then exposed one completed `OrderConfirmed v1` processing at Kafka offset 17. |
| INV-034-009 | Passed | Stopped local PostgreSQL for two seconds before durable registration, published `eventId=34000000-0000-0000-0000-000000000031`, then restored PostgreSQL. The same event was later persisted and completed exactly once (`attemptCount=1`, `evidence_count=1`, Kafka offset 16), demonstrating no silent loss in the injected temporary-failure window. |

## Architecture Gate conditions

| Condition | Outcome | Evidence |
| --- | --- | --- |
| No silent loss before durable persistence | Passed | INV-034-009 injected temporary PostgreSQL unavailability before registration and observed one eventual completed processing/evidence after restoration. |
| `eventId` idempotency including concurrent resumption | Passed | Duplicate live delivery yielded one ledger/evidence and the real Testcontainers integration scenario for concurrent recovery executed successfully. |
| Demonstrable pending vs completed | Passed on retest | INV-034-004 at `ec89ea0` validates the supported processing-state endpoint. |
| Local recognition/completion consistency | Passed | Local recovery produced one completed processing and one evidence in the same observed outcome; concurrent integration also passed. |
| Preserve OrderConfirmed v1 | Passed for exercised delivery | Two valid v1 envelopes consumed from the approved topic. |
| Service responsibilities preserved | Passed | INV-034-008 executed the existing Order producer, Kafka topic and Inventory consumer alongside preserved REST reserve/commit behavior; no additional service responsibility was required. |

## Limitations

- The former Testcontainers/Docker limitation is resolved: Testcontainers 1.21.4 executed all eight integration tests against Docker API 1.54.
- The supported pending-state endpoint was validated live at `ec89ea0`; the prior visibility defect is resolved.
- Order Service still uses Testcontainers 1.20.1, so three Order integration tests were skipped against Docker API 1.54. `mvn test` nevertheless passed with 25 executed tests, and the equivalent Order → Kafka → Inventory/REST behavior was executed live in INV-034-008.

## Open defect

**Resolved on `ec89ea0`: Story #34 / INV-034-004.** The new processing endpoint returns `PENDING`/`COMPLETED`, attempts and traceability without changing the existing completed-evidence response. No new product defect was opened in this retest.

## Final recommendation

**APPROVED WITH OBSERVATIONS.** Story #34 meets the functional and Architecture Gate conditions with reproducible live evidence. Recommendation: update Order Service Testcontainers in a follow-up to restore its three automated integration tests on the current Docker API; this does not block the Story because its equivalent baseline flow was executed successfully.

## Reproduction commands

```bash
cd services/inventory-service
source /home/mhj/.sdkman/bin/sdkman-init.sh
mvn -Pkafka test
mvn -Pkafka spring-boot:run -Dspring-boot.run.profiles=kafka
```

With Kafka and PostgreSQL local, insert a PENDING record in `order_confirmation_processing` and query its order through `GET /api/v1/inventory/order-confirmation-processings/{orderId}` before the five-second recovery cadence. The response exposes `PENDING`; after recovery, the same consultation exposes `COMPLETED`.
