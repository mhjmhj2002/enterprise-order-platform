# Postman Collections

Colecoes para testes manuais das APIs da plataforma.

## Catalog Service

Arquivo: `catalog-service.postman_collection.json`

### Como usar

1. Importe a collection no Postman.
2. Garanta o `baseUrl` apontando para o serviço local (default: `http://localhost:8081`).
3. Execute na ordem:
   - `Create Product`
   - `Add Sku`
   - `Change Product Status`
   - Demais consultas e updates

### Variaveis

- `baseUrl`: URL base da API.
- `productId`: preenchida automaticamente no teste de criacao de produto.
- `skuId`: preenchida automaticamente no teste de criacao de SKU.

## Inventory Service

Arquivo: `inventory-service.postman_collection.json`

### Variaveis

- `baseUrl`: URL base da API.
- `skuId`, `warehouseId`, `reservationId`: identificadores para fluxo de reserva/commit/release.

## Order Service

Arquivo: `order-service.postman_collection.json`

Environment sanitizado da Story-009: `story-009-local.postman_environment.json`

### Como usar

1. Importe a collection e o environment no Postman.
2. Inicie Inventory em `8082` e Order em `8083` ou ajuste as URLs.
3. Execute as pastas numeradas de `00 - Health Checks` a `12 - Cleanup`.

A collection cria pedidos independentes para happy path, falha de pagamento e validacao de estados. O cenario de falha usa `X-Payment-Fake-Outcome: FAILED` somente no request correspondente, portanto a bateria completa pode ser executada sem reiniciar o Order Service.

### Variaveis

- `orderBaseUrl`, `inventoryBaseUrl`, `catalogBaseUrl`: URLs dos servicos.
- `customerId`, `skuId`, `skuId2`: massa gerada automaticamente.
- `orderId`: preenchido automaticamente na criacao do pedido.
- `reservationId`, `reservationId2`: referencias geradas automaticamente.
- `warehouseId`, `quantity`, `correlationId`: contexto da execucao.
