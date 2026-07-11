# Convenções de Eventos e Tópicos

**Status:** Baseline de planejamento da Sprint 2

Estas convenções servem como referência institucional para o contrato inicial. A Engenharia definirá detalhes de implementação compatíveis com elas.

## Nomes de eventos

- Usar PascalCase.
- Usar tempo passado para descrever fato de domínio imutável.
- Preferir semântica de negócio, não instrução técnica.

Exemplo aprovado: `OrderConfirmed`.

Não usar nomes de comando ou de etapa técnica, como `ProcessOrderConfirmation`.

## Nomes de tópicos

Formato:

```text
<organizacao>.<dominio>.<fato-em-kebab-case>.v<versao-maior>
```

Exemplo aprovado:

```text
mercadoaurora.order.order-confirmed.v1
```

Os segmentos usam letras minúsculas e ponto como separador; o fato usa kebab-case.

## Versionamento

- A versão maior faz parte do tópico e do contrato do evento.
- Mudança compatível permanece na mesma versão maior.
- Mudança incompatível exige nova versão maior e plano explícito de coexistência.
- O catálogo inicial define somente `v1`.

## Ownership

- O producer é owner da semântica e evolução do evento publicado.
- Cada consumer é owner do próprio resultado de processamento.
- Consumers não devem criar regras de negócio novas nem depender de detalhes que não pertençam ao contrato publicado.

Para o evento inicial: Order é producer/owner de `OrderConfirmed`; Inventory é o consumer inicial planejado.

## Rastreabilidade e duplicidade

- Todo evento planejado deve conter identificador de evento, identificador de correlação e data/hora de ocorrência.
- O resultado de consumo deve ser rastreável ao pedido e ao evento de origem.
- A repetição do mesmo evento não pode produzir efeito de negócio duplicado.

Os mecanismos técnicos para garantir esses requisitos serão refinados na implementação; esta convenção não promete processamento exatamente uma vez.
