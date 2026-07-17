# Story #46 — Architecture Gate: Baseline Inicial de Segurança

**Status:** RECOMMENDED — aguardando aprovação explícita do Engineering Manager

**Data:** 2026-07-17

**Fonte funcional:** [Sprint 5 Product Plan](../../team/sprints/SPRINT_5_PRODUCT_PLAN.md)

**Baseline de entrada:** [Story #46 Documentation Baseline](STORY_046_DOCUMENTATION_BASELINE.md)

## 1. Decisão proposta

Recomenda-se uma barreira de autenticação HTTP Basic, stateless e comum aos
três serviços. Ela protege todas as rotas REST de negócio sob `/api/v1/**` e
aceita uma única credencial técnica de consumidor de API, fornecida somente por
configuração de ambiente. A decisão entrega a categoria funcional única de
consumidor autenticado sem criar usuários persistidos, papéis ou autorização
por recurso.

O Spring Security será acrescentado aos serviços Catalog, Inventory e Order. A
configuração deverá desabilitar sessão e CSRF para esta API stateless, exigir
autenticação em `/api/v1/**` e usar HTTP Basic. Credenciais ausentes, inválidas
ou malformadas receberão `401 Unauthorized`, com desafio `WWW-Authenticate`,
antes de controller, validação, caso de uso ou acesso a dados.

Nenhuma implementação é autorizada por este documento. A implementação só pode
começar após aprovação explícita do Engineering Manager.

## 2. Fronteiras de superfície

| Superfície | Decisão | Justificativa |
| --- | --- | --- |
| Catalog: todas as rotas `/api/v1/products/**` e `/api/v1/skus/**` | Protegida | São operações ou informações de negócio. |
| Inventory: todas as rotas `/api/v1/inventory/**`, inclusive consultas de processamento e observações | Protegida | São capacidades ou informações de negócio/operacionais da Story #34 e #44. |
| Order: todas as rotas `/api/v1/orders/**` e `/api/v1/customers/**` | Protegida | São comandos ou informações de negócio, inclusive checkout e confirmação. |
| `GET /actuator/health` | Exceção técnica pública | Permite health check local; não expõe informação de negócio. Outros endpoints Actuator continuam não expostos pela configuração atual. |
| `/v3/api-docs/**`, `/swagger-ui/**` e recursos necessários da UI | Exceção técnica pública | Preserva a documentação técnica local; ela não substitui autenticação para executar a API. |
| Kafka, consumers e workers locais | Fora da fronteira HTTP | Não recebem HTTP de consumidor e não alteram `OrderConfirmed` v1. |

Uma rota nova de negócio sob `/api/v1/**` herda a proteção. Qualquer nova
exceção fora desta tabela exige avaliação arquitetural antes de ser liberada.

## 3. Contrato de autenticação e configuração

| Aspecto | Contrato |
| --- | --- |
| Esquema | `Authorization: Basic <base64(username:password)>` sobre a API HTTP local. |
| Identidade | Uma única identidade técnica de consumidor de API; não representa usuário, cliente, papel ou ownership de dados. |
| Configuração | `SECURITY_API_USERNAME` e `SECURITY_API_PASSWORD`, obrigatórias e sem valor padrão no repositório. A aplicação deve falhar ao iniciar se estiverem ausentes ou vazias. |
| Armazenamento | Não haverá tabela, migration, endpoint de cadastro, login, rotação ou gestão de credenciais nesta Story. |
| Senha | Comparada por encoder seguro do Spring Security; nunca registrada em logs, respostas, exemplos, documentação ou relatórios. |
| Sessão e CSRF | Sem sessão (`STATELESS`) e CSRF desabilitado exclusivamente para esta API HTTP sem cookies. |
| Falha de autenticação | `401 Unauthorized` genérico, com `WWW-Authenticate`; não revelar se usuário ou senha falhou. |
| Autorização | Todo consumidor autenticado tem o mesmo acesso às rotas de negócio; não há roles, claims, escopos ou regras por recurso. |

HTTP Basic é uma baseline local deliberadamente pequena. Fora de um canal HTTPS
confiável, ele não é aceitável como proteção de transporte: TLS, gateway,
provedor de identidade, tokens e rotação são evolução futura e não são
implicitamente autorizados por esta Story.

## 4. Compatibilidade entre serviços e eventos

Order continua chamando as rotas protegidas de Inventory para reservar,
confirmar e liberar estoque. O adaptador REST do Order deve anexar a mesma
credencial técnica configurada (`SECURITY_API_USERNAME` e
`SECURITY_API_PASSWORD`) às suas chamadas internas, além de preservar o atual
`X-Correlation-Id`. Não será criado endpoint interno alternativo, bypass de
segurança, trust por IP, banco compartilhado ou nova dependência runtime.

O contrato REST — métodos, paths, payloads e respostas de negócio após uma
autenticação válida — permanece inalterado. Erros de domínio e validação para
requisições autenticadas preservam sua semântica atual. `OrderConfirmed` v1,
tópico, envelope, producer, consumer, grupo Kafka e o perfil `kafka` não sofrem
mudança: a autenticação desta Story é exclusivamente uma fronteira HTTP.

## 5. Implementação limitada e documentação posterior

- Adicionar `spring-boot-starter-security` aos três serviços e uma configuração
  explícita, homogênea e testável por serviço.
- Usar propriedades tipadas/validadas para a configuração de credencial; não
  codificar valores em Java, YAML, testes versionados ou coleções públicas.
- Fazer o `InventoryRestAdapter` enviar Basic Auth somente à URL configurada do
  Inventory; não propagar automaticamente a credencial recebida do consumidor.
- Atualizar OpenAPI, README e orientação de execução após a implementação para
  declarar o header, as exceções técnicas e como injetar segredos localmente.
- Não alterar agregados, migrations de domínio, eventos, tópicos, contratos de
  negócio ou regras de catálogo, estoque, pedido e pagamento simulado.

## 6. Alternativas e trade-offs

| Alternativa | Decisão | Motivo |
| --- | --- | --- |
| HTTP Basic stateless com credencial de ambiente | Escolhida | É interoperável, verificável em chamadas independentes e não introduz gestão de identidade. |
| API key customizada | Rejeitada | Exigiria protocolo e filtro próprios sem ganho para a categoria única de acesso. |
| JWT/OAuth2/IdP | Adiado | Requer emissor, ciclo de vida e decisões de identidade fora do escopo. |
| Usuários persistidos, roles e ACL | Rejeitado | Cria regra funcional e autorização granular não aprovadas. |
| Liberar Inventory para chamadas internas sem autenticação | Rejeitado | Criaria uma brecha nas rotas de negócio e invalidaria a barreira uniforme. |
| Proteger health e OpenAPI como se fossem negócio | Rejeitado | Impediria diagnóstico e consulta de contrato técnico; as exceções são limitadas e explícitas. |

Trade-off aceito: uma credencial compartilhada não atribui identidade individual
nem permite revogação granular. A baseline prova controle de acesso, não
auditoria nem uma solução completa de gestão de identidade.

## 7. Evidência obrigatória antes do Quality Gate

1. Para cada serviço, uma chamada sem `Authorization` a uma rota representativa
   de `/api/v1/**` retorna `401` antes de efeito de negócio; ao menos um cenário
   de escrita deve comprovar que o estado não foi criado ou alterado.
2. A mesma capacidade, com credencial válida fornecida somente ao ambiente de
   teste, mantém status, payload e regra de negócio aprovados.
3. A execução autenticada do fluxo completo de pedido — reserva, pagamento
   simulado, confirmação e processamento `OrderConfirmed` v1 — continua
   funcional, incluindo a chamada Order → Inventory autenticada.
4. Credencial inválida também retorna `401` genérico, sem segredo em body ou
   logs; chamadas autenticadas continuam retornando os erros de domínio atuais.
5. `GET /actuator/health` e documentação OpenAPI permanecem acessíveis como
   exceções técnicas, enquanto nenhuma rota de negócio fica fora de `/api/v1/**`
   sem classificação explícita.
6. Testes unitários/configuração e de integração cobrem Catalog, Inventory e
   Order; a evidência reproduzível usa variáveis de ambiente ou injeção dinâmica
   de segredo, nunca um segredo versionado.

## 8. Riscos e condições de parada

| Risco | Controle / condição |
| --- | --- |
| Falha de startup ou de testes por credencial ausente | Validar configuração obrigatória e injetar valores apenas no ambiente de execução. |
| Order → Inventory receber `401` | Configurar Basic Auth no adaptador e cobrir o fluxo end-to-end autenticado. |
| Rota de negócio esquecida | A regra por prefixo `/api/v1/**` é a proteção padrão; inventariar qualquer novo prefixo antes de expô-lo. |
| Exposição de segredo | Não versionar, logar ou refletir credenciais; mascarar valores em evidências. |
| Basic Auth usado sem transporte seguro em futuro ambiente remoto | Parar e escalar: TLS/terminação de transporte e gestão de credenciais exigem decisão de infraestrutura/segurança adicional. |
| Necessidade de identidade individual, roles, acesso por recurso ou auditoria | Parar e devolver ao Product Owner como nova descoberta de produto. |

## 9. Rastreabilidade de ADR e insumo ao Technical Writer

Esta decisão é transversal aos serviços Catalog, Inventory e Order e altera a
fronteira compartilhada de segurança e a configuração de execução. Portanto,
**um ADR é obrigatório** antes da nova Architecture Approval. O Technical
Writer deverá criar o próximo ADR disponível no índice oficial, usando o
conteúdo técnico abaixo; a numeração, o arquivo e a manutenção do índice são
de sua responsabilidade.

### Insumo técnico obrigatório para o ADR

- **Contexto:** a Story #46 exige autenticação antes de qualquer operação ou
  leitura de negócio nos três serviços, preservando contratos de negócio,
  `OrderConfirmed` v1 e a integração REST Order → Inventory.
- **Decisão:** HTTP Basic stateless com uma única credencial técnica por
  ambiente; Spring Security protege `/api/v1/**`; sessão é stateless e CSRF é
  desabilitado para a API sem cookies.
- **Fronteiras:** `/api/v1/**` é negócio e exige autenticação; somente
  `GET /actuator/health`, OpenAPI e Swagger UI são exceções técnicas públicas.
  Kafka e seus workers ficam fora da fronteira HTTP.
- **Configuração e compatibilidade:** `SECURITY_API_USERNAME` e
  `SECURITY_API_PASSWORD` são obrigatórias, não possuem default e não são
  versionadas. Order envia a mesma credencial configurada ao Inventory, sem
  propagar a credencial recebida, preservando `X-Correlation-Id`, paths,
  métodos, payloads e semântica dos erros autenticados.
- **Trade-offs e limites:** a credencial compartilhada não fornece identidade
  individual, rotação granular, auditoria, roles ou autorização por recurso.
  HTTP Basic requer canal HTTPS confiável fora do ambiente local; TLS remoto,
  gateway, IdP, tokens e gestão de identidade são decisões futuras e não são
  autorizadas por esta Story.

O ADR deverá referenciar este Architecture Gate e, após publicado, este gate
deverá ser devolvido junto com ele ao Engineering Manager. Qualquer divergência
do insumo que introduza TLS/infraestrutura remota, gestão de identidade,
autorização granular ou escopo de produto deve parar e ser escalada.

## 10. Parecer e handoff

**RECOMMENDED.** A decisão cobre de maneira uniforme as superfícies de negócio
identificadas, preserva contratos REST e Kafka existentes e mantém a solução
proporcional ao recorte aprovado. O Engineering Manager deve aprovar este
contrato antes de criar a branch de implementação.

## Institutional Handoff — Technical Lead → Technical Writer

### Executive summary

O Architecture Gate da Story #46 foi complementado com a rastreabilidade de ADR
obrigatória e com o insumo técnico para sua publicação. A decisão continua
recomendando HTTP Basic stateless, configurado por ambiente, para as APIs de
negócio dos serviços Catalog, Inventory e Order.

### Objective completed

Foi registrado que a decisão transversal exige ADR e foi fornecido ao Technical
Writer o conteúdo técnico delimitado para publicá-lo, sem alterar mecanismo,
superfícies, compatibilidade ou escopo previamente recomendados.

### Published artifacts

- `docs/architecture/contracts/STORY_046_ARCHITECTURE_GATE.md`
- `docs/team/sprints/sprint-005/STATUS.md`

### Evidence and constraints

- Baseado na Documentation Baseline publicada em `main` /
  `de2dd0bd5a5b36135ad4ebe4aea7092809992fb0`.
- Mantém íntegros os contratos REST de negócio após autenticação válida e o
  contrato `OrderConfirmed` v1.
- Não autoriza implementação, criação de branch, Quality ou mudanças de escopo
  até o ADR ser publicado e a nova aprovação explícita do Engineering Manager.

### Pending items

- Technical Writer: publicar o ADR no diretório e índice oficiais e devolver o
  ADR juntamente com este gate ao Engineering Manager.

### Next authorized action

- Next role: Technical Writer.
- Required action: publicar exclusivamente o ADR da baseline de autenticação a
  partir do insumo da seção 9 e atualizar o índice oficial de ADRs.
- Acceptance / stop criteria: parar e escalar se a documentação do ADR exigir
  mudança de escopo, TLS/infraestrutura remota, gestão de identidade ou
  autorização granular; não iniciar implementação ou Quality.
- Operational command: iniciar exclusivamente a publicação do ADR e o handoff
  Technical Writer → Engineering Manager para nova Architecture Approval.
