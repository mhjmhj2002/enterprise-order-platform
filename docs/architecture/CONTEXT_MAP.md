# Context Map

## Status

Baseline (Sprint 1)

## Bounded Contexts mapeados

| Contexto | Tipo (DDD) | Responsabilidade principal |
|---|---|---|
| Catalogo Comercial | Supporting | Produto, SKU, categoria, marca e conteudo comercial |
| Preco & Promocoes | Supporting | Politica de precificacao e campanhas promocionais |
| Estoque & Reserva | Core | Disponibilidade, reserva e consistencia de estoque |
| Pedido | Core | Compromisso comercial do pedido |
| Pagamento & Conciliacao | Core | Captura e conciliacao financeira |
| Fulfillment & Entrega | Supporting | Separacao, expedicao e entrega |
| Atendimento & Pos-venda | Supporting | Suporte e tratamento de incidentes pos-compra |

## Relacoes atuais (Sprint 1)

1. Catalogo Comercial publica dados de produto/SKU para consulta dos demais contextos.
2. Estoque & Reserva referencia SKU para disponibilidade.
3. Pedido referencia dados de catalogo e disponibilidade sem copiar regras do Catalogo.
4. Pagamento & Conciliacao nao depende de regras internas do Catalogo.

## Decisoes refletidas no bootstrap do Catalog Service

- Product definido como aggregate root.
- SKU modelado como entidade subordinada ao Product.
- Catalogo nao conhece preco, estoque, pedido e pagamento.
