# Architecture Notes

## Objetivo

Este documento registra discussões arquiteturais em andamento, hipóteses, trade-offs, dúvidas e decisões adiadas.

Ele não substitui ADRs, não substitui o Context Map e não representa decisões definitivas.

---

## Status

In Progress

---

# Reunião

**Data:** 2026-07-07  
**Participantes:** TM Chatinho, LT Aprendiz  
**Objetivo da reunião:** Iniciar a identificação dos Bounded Contexts do domínio Mercado Aurora.

---

# Hipóteses levantadas

## Hipótese 001

**Título:** Catálogo Comercial é um Bounded Context.  
**Descrição:** Responsável por informações comerciais do produto, incluindo Produto, SKU, variantes, categorias, marcas e conteúdo comercial.  
**Justificativa:** Concentra o núcleo de dados e linguagem comercial de catálogo.  
**Status:** Em discussão.

---

## Hipótese 002

**Título:** Preço & Promoções representam um contexto distinto.  
**Descrição:** Contexto com dinâmica própria para precificação e aplicação de promoções.  
**Justificativa:** Preço possui alta frequência de alteração e promoções possuem regras de negócio próprias.  
**Status:** Em discussão.

---

## Hipótese 003

**Título:** Carrinho & Checkout representam um único contexto.  
**Descrição:** Existe hipótese de unificação em um único contexto de negócio.  
**Justificativa:** Checkout concentra a maior parte das regras; Carrinho pode não representar um Bounded Context independente.  
**Status:** Em discussão.

---

## Hipótese 004

**Título:** Estoque & Reserva representam um Core Domain.  
**Descrição:** Contexto focado em disponibilidade e reserva de itens.  
**Justificativa:** Foi identificado como uma das maiores dores do cliente e é essencial para evitar overselling e inconsistência de estoque.  
**Status:** Em discussão.

---

## Hipótese 005

**Título:** Pedido representa o principal Core Domain.  
**Descrição:** Contexto do compromisso comercial com o cliente.  
**Justificativa:** Centraliza o compromisso comercial da empresa.  
**Status:** Em discussão.

---

## Hipótese 006

**Título:** Pagamento & Conciliação representam Core Domain.  
**Descrição:** Contexto de captura, confirmação e conciliação financeira.  
**Justificativa:** Protegem receita, conformidade e consistência financeira.  
**Status:** Em discussão.

---

## Hipótese 007

**Título:** Fulfillment & Entrega iniciam como Supporting Domain.  
**Descrição:** Contexto de preparação e entrega de pedidos.  
**Justificativa:** Suporta execução operacional da venda na estratégia atual.  
**Status:** Em discussão.

---

## Hipótese 008

**Título:** Atendimento & Pós-venda iniciam como Supporting Domain.  
**Descrição:** Contexto de relacionamento após a compra, suporte e resolução de problemas.  
**Justificativa:** Atualmente suporta a operação e pode ganhar protagonismo conforme a estratégia de experiência do cliente evoluir.  
**Status:** Em discussão.

---

# Conteúdo da reunião

## Hipótese 001

Catálogo Comercial é um Bounded Context.

**Status:** Em discussão.

**Justificativa:** É responsável pelas informações comerciais do produto, incluindo Produto, SKU, variantes, categorias, marcas e conteúdo comercial.

**Observação:** Ainda não representa decisão sobre microsserviços.

---

## Hipótese 002

Preço & Promoções representam um contexto distinto.

**Status:** Em discussão.

**Justificativa:** Preço possui alta frequência de alteração. Promoções possuem regras de negócio próprias.

**Observação:** Existe possibilidade futura de separação entre Pricing e Promotions.

---

## Hipótese 003

Carrinho & Checkout representam um único contexto.

**Status:** Em discussão.

**Hipótese do LT:** Podem formar um único contexto de negócio.

**Hipótese do TM:** Carrinho pode não representar um Bounded Context independente. Checkout concentra a maior parte das regras de negócio.

**Decisão:** Adiar definição para construção do Context Map.

---

## Hipótese 004

Estoque & Reserva representam um Core Domain.

**Status:** Em discussão.

**Justificativa:** Foi identificado como uma das maiores dores do cliente. Responsável por evitar overselling e inconsistência de estoque.

---

## Hipótese 005

Pedido representa o principal Core Domain.

**Status:** Em discussão.

**Justificativa:** Centraliza o compromisso comercial da empresa.

---

## Hipótese 006

Pagamento & Conciliação representam Core Domain.

**Status:** Em discussão.

**Justificativa:** Protegem receita, conformidade e consistência financeira.

---

## Hipótese 007

Fulfillment & Entrega iniciam como Supporting Domain.

**Status:** Em discussão.

**Observação:** Poderão migrar para Core caso a estratégia da empresa passe a competir por excelência logística.

---

## Hipótese 008

Atendimento & Pós-venda iniciam como Supporting Domain.

**Status:** Em discussão.

**Observação:** Podem ganhar importância conforme a estratégia de experiência do cliente evoluir.

---

# Classificação preliminar

Registrar exatamente a classificação discutida.

## Core Domain

- Pedido
- Estoque & Reserva
- Pagamento & Conciliação

---

## Supporting Domain

- Catálogo Comercial
- Preço & Promoções
- Fulfillment & Entrega
- Atendimento & Pós-venda

---

## Em discussão

- Carrinho & Checkout

---

# Trade-offs discutidos

- Carrinho como contexto próprio versus agregado do Checkout.
- Preço e Promoções juntos versus separados.
- Fulfillment Supporting versus Core conforme evolução do negócio.

---

# Questões em aberto

- Quais limites exatos separam Carrinho e Checkout no fluxo de negócio?
- Preço e Promoções devem iniciar juntos ou separados no Context Map?
- Quais gatilhos de negócio justificariam promover Fulfillment para Core Domain?
- Qual fronteira entre Atendimento & Pós-venda e Pedido evita sobreposição de responsabilidade?

---

# Próximos passos

1. Revisar Domain Glossary.
2. Finalizar Gate Review da Story-002.
3. Construir Context Map.
4. Validar Bounded Contexts.
5. Definir Service Boundaries.

---

# Reunião

**Data:** 2026-07-08  
**Participantes:** TM Chatinho, LT Aprendiz  
**Objetivo da reunião:** Validar modelagem inicial do Catalog Service para Story-007.

## Decisões registradas

1. Product segue como Aggregate Root no contexto de Catalogo Comercial.
2. SKU segue como entidade dependente de Product (nao existe sem Product).
3. `sellerCode` deve ser unico no catalogo e validado em regra de aplicacao + constraint no banco.
4. `ean` e unico quando informado e validado em regra de aplicacao + constraint no banco.
5. Activacao de Product permanece protegida por invariante: ao menos um SKU ACTIVE.

## Trade-offs observados

- Constraint de unicidade no banco protege concorrencia; validacao em aplicacao melhora feedback de erro de negocio.
- Catalog Service nao absorve preco/estoque para preservar fronteira com contexts dedicados.

---

# Reunião

**Data:** 2026-07-08  
**Participantes:** TM Chatinho, LT Aprendiz  
**Objetivo da reunião:** Validar modelagem transacional do Inventory Service para Story-008.

## Decisões registradas

1. `InventoryItem` segue como aggregate root com identidade logica `skuId + warehouseId`.
2. `Reservation` segue como entidade interna do agregado, com transicoes controladas exclusivamente no dominio.
3. `availableQuantity` permanece derivado (`physical - reserved`) e nao sera persistido como fonte de verdade.
4. Ajuste fisico, reserva, commit e release sao operacoes de agregado; services apenas orquestram carregamento/salvamento.
5. O agregado passa a registrar Domain Events para facilitar plug futuro de Kafka/Saga sem reescrever o dominio.

## Trade-offs observados

- Persistir `reservedQuantity` com validacao por soma de reservas abertas melhora performance de leitura e protege consistencia.
- Manter eventos somente em memoria nesta sprint evita acoplamento prematuro com mensageria, preservando fronteiras hexagonais.
