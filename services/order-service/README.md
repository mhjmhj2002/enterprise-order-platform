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
- Consumo Kafka, retry, DLQ e Outbox.
## Integracoes Sprint 1
- Inventory: REST sincrono via `INVENTORY_SERVICE_URL`.
- Payment: porta de aplicacao com `PaymentFakeAdapter`, sem provedor externo real nesta sprint.
- PaymentFake aprova por padrao. `PAYMENT_FAKE_FAIL=true` altera o processo inteiro; o header `X-Payment-Fake-Outcome: FAILED` simula falha somente na requisicao atual, sem restart. Em ambos os casos, a falha libera as reservas e cancela o pedido.
- Com o profile Spring `kafka`, a confirmacao concluida publica `OrderConfirmed` v1 no topico aprovado. Sem esse profile, nenhuma publicacao Kafka ocorre.
- Reserva de estoque valida o agregado antes de chamar Inventory. Se uma reserva parcial falhar, o adapter tenta liberar as reservas ja criadas antes de propagar erro `502`.
- `X-Correlation-Id` recebido pelo Order e devolvido na resposta e propagado nas chamadas REST ao Inventory.
## Endpoints principais
- `POST /api/v1/orders`
- `POST /api/v1/orders/{orderId}/reserve-stock`
- `POST /api/v1/orders/{orderId}/start-payment`
- `POST /api/v1/orders/{orderId}/mark-paid`
- `POST /api/v1/orders/{orderId}/confirm`
- `POST /api/v1/orders/{orderId}/cancel`
- `GET /api/v1/orders/{orderId}`
- `GET /api/v1/customers/{customerId}/orders`
- `GET /actuator/health`

Erros previsiveis de entrada, incluindo JSON malformado e UUID invalido, retornam `400` com resposta sanitizada. Falha configurada do PaymentFake retorna `503`.
## Execucao local
```bash
mvn spring-boot:run
```
Variaveis principais:
- `SECURITY_API_USERNAME` (obrigatoria)
- `SECURITY_API_PASSWORD` (obrigatoria)
- `ORDER_DB_URL` (default `jdbc:postgresql://localhost:5432/order`)
- `ORDER_DB_USERNAME` (default `order`)
- `ORDER_DB_PASSWORD` (default `order`)
- `SERVER_PORT` (default `8083`)
- `INVENTORY_SERVICE_URL` (default `http://localhost:8082`)
- `ORDER_DEFAULT_WAREHOUSE_ID` (default `00000000-0000-0000-0000-000000000001`)
- `PAYMENT_FAKE_FAIL` (default `false`)
- `KAFKA_BOOTSTRAP_SERVERS` (profile Spring `kafka`; default `localhost:9094`)

As rotas de negocio em `/api/v1/**` exigem HTTP Basic. O Order usa as mesmas credenciais configuradas localmente ao chamar o Inventory, sem repassar a credencial do consumidor. Nao versione nem exponha esses valores; HTTP Basic requer HTTPS confiavel fora do ambiente local. `GET /actuator/health`, OpenAPI e Swagger UI sao as unicas excecoes tecnicas publicas.

Para habilitar a publicacao localmente:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=kafka
```
