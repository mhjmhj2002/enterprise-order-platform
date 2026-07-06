# Enterprise Portfolio Project Charter

> Versao: 1.0.0  
> Status: Ativo

## Missao

Construir um projeto de nivel Enterprise que sirva simultaneamente como laboratorio de estudos, portfolio profissional, preparacao para entrevistas de Java Senior / Tech Lead e ambiente para praticar arquitetura e lideranca tecnica.

## Visao

Ao final do projeto, o LT Aprendiz devera ser capaz de discutir arquitetura distribuida, defender decisoes tecnicas, justificar trade-offs, conduzir revisoes tecnicas e apresentar um portfolio solido em entrevistas.

## Papeis

### TM Chatinho

Tech Manager responsavel por arquitetura, padroes, qualidade, mentoring, design review, code review e aprovacao das releases.

Nao implementa codigo.

### LT Aprendiz

Responsavel por definir direcao tecnica, entender os conceitos, aprovar implementacoes, conduzir discussoes e aprender a pensar como lider tecnico.

Seu foco principal e aprender e liderar tecnicamente, nao apenas programar.

### Dev Mao na Massa

Responsavel por implementar historias, escrever testes, documentar, defender tecnicamente o codigo, sugerir melhorias e discordar quando necessario.

Nunca e apenas um executor passivo.

## Fluxo Oficial

Backlog -> Planning -> Discussao Tecnica -> Implementacao -> Pull Request -> Code Review -> Ajustes -> Merge -> Release -> LinkedIn / Curriculo

## Valores do Time

1. Qualidade acima de velocidade.
2. Simplicidade acima de complexidade desnecessaria.
3. Todo codigo deve ser explicavel.
4. Toda decisao deve possuir justificativa.
5. Discordancias tecnicas sao bem-vindas quando fundamentadas.
6. Aprender vale mais do que terminar rapidamente.

## Uso de IA

A IA pode ser utilizada como tutor, pair programming, revisora e geradora de ideias.

Nunca como substituta da compreensao.

Todo codigo entregue devera ser entendido pelo LT Aprendiz.

## Git Flow

- `main`: somente releases aprovadas.
- `feature/*`: desenvolvimento.

Cada Sprint gera uma release.

## Commits

Utilizar preferencialmente Conventional Commits:

- `feat:`
- `fix:`
- `refactor:`
- `docs:`
- `test:`
- `chore:`

## Pull Request Checklist

Antes de solicitar revisao:

- Codigo funcionando.
- Testes passando.
- Documentacao atualizada.
- README atualizado.
- CHANGELOG atualizado.
- Decisao tecnica conhecida pelo LT Aprendiz.

## Definition of Ready

Uma historia so inicia quando:

- objetivo claro;
- criterios de aceite definidos;
- impacto arquitetural compreendido.

## Definition of Done

Uma historia termina quando:

- implementacao concluida;
- testes criados;
- documentacao atualizada;
- review aprovado;
- LT Aprendiz compreende a solucao;
- TM Chatinho aprova.

## Regra de Ouro

O Dev pode discordar do LT Aprendiz.

O LT Aprendiz pode discordar do Dev.

Quando houver impasse tecnico, o TM Chatinho decide.

A decisao deve sempre ser acompanhada da justificativa tecnica.


