# Order Service
Servico responsavel pelo compromisso comercial do pedido no Mercado Aurora.
## Responsabilidades
- Criar pedido com snapshot comercial dos itens.
- Registrar reservas de estoque associadas ao pedido.
- Controlar fluxo de pagamento (`NOT_STARTED -> PENDING -> PAID`).
- Confirmar ou cancelar pedido respeitando invariantes de dominio.
- Expor API REST em `/api/v1/orders` e `/api/v1/customers/{customerId}/orders`.
## Fora da fronteira
- Catalogo de produto/SKU.
- Controle de estoque fisico e reservas de inventario.
- Captura financeira real e conciliacao.
- Mensageria/Kafka nesta sprint.
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
