# Business Discovery - Mercado Aurora

## Status

Consolidado

## 1. Objetivo do Discovery

Consolidar o entendimento do negocio do Mercado Aurora para orientar as proximas decisoes de produto e planejamento de entrega.

Este documento registra fatos do dominio, dores atuais, necessidades de evolucao e pontos ainda abertos.

## 2. Contexto da empresa

Mercado Aurora opera um e-commerce de produtos fisicos com foco principal em B2C.

Existe operacao B2B de baixo volume (menos de 5%), que nao deve ser bloqueada por regras futuras.

Cenario atual informado pelo cliente:

- aproximadamente 120 mil SKUs ativos;
- cerca de 4 mil novos SKUs por mes;
- media atual de aproximadamente 5 mil pedidos por dia;
- objetivo de crescimento para aproximadamente 150 mil pedidos por dia.

## 3. Modelo de negocio

- venda direta ao consumidor final;
- operacao propria (nao marketplace);
- estoque proprio;
- compromisso com experiencia de compra ponta a ponta, do catalogo a entrega.

## 4. Objetivos estrategicos

- sustentar crescimento de volume sem degradar experiencia do cliente;
- reduzir falhas recorrentes no ciclo de pedido;
- melhorar confiabilidade de estoque e pagamento;
- reduzir tempo de atendimento ao cliente;
- aumentar rastreabilidade do pedido durante todo o fluxo.

## 5. Escopo atual

### Catalogo e ofertas

- produto e SKU sao conceitos diferentes;
- SKU e a unidade transacionavel;
- produto concentra informacoes comerciais;
- preco, disponibilidade e promocao mudam com alta frequencia;
- descricao e especificacao mudam com baixa frequencia;
- existe alta variacao de alteracoes de preco por dia, com picos sazonais.

Promocoes vigentes:

- desconto percentual;
- desconto em valor fixo;
- cupom.

### Estoque

- operacao com 3 centros de distribuicao;
- um mesmo SKU pode existir em mais de um centro;
- controle por estoque fisico, reservado e disponivel;
- reserva ocorre no checkout;
- ERP e o sistema mestre do estoque fisico.

### Pagamento

Meios atuais:

- PIX;
- cartao;
- boleto.

### Fulfillment e entrega

- operacao com multiplas transportadoras;
- frete calculado em tempo real;
- um pedido pode gerar multiplas remessas;
- prazo de entrega e compromisso de negocio.

### Atendimento

- operadores consultam diferentes sistemas para entender o estado do pedido;
- nao existe visao unica consolidada para atendimento.

## 6. Escopo futuro conhecido

Evolucoes de negocio ja mapeadas pelo cliente:

- promocoes progressivas;
- leve X pague Y;
- descontos por categoria;
- cashback;
- operacao com multiplos gateways de pagamento;
- maior uso de carteiras digitais;
- timeline unica do pedido para atendimento.

## 7. Stakeholders

- cliente final (comprador B2C);
- cliente corporativo de baixo volume (B2B);
- atendimento ao cliente;
- operacao de estoque e expedicao;
- area comercial e promocional;
- financeiro e conciliacao;
- lideranca de produto e tecnologia.

## 8. Personas principais

### Comprador B2C

Espera disponibilidade correta, preco coerente, pagamento confiavel e prazo de entrega cumprido.

### Comprador B2B de baixo volume

Precisa concluir pedidos sem friccao, mesmo nao sendo o foco principal da operacao.

### Operador de atendimento

Precisa entender rapidamente o historico do pedido para responder com seguranca e reduzir retrabalho.

### Operador logistico

Precisa de visibilidade clara sobre reserva, separacao e remessa para reduzir excecoes na entrega.

## 9. Fluxo macro da venda

1. cliente busca e seleciona itens no catalogo;
2. cliente monta carrinho e inicia checkout;
3. sistema valida disponibilidade e realiza reserva no checkout;
4. cliente escolhe forma de pagamento e confirma compra;
5. pagamento retorna aprovacao, pendencia ou falha;
6. pedido confirmado segue para separacao e expedicao;
7. pedido pode ser dividido em mais de uma remessa;
8. cliente acompanha entrega e, quando necessario, aciona atendimento.

## 10. Regras de negocio identificadas

- SKU e a unidade de venda;
- preco e promocao sao componentes centrais da conversao;
- reserva de estoque ocorre no checkout;
- estoque disponivel depende de fisico menos reservado;
- ERP e referencia para estoque fisico;
- um pedido pode ter multiplas remessas;
- prazo prometido de entrega e compromisso de negocio;
- atendimento precisa rastrear o ciclo completo do pedido.

## 11. Requisitos funcionais

- permitir consulta e compra por SKU com dados comerciais corretos;
- aplicar promocoes vigentes de forma consistente;
- reservar estoque durante checkout;
- processar pagamento por PIX, cartao e boleto;
- tratar excecoes de pagamento sem perder rastreabilidade;
- suportar multiplas remessas por pedido;
- disponibilizar acompanhamento de entrega;
- oferecer ao atendimento visao consolidada do historico do pedido.

## 12. Requisitos nao funcionais

- suportar crescimento de volume para a meta de 150 mil pedidos/dia;
- manter consistencia entre estados de pedido, pagamento e estoque;
- garantir rastreabilidade ponta a ponta para operacao e atendimento;
- reduzir tempo medio de resolucao no atendimento;
- sustentar picos sazonais de alteracao de preco e alta demanda.

## 13. Restricoes de negocio

- operacao e de estoque proprio, nao marketplace;
- existe dependencia do ERP como mestre de estoque fisico;
- operacao com tres centros de distribuicao ativos;
- compromisso com multiplas transportadoras e SLA de entrega;
- operacao B2B existe e nao pode ser inviabilizada.

## 14. Dores atuais

- overselling em cenarios de alta demanda;
- reserva nao liberada em alguns fluxos;
- divergencia entre estoque registrado e estoque fisico;
- timeout com pagamento efetivado;
- dupla cobranca em casos de repeticao de tentativa;
- dificuldade para tratar multiplos cliques em pagar;
- indisponibilidade de gateway impactando conversao;
- atendimento lento por ausencia de visao unica;
- baixa rastreabilidade do pedido apos confirmacao.

## 15. Oportunidades futuras

- aumentar conversao com promocoes mais sofisticadas;
- melhorar fidelizacao com cashback;
- reduzir perda de venda com maior resiliencia na etapa de pagamento;
- reduzir custo operacional com visao unica para atendimento;
- elevar confianca do cliente com melhor previsibilidade de entrega.

## 16. Questoes ainda em aberto

- qual politica detalhada para liberacao automatica de reserva expirada;
- quais limites de tempo aceitaveis por etapa do checkout;
- qual regra oficial para reconciliacao de casos de timeout com cobranca;
- como priorizar divergencias entre sistemas operacionais e realidade fisica;
- quais regras de reembolso parcial por item em pedidos com multiplas remessas.

## 17. Riscos identificados

- risco de perda de venda por indisponibilidade aparente incorreta;
- risco financeiro por cobranca duplicada;
- risco reputacional por atraso de entrega e baixa transparencia;
- risco operacional por retrabalho no atendimento;
- risco de escalabilidade caso dores atuais persistam com aumento de volume.

## 18. Glossario preliminar

- Produto: entidade comercial exibida ao cliente.
- SKU: unidade transacionavel de venda.
- EAN: identificador comercial do item.
- CD: centro de distribuicao.
- Checkout: etapa de fechamento da compra.
- Reserva de estoque: bloqueio temporario de disponibilidade para concluir compra.
- SLA: compromisso de prazo acordado com cliente.
- Fulfillment: processo de separacao, expedicao e entrega.
- Gateway: parceiro que processa transacoes de pagamento.
- Cashback: beneficio financeiro futuro vinculado a compra.
