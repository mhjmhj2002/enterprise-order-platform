# TEST PLAN — STORY-009

## Objective

Validar funcionalmente o Order Service, sua integração REST síncrona com o Inventory Service, o ciclo de estados, compensações, persistência e regressão mínima do Inventory.

## Scope

- Order Service: criação, consulta, transições, validações, persistência e contrato REST.
- Inventory Service: criação/consulta de item, reserva, commit, release e invariantes.
- Integração Order → Inventory e `PaymentFakeAdapter`.
- Collection e environment Postman da Story-009.

## Out of Scope

Kafka, Saga por eventos, Payment Service real, carga formal, autenticação, frete, tributos, cupons e integração obrigatória com Catalog.

## Environment

- Data: 2026-07-10 (America/Sao_Paulo)
- Branch: `feature/story-009-bootstrap-order-service`
- Initial commit: `8ebf7d8b9d7118a8615593e4fc9d9cedd134a636`
- Final retest commit: `dfa320bef679ebe1e454b5bd43fda8bd81aa9483`
- Java: Temurin 25.0.3 (projeto declara Java 21)
- Docker/PostgreSQL: indisponível no executor (`/var/run/docker.sock` indisponível e snap sem capabilities)
- URLs planejadas: Order `http://localhost:8083`; Inventory `http://localhost:8082`

## Services

- `services/order-service`
- `services/inventory-service`

## Dependencies

- PostgreSQL separado para Order e Inventory.
- Inventory disponível antes do fluxo Order.
- PaymentFake configurável para sucesso e falha.
- Docker/Testcontainers para integrações automatizadas.

## Test Data

| Dado | Valor planejado |
|---|---|
| warehouseId | `00000000-0000-0000-0000-000000000001` |
| customerId | gerado pelo script Postman |
| skuId / skuId2 | gerados pelo script Postman |
| reservationId / reservationId2 | gerados pelo script Postman |
| physicalQuantity | 20 por SKU |
| quantity | 2 |

Nenhum ID de runtime foi gerado, pois os bancos e serviços não puderam ser iniciados.

## Test Scenarios

Os cenários CT-001 a CT-050 definidos no guia são a baseline. A ordem é: smoke, massa, Inventory isolado, happy path, contrato, falhas, compensação, estados, persistência, duplicidade, regressão e cleanup.

Priorização bloqueante: CT-001, CT-002, CT-004, CT-006, CT-017 a CT-027 e regressão Inventory.

## Entry Criteria

- PR #20 aberto e branch correta: atendido.
- Commit local igual ao head do PR: atendido.
- Docker/PostgreSQL e serviços disponíveis no executor QE inicial: não atendido; execução Postman posterior comprovou ambos os serviços acessíveis no ambiente do usuário.
- PaymentFake configurável para falha: não atendido.
- Collection carregável: atendido; corrigida novamente após o primeiro run para separar massas de reserva e preparar regressão de release.

## Exit Criteria

- Smoke, happy path, integração, compensação e regressão aprovados.
- Nenhum bug Critical/High aberto.
- Evidências, Collection, Environment e relatório concluídos.

## Risks

- Impossibilidade de provar consistência transacional sem execução integrada.
- Fluxo público permite orquestração manual e não corresponde ao fluxo completo descrito no guia.
- Falhas posteriores à reserva não são simuláveis pelo PaymentFake atual.
- Ausência de health endpoint impede smoke conforme contrato do guia.

## Assumptions

- `POST /orders` atualmente apenas persiste `CREATED`; as etapas posteriores são endpoints separados.
- A ausência de Docker é bloqueio de ambiente, não falha funcional do produto.
- Erros do Inventory unitário no executor são de instrumentação Mockito/JDK, não falhas de asserção.

## Evidence Strategy

- Saídas Maven/Surefire.
- Inspeção de contratos, configuração, adapters e testes existentes.
- Collection e Environment exportáveis sem segredos.
- Matriz CT-001..CT-050 no `TEST_REPORT.md`.
