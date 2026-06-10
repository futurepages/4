# Futurepages Web Framework v4 — Documentação

> **Framework legado** — Java 8 / JEE6 (Servlet 3.0, JSP 2.2)  
> Versão: 4.2.0-SNAPSHOT  
> Licença: LGPL-3.0

## Comece por Aqui

Leitura recomendada para manutencao legada:

1. [Glossário](../CONTEXT.md) — termos canonicos do framework
2. [Visão Geral da Arquitetura](architecture/ARCHITECTURE.md) — mapa do ecossistema
3. [Fluxo de Requisição](architecture/REQUEST_FLOW.md) — MVC classico via `Controller`
4. [Padrões de Actions](guides/ACTION_PATTERNS.md) — conventions reais de `Action`, `CrudActions`, `AjaxAction` e `DynAction`
5. [Persistência](guides/PERSISTENCE.md) — DAO, HQL e transacoes
6. [Views e Templates](guides/VIEWS.md) — JSP, TemplateServlet, `app-template.xml` com exemplos práticos
7. [Tag Files](guides/TAG_FILES.md) — componentes reutilizáveis, namespace, padrões de design
8. [Assets e Cache](guides/ASSETS.md) — CSS/JS, cache-busting, minificação, estratégias CDN vs local
9. [Padrões AJAX](guides/AJAX_PATTERNS.md) — encapsulamento de AJAX em tag files, exemplos práticos
10. [REST API](guides/REST.md) — camada Jersey usada por projetos do ecossistema

## Como Ler esta Documentacao

- A documentacao descreve o **framework** e tambem aponta excecoes observadas nos projetos reais `convite_in_web` (complexo) e `alunoonline` (simples).
- Quando houver diferenca entre a forma "ideal" e a forma usada em producao, a documentacao deve deixar isso explicito.
- O objetivo principal e **manutencao segura**, nao modernizacao.

## Skill Local

Existe uma skill local do OpenCode para manutencao deste framework em:

- `.opencode/skills/futurepages4-framework/SKILL.md`

Ela foi alinhada a esta documentacao do `futurepages4` e aos projetos exemplo `convite_in_web` e `alunoonline`.

## Índice da Documentação

### Arquitetura
- [Visão Geral da Arquitetura](architecture/ARCHITECTURE.md) — MVC, pacotes, camadas
- [Fluxo de Requisição](architecture/REQUEST_FLOW.md) — ciclo de vida completo de uma request
- [Estrutura de Módulo](modules/OVERVIEW.md) — organização de um módulo

### Guias
- [Criação de Módulo](guides/MODULE_CREATION.md) — passo a passo para criar um novo módulo
- [Padrões de Actions](guides/ACTION_PATTERNS.md) — actions, inner actions, interfaces marcadoras
- [Persistência](guides/PERSISTENCE.md) — DAOs, HQLProvider, GenericDao, paginação
- [Views e Templates](guides/VIEWS.md) — JSPs, TemplateServlet (.page), taglib fpg: core, exemplos alunoonline vs convite_in_web
- [Tag Files](guides/TAG_FILES.md) — componentes reutilizáveis em `WEB-INF/tags/`, namespace, padrões, ciclo de vida
- [Assets e Cache](guides/ASSETS.md) — CSS/JS, cache-busting, minificação, estratégias local vs CDN
- [Padrões AJAX](guides/AJAX_PATTERNS.md) — encapsulamento AJAX em tag files, ajaxSelect, upload, autocomplete, paginação
- [Validação](guides/VALIDATION.md) — Validator, UserException, boas práticas
- [Migrações](guides/MIGRATIONS.md) — DataModelMigrationController, SQL e Java
- [Jobs Agendados](guides/QUARTZ.md) — QuartzManager, @CronTrigger, LoggableJob
- [Instalação de Dados](guides/INSTALLERS.md) — InstallersManager, dados iniciais e exemplos
- [Inicialização da Aplicação](guides/BOOT.md) — ApplicationListener, InitManager, ordem de boot
- [REST API](guides/REST.md) — Jersey, endpoints REST

### Referência
- [Glossário](../CONTEXT.md) — termos do domínio
- [Configuração](architecture/CONFIG.md) — app-params.xml e propriedades
- [Filtros Built-in](architecture/FILTERS.md) — filtros do framework
- [Utilitários](architecture/UTILS.md) — Is, The, DateUtil, etc.

## Pontos de Atenção

- **Views e Tag Files** — A documentação em `guides/VIEWS.md` agora diferencia claramente entre:
  - Tags core do framework (Java)
  - Tag files de aplicação (JSP em `WEB-INF/tags/`)
  - Exemplos práticos de `alunoonline` (simples) vs `convite_in_web` (complexo)
  
- **Tag Files** — Novo guia completo em `guides/TAG_FILES.md` cobre:
  - Organização por namespace (core, modules, resource)
  - Anatomia de uma tag file (atributos, variáveis implícitas)
  - Injeção de CSS/JS via `<fpg:head>` e `<fpg:footer>`
  - Padrões de reutilização e composição
  - Debugging e ciclo de vida

- **Assets e Cache-Busting** — Novo guia em `guides/ASSETS.md` cobre:
  - Estratégia CDN vs local (comparação alunoonline vs convite_in_web)
  - Cache-busting com `${params.RELEASE_QUERY}`
  - Minificação automática via `ResourceMinifier`
  - Organização de assets (CSS, JS, imagens)
  - Quando usar cada estratégia

- **Padrões AJAX** — Novo guia em `guides/AJAX_PATTERNS.md` cobre:
  - Encapsulamento de AJAX em tag files (não usar inline)
  - Padrões práticos: ajaxSelect, uploadImage, autoComplete, paginação
  - Response padrão em endpoints (BaseResponse)
  - Boas práticas (timeout, validação, error handling)
  - Debugging com DevTools

- `guides/REST.md` cobre a camada Jersey em nivel operacional. Ela existe no framework, mas depende de wiring do projeto consumidor.

- Transacoes anotadas e transacoes manuais convivem no ecossistema. Consulte tambem `guides/PERSISTENCE.md` antes de alterar fluxo transacional.
