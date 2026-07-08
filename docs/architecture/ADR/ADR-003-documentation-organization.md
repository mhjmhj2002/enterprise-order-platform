# ADR-003: Organizacao da documentacao do projeto

## Status

Aceito

## Contexto

O projeto comecou a crescer em quantidade de documentos.

Era necessario separar claramente documentacao de arquitetura, documentos do time, roadmap e ADRs para evitar perda de organizacao.

## Decisao

Adotar a seguinte estrutura:

- docs/architecture
- docs/team
- docs/roadmap
- docs/adr
- docs/diagrams
- docs/decisions

O Engineering Guide passa a ser o Engineering Handbook localizado em:

`docs/architecture/ENGINEERING_HANDBOOK.md`

## Consequencias

Positivas:

- documentacao escalavel;
- navegacao mais simples;
- organizacao semelhante a encontrada em projetos enterprise.

Negativas:

- atualizacao de links internos;
- necessidade de mover documentos existentes.

Alternativa considerada:

Manter todos os documentos na raiz.

Rejeitada por comprometer organizacao de longo prazo.
