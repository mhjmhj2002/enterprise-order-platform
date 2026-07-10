# EVIDENCE — STORY-009

## Execution identity

- Timestamp: 2026-07-10 11:52–11:53 -03:00
- Branch: `feature/story-009-bootstrap-order-service`
- Commit: `8ebf7d8b9d7118a8615593e4fc9d9cedd134a636`
- PR: #20, OPEN / REVIEW_REQUIRED
- Issue: #14, OPEN

## Environment blockers

```text
docker: snap-confine is packaged without necessary permissions
Testcontainers: DOCKER_HOST unix:///var/run/docker.sock is not listening
```

## Order suite

```text
Tests run: 19, Failures: 0, Errors: 0, Skipped: 3
BUILD SUCCESS
```

Suites executadas: domínio (6 PASS), use cases (8 PASS), Inventory adapter (2 PASS); integração (3 SKIPPED).

## Inventory suite

```text
Tests run: 22, Failures: 0, Errors: 10, Skipped: 6
BUILD FAILURE
```

Domínio: 6 PASS. Integração: 6 SKIPPED. Use cases: 10 ERROR antes das assertions por falha de inicialização do Mockito inline/Byte Buddy no Java 25.

## Contract evidence

- Order expõe oito endpoints separados para criar, reservar, iniciar pagamento, marcar pago, confirmar, cancelar e consultar.
- PaymentFake executa uma operação vazia de sucesso e não contém chave/modo de falha.
- Configurações não expõem health/actuator.
- Inventory adapter chama a API pública de Inventory para reserve/commit/release.

## Postman run supplied after initial execution

- File: `tasks/Enterprise Order Platform - STORY-009 QE.postman_test_run.json`
- Started: `2026-07-10T15:10:53.491Z`
- Collection: `Enterprise Order Platform - STORY-009 QE`
- Requests: 28
- Assertions: 74 PASS / 13 FAIL
- Order/Inventory estavam acessíveis em `localhost:8083` e `localhost:8082`.
- Confirmed product failures: health endpoints 500; malformed Order JSON 500; invalid UUID sanitization assertion failed.
- Inconclusive due to collection data collision: Order reservation/happy path.
- Inconclusive due to missing setup: release and duplicate release.
- The supplied run does not contain response bodies.

## QE collection corrections after run

- Inventory precondition now uses `inventoryReservationId`, independent from the Order happy-path `reservationId`.
- Added creation of `skuId2` and reservation setup before release regression.
- Duplicate commit/release accept controlled HTTP 400 or 409; first commit/release still require HTTP 200.

## Retest — commit 4edbe2c

- Postman started: `2026-07-10T18:42:40.777Z`.
- 33 requests; 102 assertions PASS; 0 FAIL; status `finished`.
- Health: Inventory 200; Order 200.
- Happy path: create 201, reserve 200, start payment 200, mark paid 200, confirm 200 with `CONFIRMED` assertion.
- Validation: invalid UUID 400 sanitized; malformed JSON 400 sanitized.
- Payment failure: isolated order 201, reserve 200, forced failure 503, Inventory query 200.
- Regression: commit 200, duplicate commit 400, release 200, duplicate release 400.
- Order Maven: 26 tests, 0 failures, 0 errors, 3 skipped; BUILD SUCCESS.
- Inventory Maven in QE executor: 6 domain PASS, 6 integration skipped, 10 runner errors due Mockito self-attach on JDK 25. Developer reports 16 PASS / 6 skipped in compatible environment.

## Compensation evidence gap

The supplied run checked only HTTP status after payment failure. QE added explicit checks for persisted Order `CANCELLED`, payment `FAILED`, the matching reservation `RELEASED`, and the Inventory quantity invariant. One final full Collection run is required because compensation depends on the initial Inventory setup.

## Final compensation retest — commit dfa320b

- Started: `2026-07-10T19:11:49.568Z`.
- Status: `finished`.
- 34 requests; 109 assertions PASS; 0 FAIL; total time 637 ms.
- Payment failure returned HTTP 503 without internal details.
- Persisted Order returned HTTP 200, `status=CANCELLED`, `paymentStatus=FAILED`, with the expected reservation reference.
- Inventory returned HTTP 200, the matching reservation in `RELEASED`, and `availableQuantity = physicalQuantity - reservedQuantity`.
- BUG-009-03 / Issue #23 closed after retest.
