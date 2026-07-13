# Inventory Service

Microsservico responsavel por estoque transacional do Mercado Aurora.
Implementa `InventoryItem` como aggregate root (identidade logica `skuId + warehouseId`) e `Reservation` como entidade interna com maquina de estados encapsulada no dominio.

## O que este servico faz

Gerencia saldo fisico, reservas e baixa de estoque por SKU e warehouse, protegendo invariantes de disponibilidade.

## Responsabilidades

- Manter dados de estoque por `skuId` e `warehouseId`.
- Garantir invariantes de dominio:
  - `physicalQuantity >= 0`
  - `reservedQuantity >= 0`
  - `reservedQuantity <= physicalQuantity`
  - `availableQuantity = physicalQuantity - reservedQuantity` (sempre derivado)
  - reserva apenas quando `quantity <= availableQuantity`
- Encapsular transicoes da reserva:
  - `CREATED -> COMMITTED`
  - `CREATED -> RELEASED`
  - `CREATED -> EXPIRED`
  - estados finais nao mudam mais.
- Expor API REST versionada em `/api/v1/inventory`.
- Persistir dados em PostgreSQL com migracoes Flyway.

## O que NAO e responsabilidade deste servico

- Catalogo comercial.
- Preco e promocoes.
- Pedido e checkout.
- Pagamento.
- Orquestracao Saga e publicacao Kafka (dominio ja preparado com Domain Events).

## Como rodar localmente

```bash
cd services/inventory-service
mvn spring-boot:run
```

Variaveis opcionais:

- `INVENTORY_DB_URL` (default `jdbc:postgresql://localhost:5432/inventory`)
- `INVENTORY_DB_USERNAME` (default `inventory`)
- `INVENTORY_DB_PASSWORD` (default `inventory`)
- `SERVER_PORT` (default `8082`)
- `KAFKA_BOOTSTRAP_SERVERS` (profile Spring e Maven `kafka`; default `localhost:9094`)
- `KAFKA_CONSUMER_GROUP_ID` (profile `kafka`; default `mercadoaurora.inventory.v1`)

O profile `kafka` prepara somente configuracao e health check nesta Story. Nenhum consumer e iniciado.

## Como testar

```bash
cd services/inventory-service
mvn test
```

> Os testes de integracao usam Testcontainers e sao automaticamente ignorados quando Docker nao estiver disponivel.

## Endpoints principais

- `POST /api/v1/inventory`
- `PATCH /api/v1/inventory/{skuId}/{warehouseId}/adjust`
- `POST /api/v1/inventory/{skuId}/{warehouseId}/reservations`
- `POST /api/v1/inventory/{skuId}/{warehouseId}/reservations/{reservationId}/commit`
- `POST /api/v1/inventory/{skuId}/{warehouseId}/reservations/{reservationId}/release`
- `GET /api/v1/inventory/{skuId}`
- `GET /api/v1/inventory/{skuId}/{warehouseId}`

Swagger UI:

- `http://localhost:8082/swagger-ui/index.html`
