# TEST PLAN — STORY #46

## Objective

Validate the initial security baseline: every business route in Catalog,
Inventory and Order rejects absent, malformed or invalid HTTP Basic credentials
without exposing business information or changing business state; the same
capabilities, when called by the configured API consumer, preserve the approved
functional baseline, including Order → Inventory and `OrderConfirmed` v1.

## Scope

- Catalog Service, Inventory Service and Order Service on the official Story
  branch `feature/story-046-security-baseline`.
- Published implementation handoff `8dbb2e2` (technical implementation
  `a7dc0d5ea3e3ac7d2f5b198595a6ad4e805d735b`).
- HTTP Basic boundary on `/api/v1/**`, its technical public exceptions, and
  the existing REST and Kafka functional flow.
- Postman collections `catalog-service.postman_collection.json`,
  `inventory-service.postman_collection.json` and
  `order-service.postman_collection.json` will be updated before execution to
  use non-versioned `apiUsername` and `apiPassword` variables, plus explicit
  unauthenticated/invalid-credential requests. No credential value belongs in
  a collection, environment, evidence or report.

## Environment

- Local isolated environment with Java 21, Maven, PostgreSQL and Docker
  available for the applicable Testcontainers suites.
- Kafka and the `kafka` profile for the end-to-end `OrderConfirmed` v1
  regression.
- `SECURITY_API_USERNAME` and `SECURITY_API_PASSWORD` injected only into the
  service/test runtime. The test operator keeps their values out of terminal
  captures and evidence.

## Preconditions

1. Engineering Manager explicitly authorizes Quality execution after this
   plan is published.
2. The official branch remains published at, or is advanced from, the inbound
   handoff reference; any new implementation commit is reviewed before use.
3. Catalog, Inventory and Order can start with non-empty security environment
   variables. Inventory is reachable by Order with the same configured
   technical credential.
4. The local databases and Kafka environment can be reset or use isolated test
   identifiers, so that unauthorized-write assertions have an observable
   before/after state.

## Remediation update — 2026-07-17

The plan was reviewed against the published remediation
`29b52645529fea0fdfb3507efbe523e2e7e1c6e1`. Its scope is limited to test
infrastructure: Catalog and Order use Testcontainers 1.21.4, and Inventory now
declares `spring-kafka` in test scope so that `mvn test` can discover its Kafka
consumer test. No security contract, business route, credential handling or
acceptance criterion changed. The scenario set above remains valid; the next
execution must rerun `SEC-046-002` before the previously blocked HTTP and
end-to-end scenarios.

## Integration remediation update — 2026-07-17

Reviewed published commit `f10cc53320976bdfcc3c99b6cf2b2973815adb5b`.
It adds an adapter-level assertion that Order sends its configured local Basic
credential to Inventory; the Software Engineer also published successful
authenticated reservation evidence. No contract changed. `ORD-046-003` and
`E2E-046-001` remain the mandatory retest scenarios and require renewed
Engineering Manager authorization before execution.

## Test scenarios

| ID | Scenario | Acceptance criterion |
| --- | --- | --- |
| CAT-046-001 | Catalog smoke and public technical surface | Catalog starts with injected credentials; health, OpenAPI and Swagger are reachable without an Authorization header; a representative `/api/v1/products` read is not. |
| CAT-046-002 | Catalog unauthorized read and write | Missing, malformed and invalid Basic credentials return generic `401` with `WWW-Authenticate`; an unauthorized product write creates no product and no business response data is exposed. |
| CAT-046-003 | Catalog authenticated regression | Valid Basic credentials preserve representative product/SKU create, update and query status/payload semantics. |
| INV-046-001 | Inventory boundary and no-effect write | A representative inventory read and reserve/adjust write without valid credentials return generic `401`; the subsequent authenticated query proves no inventory/reservation change from the rejected request. |
| INV-046-002 | Inventory authenticated and operational-query regression | Valid credentials preserve inventory adjustment, reservation lifecycle and the approved confirmation-processing/observation queries. |
| ORD-046-001 | Order boundary and no-effect command | An unauthenticated order creation or state command returns generic `401`; authenticated lookup proves that no order or reservation was created or changed by it. |
| ORD-046-002 | Order authenticated business regression | Valid credentials preserve create, reserve, payment, confirm, cancel/error and customer-order query semantics, including existing sanitized domain/validation errors. |
| ORD-046-003 | Order → Inventory authenticated integration | A valid consumer request that causes Order to call Inventory succeeds without forwarding the inbound credential; correlation propagation and existing REST contract remain intact. |
| E2E-046-001 | Confirmed-order event regression | With `kafka` enabled, an authenticated confirmed order preserves reservation/commit behavior, `OrderConfirmed` v1 publication and Inventory processing/evidence. |
| SEC-046-001 | Configuration and secret-safety regression | Each service fails predictably when either required credential is absent/empty; invalid credentials do not distinguish username from password and responses/log evidence contain no secret. |
| SEC-046-002 | Automated regression | Applicable service suites, including the Kafka profile suites, complete without failures/errors; any environment-caused skip is recorded as a limitation rather than treated as a pass. |

## Acceptance criteria

- Every `/api/v1/**` business route sampled across the three services requires
  valid authentication and returns generic `401` plus `WWW-Authenticate` for
  missing, malformed or invalid credentials.
- At least one unauthorized write in each affected business context proves no
  resulting state change; no unauthorized read reveals business information.
- Authenticated calls retain the existing REST methods, paths, payloads,
  status semantics and domain validation behavior.
- The authenticated confirmed-order flow preserves Order → Inventory REST,
  `X-Correlation-Id`, `OrderConfirmed` v1 and Inventory processing behavior.
- Health, OpenAPI and Swagger UI remain deliberately public technical
  exceptions; no additional business exception is accepted.
- No critical or high defect remains, and evidence is reproducible without
  recording credentials.

## Known risks

- Local Docker/Testcontainers or Kafka availability can block integration
  evidence; the report must distinguish that limitation from a passed test.
- A shared credential can mask an incorrect internal Order → Inventory setup;
  the integration scenario must observe the business outcome, not only HTTP
  success.
- HTTP Basic is valid only for the authorized local baseline. Any remote/TLS,
  gateway, identity, role, audit or credential-rotation requirement is outside
  this plan and requires escalation.

## Out of scope

TLS or remote deployment; gateway, IdP, tokens, credential rotation; users,
roles, ownership and authorization by resource; access audit; new business
contracts, events or topics; and changes to domain behavior.

## Institutional Handoff — Quality Engineer → Engineering Manager

### Executive summary

Quality planning was updated after the published test-infrastructure
remediation. The plan still maps the approved authentication contract to
reproducible smoke, negative, authenticated regression and end-to-end
scenarios. No retest has been executed under this update.

### Published artifacts

- `docs/quality/story-046/TEST_PLAN.md`
- `docs/team/sprints/sprint-005/STATUS.md`

### Versioned reference

- Branch: `feature/story-046-security-baseline`
- Remediation reviewed: `29b52645529fea0fdfb3507efbe523e2e7e1c6e1`.
- Updated-plan commit: recorded after the Quality Engineer publishes this plan.
- Inbound implementation: `8dbb2e2` / `a7dc0d5ea3e3ac7d2f5b198595a6ad4e805d735b`.

### Evidence and constraints

- Planning is based only on the published implementation handoff, Story #46
  product criteria, Architecture Gate and service documentation.
- Execution requires Engineering Manager authorization; no test result or
  release recommendation is implied by this plan.
- Credentials must be injected at runtime and redacted from all Quality
  artifacts.

### Risks and pending items

- Pending: renewed Engineering Manager authorization for Quality execution.
- Before execution, QE will update the three Postman collections as described
  in scope, without versioning a credential, then rerun the complete plan.

### Next authorized action

- Next role: Engineering Manager.
- Required action: review this Test Plan and explicitly authorize or reject
  Quality execution.
- Acceptance / stop criteria: authorize only if the official implementation is
  still published and a reproducible local environment can inject credentials;
  otherwise return to the producing role or escalate the blocking condition.
