# Order Service
Servico responsavel pelo compromisso comercial do pedido no Mercado Aurora.
## Responsabilidades
- Criar pedido com snapshot comercial dos itens.
- Registrar reservas de estoque associadas ao pedido.
- Controlar fluxo de pagamento (`NOT_STARTED -> PENDING -> PAID`).
- Confirmar ou cancelar pedido respeitando invariantes de dominio.
- Orquestrar chamadas REST sincronas para Inventory nas etapas de reserva, commit e liberacao.
- Expor API REST em `/api/v1/orders` e `/api/v1/customers/{customerId}/orders`.
## Fora da fronteira
- Catalogo de produto/SKU.
- Controle de estoque fisico e reservas de inventario.
- Captura financeira real e conciliacao.
- Mensageria/Kafka nesta sprint.
## Integracoes Sprint 1
- Inventory: REST sincrono via `INVENTORY_SERVICE_URL`.
- Payment: porta de aplicacao com `PaymentFakeAdapter`, sem provedor externo real nesta sprint.
- Domain Events ficam registrados no dominio para evolucao futura, mas nao sao publicados.
## Endpoints principais
- `POST /api/v1/orders`
- `POST /api/v1/orders/{orderId}/reserve-stock`
- `POST /api/v1/orders/{orderId}/start-payment`
- `POST /api/v1/orders/{orderId}/mark-paid`
- `POST /api/v1/orders/{orderId}/confirm`
- `POST /api/v1/orders/{orderId}/cancel`
- `GET /api/v1/orders/{orderId}`
- `GET /api/v1/customers/{customerId}/orders`
## Execucao local
```bash
mvn spring-boot:run
```
Variaveis principais:
- `ORDER_DB_URL` (default `jdbc:postgresql://localhost:5432/order`)
- `ORDER_DB_USERNAME` (default `order`)
- `ORDER_DB_PASSWORD` (default `order`)
- `SERVER_PORT` (default `8083`)
- `INVENTORY_SERVICE_URL` (default `http://localhost:8082`)
- `ORDER_DEFAULT_WAREHOUSE_ID` (default `00000000-0000-0000-0000-000000000001`)
