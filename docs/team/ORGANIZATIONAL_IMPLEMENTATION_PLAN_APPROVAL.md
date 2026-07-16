# Organizational Implementation Plan Approval

**Parecer do Engineering Manager:** CHANGES REQUIRED

**Data:** 2026-07-16

**Plano avaliado:** [Organizational Implementation Plan](ORGANIZATIONAL_IMPLEMENTATION_PLAN.md)

**Referência publicada do AEO:** `main` / `cbfee2be938a6c70f03429e5ecb05240c6394973`

## Resultado

O plano cobre PI-004, PI-005, o Sprint Execution Protocol, o Organizational
Freeze, os templates, a validação antes da Sprint 5 e a sequência de implantação
necessária. Entretanto, duas instruções devem ser corrigidas antes de qualquer
implementação, pois entram em conflito com as Organizational Decisions da
[Sprint 4 Engineering Retrospective](SPRINT_4_ENGINEERING_RETROSPECTIVE.md) e
com o [Engineering Workflow](ENGINEERING_WORKFLOW.md).

## Alterações obrigatórias

| Item do plano | Inconsistência | Ajuste requerido |
| --- | --- | --- |
| Seção 3 — Institutional Handoff Package | O plano permite um pacote Markdown autônomo quando não existir artefato de handoff. A decisão aprovada exige que o pacote seja uma seção padronizada do artefato, PR ou registro institucional já existente, sem documento paralelo. | Remover a exceção de pacote autônomo. Para toda transição, identificar o registro institucional existente que receberá a seção. O `HANDOFFS/README.md`, se mantido, deve ser apenas índice navegacional derivado de referências publicadas, nunca uma fonte de verdade ou pacote adicional. |
| Seção 12, ordem 10 — publicação em `main` | O plano atribui ao Repository Owner a publicação da implementação organizacional. Isso pode fazê-lo publicar artefatos não produzidos por ele, contrariando a regra de que cada role publica seus próprios artefatos em um Versioned Handoff. | Definir que AEO, Technical Writer e qualquer outra role publicam seus próprios artefatos e referências. O Repository Owner executa somente as operações administrativas autorizadas — como merge ou release — depois da validação do EM; não é autor ou publicador substituto da implementação organizacional. |

## Comentários de consistência

- A confirmação da situação e do índice do papel PMO antes da criação da
  workspace da Sprint 5 é adequada e deve permanecer como primeira dependência.
- O `STATUS.md` pode ser um artefato vivo e excluído do Freeze, desde que cada
  atualização seja publicada junto ao handoff que ela representa.
- O índice de handoffs pode ser mantido pelo AEO como auxílio de navegação; os
  artefatos e pacotes publicados pelas roles continuam sendo a única fonte de
  verdade.

## Decisão e handoff

Nenhuma implementação organizacional está autorizada enquanto o plano não for
revisado e republicado com os dois ajustes obrigatórios. Após a revisão, o AEO
deve realizar novo Versioned Handoff do plano para uma reavaliação do Engineering
Manager.

## Revalidação — 2026-07-16

**Plano reavaliado:** `main` / `c9d95e0284bd0e339fd6766561e56817f3a1abd9`

O primeiro ajuste obrigatório foi atendido: o plano removeu o diretório e índice
`HANDOFFS/`, eliminou a exceção de pacote autônomo e exige que todo pacote seja
uma seção do artefato, PR ou registro institucional existente.

O segundo ajuste foi atendido nas regras gerais de ownership, mas permanece uma
instrução contraditória na seção 12, ordem 10: ela ainda atribui ao Repository
Owner a publicação da implementação organizacional em `main`. Essa formulação
deve ser substituída por uma sequência que preserve a publicação dos artefatos
por AEO, Technical Writer e demais roles produtoras; o Repository Owner poderá
somente executar a operação administrativa autorizada após a validação do EM.

**Resultado da revalidação: CHANGES REQUIRED.** Até essa correção textual, o
plano não está autorizado para implementação.
