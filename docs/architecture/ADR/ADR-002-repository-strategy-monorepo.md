# ADR-002: Estrategia de repositorio - Monorepo

## Status

Aceito

## Contexto

O projeto Enterprise Order Platform sera utilizado como laboratorio de estudos, portfolio profissional e simulacao de um ambiente corporativo de engenharia.

A plataforma sera composta por multiplos servicos, documentacao arquitetural, scripts, configuracoes de CI/CD e artefatos de apoio.

Como o projeto esta em fase inicial e sera conduzido por um time pequeno, e importante reduzir a complexidade operacional de versionamento e integracao.

## Decisao

Utilizaremos um unico repositorio no formato monorepo.

Os servicos ficarao organizados dentro do diretorio:

```text
services/
```

A documentacao arquitetural ficara centralizada em:

```text
docs/
```

## Consequencias positivas

- Facilita a navegacao no projeto.
- Centraliza documentacao e ADRs.
- Simplifica execucao local com Docker Compose.
- Simplifica configuracao inicial de CI/CD.
- Facilita abrir todos os servicos em uma unica IDE.
- Reduz overhead para o contexto de portfolio e estudo.

## Consequencias negativas

- O repositorio pode crescer bastante ao longo do tempo.
- Pipelines podem ficar mais complexos se nao houver separacao por servico.
- Controle de permissoes por servico fica menos granular.
- Em uma organizacao maior, pode exigir mais governanca.

## Alternativas consideradas

### Multi-repo

Cada servico teria seu proprio repositorio.

Rejeitado neste momento porque adicionaria complexidade operacional desnecessaria para a fase inicial do projeto.

A estrategia pode ser revisitada no futuro se o projeto crescer em numero de servicos, colaboradores ou pipelines independentes.

