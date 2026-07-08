# ADR-004: Product como Aggregate Root no Catalog Service

## Status

Aceito

## Contexto

A Story-007 exige bootstrap do Catalog Service como referencia de DDD e arquitetura hexagonal para os proximos microsservicos.

Era necessario definir explicitamente a fronteira transacional entre Product e Sku, alem de formalizar regras de consistencia:

- SKU nao existe sem Product.
- Product ACTIVE exige ao menos um SKU ACTIVE.
- `sellerCode` obrigatorio e unico no catalogo.
- `ean` unico quando informado.

## Decisao

Adotar **Product** como aggregate root no Catalog Service.

- `Sku` torna-se entidade interna do agregado Product.
- `Sku` no dominio nao carrega `productId`; essa relacao fica como detalhe de persistencia/infraestrutura.
- Alteracoes de SKU ocorrem via comandos que carregam e persistem o Product.
- Unicidade global de `sellerCode` e `ean` e garantida por validacao de aplicacao e constraint no PostgreSQL.
- O contexto de Catalogo permanece restrito a informacao comercial; preco, estoque, pedido e pagamento ficam fora da fronteira.

## Consequencias

Positivas:

- Consistencia de regras do catalogo centralizada no agregado.
- Maior clareza de ownership do dominio comercial.
- Base arquitetural reutilizavel para os proximos servicos.

Negativas:

- Operacoes de SKU dependem do ciclo de vida do Product.
- Necessidade de checagens adicionais de unicidade para evitar conflitos concorrentes.

## Alternativas consideradas

### SKU como aggregate root independente

Rejeitada porque quebraria a regra de dependencia obrigatoria entre SKU e Product e aumentaria o risco de inconsistencias de ciclo de vida.

### Validar unicidade apenas na aplicacao

Rejeitada porque nao protege concorrencia entre requests simultaneas sem apoio de constraint no banco.
