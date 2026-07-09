# ADR-006: Order como Aggregate Root no Order Service

## Status

Aceito

## Contexto

A Story-009 exige o bootstrap do Order Service como contexto de compromisso comercial do pedido.

Era necessario formalizar a fronteira transacional entre `Order` e `OrderItem`, protegendo:

- Pedido deve possuir ao menos um item.
- Quantidade de item deve ser maior que zero.
- Total do pedido deve ser consistente com o somatorio dos itens.
- Nao iniciar pagamento sem estoque reservado.
- Nao confirmar pedido sem pagamento aprovado.
- Pedido cancelado nao muda de estado.
- Pedido confirmado nao pode ser cancelado nesta sprint.

## Decisao

Adotar **Order** como aggregate root no Order Service.

- `OrderItem` torna-se entidade interna do agregado.
- O ciclo de estados (`CREATED`, `STOCK_RESERVED`, `PAYMENT_PENDING`, `PAID`, `CONFIRMED`, `CANCELLED`) fica encapsulado no dominio.
- Operacoes de reserva, pagamento, confirmacao e cancelamento ocorrem exclusivamente via agregado.
- Snapshots comerciais dos itens ficam no agregado para preservar o contexto da compra.
- O agregado registra Domain Events para futura publicacao assincrona, sem acoplar a Sprint 1 a Kafka.

## Consequencias

Positivas:

- Invariantes de pedido centralizadas e protegidas em um unico ponto.
- Menor risco de regras de negocio vazarem para API ou camada de aplicacao.
- Base pronta para evolucao orientada a eventos sem reescrever o nucleo de dominio.

Negativas:

- Maior responsabilidade do agregado sobre fluxo de checkout da sprint.
- Dependencia de disciplina para manter servicos de aplicacao apenas como orquestradores.

## Alternativas consideradas

### Modelar `OrderItem` como aggregate root separado

Rejeitada porque fragmentaria as invariantes de totais e de transicoes do pedido.

### Permitir transicoes de estado via services/controllers

Rejeitada porque violaria principios de DDD e aumentaria risco de regressao por regra duplicada.

