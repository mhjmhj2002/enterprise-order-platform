# Domain Glossary - Mercado Aurora

## Status

Baseline

## Objetivo

Estabelecer uma linguagem comum de negocio para Mercado Aurora, reduzindo ambiguidades entre Produto, Operacao e Engenharia antes da elaboracao do Context Map e de novas stories.

## Como usar este documento

Este documento define termos de negocio e seus significados oficiais para o projeto. Ele nao descreve implementacao, arquitetura ou escolhas de tecnologia.

## Termos do Dominio

### Produto

**Definicao:**  
Unidade comercial exibida ao cliente com nome, descricao, imagens, marca e especificacoes.

**Nao confundir com:**  
SKU. Produto e a representacao comercial; SKU e a variacao vendavel.

### SKU

**Definicao:**  
Variacao especifica de um Produto que pode ser vendida, reservada e faturada.

**Nao confundir com:**  
Produto. Um Produto pode ter varios SKUs.

### Variante

**Definicao:**  
Atributo que diferencia SKUs de um mesmo Produto, como cor, tamanho ou voltagem.

### Categoria

**Definicao:**  
Classificacao comercial usada para organizar e facilitar descoberta de Produtos.

### Marca

**Definicao:**  
Identidade comercial associada ao Produto percebida pelo cliente.

### Fabricante

**Definicao:**  
Empresa responsavel pela producao fisica do item.

**Nao confundir com:**  
Marca. Uma marca pode contratar fabricantes terceiros.

### Fornecedor

**Definicao:**  
Parceiro comercial que abastece o Mercado Aurora com itens para venda.

**Nao confundir com:**  
Fabricante. Nem todo fornecedor fabrica o item.

### EAN / GTIN

**Definicao:**  
Identificador comercial padrao usado para reconhecer itens no mercado.

### Codigo interno

**Definicao:**  
Identificador proprio do Mercado Aurora para controle interno de catalogo e operacao.

**Nao confundir com:**  
EAN / GTIN, que e um padrao externo.

### Imagem do produto

**Definicao:**  
Conjunto de imagens usadas para apresentar o Produto ao cliente.

### Especificacao tecnica

**Definicao:**  
Informacoes objetivas do item, como dimensoes, composicao e caracteristicas funcionais.

### Preco

**Definicao:**  
Valor comercial cobrado do cliente por um SKU em um dado momento.

### Promocao

**Definicao:**  
Regra comercial temporaria que altera condicao de compra, como desconto percentual ou fixo.

### Cupom

**Definicao:**  
Codigo aplicado no checkout para conceder beneficio comercial conforme regras vigentes.

### Cashback

**Definicao:**  
Beneficio financeiro futuro concedido ao cliente apos uma compra elegivel.

## Estoque

### Centro de Distribuicao (CD)

**Definicao:**  
Unidade operacional onde itens sao armazenados, separados e expedidos.

### Estoque fisico

**Definicao:**  
Quantidade real de itens presentes no CD.

### Estoque reservado

**Definicao:**  
Quantidade temporariamente bloqueada para pedidos em processo de compra.

### Estoque disponivel

**Definicao:**  
Quantidade vendavel no momento, resultante do estoque fisico menos o reservado.

### Reserva de estoque

**Definicao:**  
Ato de bloquear quantidade para evitar venda simultanea do mesmo item.

### Liberacao de estoque

**Definicao:**  
Remocao da reserva quando a compra nao e concluida ou quando a regra de expiracao se aplica.

### Baixa de estoque

**Definicao:**  
Reducao definitiva de quantidade apos confirmacao operacional da saida do item.

### Overselling

**Definicao:**  
Venda acima do estoque realmente disponivel, gerando ruptura e friccao com cliente.

### Divergencia de estoque

**Definicao:**  
Diferenca entre quantidade registrada em sistema e quantidade fisica observada.

### ERP

**Definicao:**  
Sistema corporativo que atua como referencia oficial do estoque fisico.

## Pedido

### Pedido

**Definicao:**  
Confirmacao formal da compra realizada pelo cliente.

### Item do pedido

**Definicao:**  
Linha individual do Pedido, representando um SKU com quantidade e condicoes comerciais.

### Checkout

**Definicao:**  
Etapa final da compra em que cliente confirma itens, endereco, frete e pagamento.

### Carrinho

**Definicao:**  
Agrupamento temporario de itens selecionados antes da confirmacao no checkout.

### Status do pedido

**Definicao:**  
Estado atual do ciclo do Pedido, usado para acompanhamento por cliente e operacao.

### Cancelamento

**Definicao:**  
Interrupcao total do Pedido ou de sua continuidade comercial.

### Reembolso

**Definicao:**  
Devolucao de valor ao cliente referente a um Pedido cancelado ou ajustado.

### Reembolso parcial

**Definicao:**  
Devolucao de parte do valor quando apenas alguns itens ou condicoes do Pedido sao revertidos.

## Pagamento

### Pagamento

**Definicao:**  
Etapa de cobranca do valor do Pedido no meio escolhido pelo cliente.

### Meio de pagamento

**Definicao:**  
Forma usada para pagar, como PIX, cartao de credito ou boleto.

### PIX

**Definicao:**  
Meio de pagamento instantaneo com confirmacao rapida.

### Cartao de credito

**Definicao:**  
Meio de pagamento sujeito a autorizacao e regras da emissora.

### Boleto

**Definicao:**  
Meio de pagamento com liquidacao condicionada ao pagamento ate o vencimento.

### Autorizacao

**Definicao:**  
Resultado inicial de validacao da transacao para permitir cobranca.

### Aprovacao

**Definicao:**  
Confirmacao de que o pagamento foi aceito para continuidade do Pedido.

**Nao confundir com:**  
Autorizacao. Pode existir autorizacao sem conclusao final do ciclo esperado.

### Antifraude

**Definicao:**  
Analise de risco para reduzir transacoes indevidas.

### Estorno

**Definicao:**  
Reversao financeira de uma cobranca previamente efetivada.

### Gateway de pagamento

**Definicao:**  
Parceiro responsavel por intermediar o processamento de transacoes de pagamento.

### Tentativa de pagamento

**Definicao:**  
Cada submissao feita para concluir o pagamento de um Pedido.

### Dupla cobranca

**Definicao:**  
Cenario indevido em que o cliente e cobrado mais de uma vez pelo mesmo Pedido.

## Fulfillment / Entrega

### Fulfillment

**Definicao:**  
Conjunto de atividades de separacao, expedicao e entrega apos confirmacao do Pedido.

### Frete

**Definicao:**  
Custo e condicao comercial de transporte para entregar os itens ao cliente.

### Cotacao de frete

**Definicao:**  
Calculo de opcoes e valores de entrega para uma compra especifica.

### Transportadora

**Definicao:**  
Parceiro logistico responsavel por transportar remessas.

### Remessa

**Definicao:**  
Entrega fisica vinculada ao Pedido total ou a parte dele.

**Nao confundir com:**  
Pedido. Um Pedido pode gerar multiplas remessas.

### Rastreamento

**Definicao:**  
Acompanhamento da movimentacao e do status de uma Remessa.

### Expedicao

**Definicao:**  
Etapa operacional em que itens separados sao preparados e despachados.

### SLA de entrega

**Definicao:**  
Compromisso de prazo de entrega assumido com o cliente.

### Prazo prometido

**Definicao:**  
Data ou janela informada ao cliente como expectativa de recebimento.

**Nao confundir com:**  
SLA de entrega. O SLA define regra de compromisso; o prazo prometido e a concretizacao para um caso.

## Atendimento / Operacao

### Atendimento

**Definicao:**  
Funcao de suporte ao cliente durante duvidas, excecoes e pos-venda.

### Timeline do pedido

**Definicao:**  
Visao cronologica dos principais eventos do Pedido para consulta operacional.

### Chamado

**Definicao:**  
Registro formal de uma demanda de cliente ou operacao para acompanhamento.

### Reprocessamento

**Definicao:**  
Nova execucao de uma acao para corrigir falha ou inconsistencias.

### Acao manual

**Definicao:**  
Intervencao operacional realizada por pessoa quando o fluxo padrao nao resolve o caso.

### Permissao operacional

**Definicao:**  
Nivel de autorizacao de cada perfil para executar acoes sensiveis na operacao.

## Conceitos futuros conhecidos

### Marketplace

**Definicao:**  
Modelo de negocio em que terceiros vendem usando a plataforma.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Seller

**Definicao:**  
Lojista terceiro que oferta itens em um modelo de marketplace.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Clube de beneficios

**Definicao:**  
Programa recorrente de vantagens para clientes elegiveis.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Carteira digital

**Definicao:**  
Meio digital para armazenar saldo ou instrumentos de pagamento.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Multiplos gateways

**Definicao:**  
Uso de mais de um parceiro de pagamento para reduzir risco de indisponibilidade e ampliar cobertura.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Loja fisica

**Definicao:**  
Canal presencial de venda e atendimento fora do ambiente digital.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.

### Canal de venda

**Definicao:**  
Meio pelo qual a venda acontece, como e-commerce proprio, app ou loja fisica.

**Observacoes:**  
Questao em aberto para evolucao futura do negocio.
