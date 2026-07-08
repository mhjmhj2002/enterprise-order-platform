# Catalog Service

Microsservico responsavel pelo catalogo comercial do Mercado Aurora.
Implementa Product como aggregate root e Sku como entidade interna do agregado.

## O que este servico faz

Gerencia produtos e SKUs do catalogo, incluindo criacao, atualizacao, ativacao/desativacao e consulta de produtos ativos.

## Responsabilidades

- Manter dados comerciais de Product e Sku.
- Garantir invariantes de dominio:
  - SKU nao existe sem Product.
  - Product ACTIVE exige ao menos um SKU ACTIVE.
  - `sellerCode` obrigatorio e unico no catalogo.
  - `ean` unico quando informado.
- Expor API REST versionada em `/api/v1`.
- Persistir dados em PostgreSQL com migracoes Flyway.

## O que NAO e responsabilidade deste servico

- Preco e promocoes.
- Estoque e reserva.
- Pedido e checkout.
- Pagamento.
- Autenticacao e autorizacao.

## Como rodar localmente

```bash
cd services/catalog-service
mvn spring-boot:run
```

Variaveis opcionais:

- `CATALOG_DB_URL` (default `jdbc:postgresql://localhost:5432/catalog`)
- `CATALOG_DB_USERNAME` (default `catalog`)
- `CATALOG_DB_PASSWORD` (default `catalog`)
- `SERVER_PORT` (default `8081`)

## Como testar

```bash
cd services/catalog-service
mvn test
```

> Os testes de integracao usam Testcontainers e sao automaticamente ignorados quando Docker nao estiver disponivel.

## Endpoints principais

- `POST /api/v1/products`
- `PUT /api/v1/products/{productId}`
- `PATCH /api/v1/products/{productId}/status`
- `GET /api/v1/products`
- `GET /api/v1/products/{productId}`
- `GET /api/v1/products/{productId}/skus`
- `POST /api/v1/products/{productId}/skus`
- `PUT /api/v1/products/{productId}/skus/{skuId}`
- `GET /api/v1/skus/{skuId}`

Swagger UI:

- `http://localhost:8081/swagger-ui/index.html`

## Dependencias

- PostgreSQL 16+
- Flyway
- Spring Boot 3.3.x
- Testcontainers (testes de integracao)
