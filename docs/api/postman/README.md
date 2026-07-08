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
