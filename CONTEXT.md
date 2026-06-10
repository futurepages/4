# CONTEXT.md — Futurepages Web Framework v4

> Glossário do domínio. Mantido durante a sessão grill-with-docs.
> Contém apenas termos e definições, sem detalhes de implementação.

## Status do Projeto

- **Framework legado** — não será mais evoluído. O objetivo é preservar conhecimento operacional para manutenção do ecossistema que depende dele.
- **Versão:** 4.2.0-SNAPSHOT
- **Java 8 / JEE6** (Servlet 3.0, JSP 2.2)

## Termos do Domínio

### Ação (Action)

Classe que implementa a lógica de uma requisição. Estende `AbstractAction` ou uma de suas subclasses especializadas. Cada Action retorna uma String de resultado ("success", "error", "login", etc.) que é mapeada para uma Consequence.

#### Hierarquia de Actions

| Classe | Proteção | Uso |
|--------|----------|-----|
| `AbstractAction` | (base) | Implementa Action, fornece validação, paginação, output/input helpers |
| `FreeAction` | Pública | Actions que não exigem autenticação (implementa `AuthenticationFree`) |
| `LoginAction` | Pública | Tela de login (implementa `AuthenticationFree` + `HiddenRequestAction`) |
| `ProtectedAction` | Autenticada | Actions que exigem usuário logado (implementa `RedirectAfterLogin`) |
| `CrudActions` | Mista | CRUD automático com inner actions explore/show/create/update/delete. Implementa `AuthenticationFree` + `RedirectAfterLogin` |
| `FreeCrudActions` | Pública | CRUD público, herda de `CrudActions` |
| `ProtectedCrudActions` | Autenticada | CRUD autenticado, herda de `CrudActions` |
| `AjaxAction` | (interface) | Marca uma action como AJAX. Extende `AsynchronousAction` |
| `DynAction` | (interface) | Marca uma action como dinâmica (AJAX). Extende `AsynchronousAction` |
| `NullAction` | Pública | Action vazia (no-op), implementa `DontTrackURL` |

#### Interfaces Marcadoras (Marker Interfaces)

Controlam o comportamento do framework sem exigir implementação de métodos (exceto quando indicado):

| Interface | Método | Efeito |
|-----------|--------|--------|
| `AuthenticationFree` | `bypassAuthentication(innerAction)` | Action não é bloqueada pelo `AuthenticationFilter`. Pode decidir por inner action |
| `RedirectAfterLogin` | `shouldRedirect(innerAction)` | Após login, redireciona de volta para esta action |
| `AllModulesFree` | — (marker) | Action pode ser acessada por usuários com permissão a qualquer outro módulo. Bypassa `ModulePermissionFilter` |
| `GlobalFilterFree` | `isGlobalFilterFree(innerAction)` | Action não executa filtros globais. Pode decidir por inner action |
| `AsynchronousAction` | — (marker) | Base para ações assíncronas (AJAX). Pai de `DynAction` e `AjaxAction` |
| `DynAction` | — (marker) | Action dinâmica (AJAX). Usada com `AbstractModuleManager.dynAction()` |
| `AjaxAction` | — (marker) | Action AJAX. Usada com `AbstractModuleManager.ajaxAction()` |
| `HiddenRequestAction` | — (marker) | Request oculto no log de exceções (ex: LoginAction para não logar senhas) |
| `DontTrackURL` | — (marker) | Action não é rastreada pelo URLTracker |
| `UserSessionProtected` | — (marker) | Marker da camada Jersey para endpoints REST que dependem de proteção baseada em sessão; o comportamento efetivo depende do wiring do projeto |
| `RestTokenProtected` | — (marker) | Marker da camada Jersey para endpoints REST que dependem de proteção por token; o comportamento efetivo depende do wiring do projeto |

### API Única do Framework
Embora o código-fonte esteja organizado nos pacotes `org.futurepages.menta.*` (herdado do Mentawai) e `org.futurepages.core.*` (escrito nativamente), para fins de uso ambos fazem parte da **mesma API do Futurepages**. Um agente que escreve código contra o framework não precisa distinguir a origem.

### Menta Layer (origem histórica)
Camada MVC herdada do Mentawai (por Sérgio Oliveira). Contém o front-controller, actions, filters, consequences e tags JSP.

### Core Layer (origem histórica)
Camada de serviços e infraestrutura escrita nativamente do Futurepages. Inclui persistência (Hibernate 4), autenticação, validação, locale, mail, formatação, migração, agendamento (Quartz) e relatórios (JasperReports).

### Módulo (Module)
Unidade organizacional do framework. Um módulo agrupa actions, beans (entidades), DAOs, services, validators, filters, tags, formatters e JSPs em um contexto coeso. Cada módulo tem um `ModuleManager.java` que configura suas rotas, filtros, consequências e dependências.

### Inner Action
Método específico dentro de uma Action, invocado por reflexão quando a URL contém `ActionName-innerAction` (separador hífen). Permite que uma Action tenha múltiplos comportamentos sem criar classes separadas. Ex: `UserActions-explore` chama o método `explore()` em `UserActions`.

### Type Parameter
Parâmetro `?type=explore` que o `CrudActions.execute()` lê para determinar qual fluxo executar (`explore`, `show`, `create`, `update`, `delete`). A resolução do tipo segue esta precedência:
1. Parâmetro `type` na request (`?type=explore`)
2. Inner action da URL (`UserActions-explore` → inner action vira o type)
3. Padrão: `SHOW`

### CrudActions Hooks
Métodos que `CrudActions` chama durante o ciclo de vida:
| Hook | Chamado para | Finalidade |
|------|-------------|------------|
| `listObjects()` | EXPLORE | Carrega lista de objetos para exibição |
| `doListDependencies()` | CREATE, UPDATE | Prepara listas de dependências (selects, combos) |
| `restoreObject()` | SHOW, UPDATE, DELETE | Carrega o objeto do banco pelo ID |

### Result String
Constante string retornada por uma Action que determina qual Consequence será executada. Definidas em `Manipulable`: `SUCCESS`, `ERROR`, `LOGIN`, `REDIR`, `ACCESS_DENIED`, `AJAX_DENIED`, `EXCEPTION`, `NOT_FOUND`, `SUBMIT`, `EXPLORE`, `SHOW`, `CREATE`, `UPDATE`, `DELETE`, etc.

### Consequence
Manipulador de resultado de uma Action. Tipos: `Forward` (JSP), `Redirect`, `Chain` (encadeia outra Action), `StreamConsequence` (arquivos/binário), `AjaxConsequence`, `StringConsequence`.

### Filter
Interceptor que executa antes e/ou depois de uma Action na cadeia de invocação (`InvocationChain`). Usado para injeção de parâmetros (`InjectionFilter`), transação Hibernate (`HibernateFilter`), autenticação (`AuthenticationFilter`), permissão de módulo (`ModulePermissionFilter`).

### ApplicationManager
Classe de bootstrap do framework. Registra actions, módulos, filtros globais, locales e formatters. O projeto convite_in_web usa a implementação padrão (`org.futurepages.menta.core.ApplicationManager`) e delega configurações específicas para cada `ModuleManager`.

### ModuleManager
Configurador de um módulo específico. Registra actions do módulo, mapeia consequências, define filtros da cadeia e configura formatters/locales locais.

### Bean / Entidade
Classe Java com anotações Hibernate (`@Entity`, `@Table`, `@ManyToOne`, etc.) que mapeia uma tabela do banco de dados. Gerenciada pela camada de persistência do Core Layer.

### DAO (Data Access Object)
Classe que encapsula consultas HQL para uma entidade. O padrão vigente é:
- **Estender `HQLProvider`** para herdar os métodos fluentes de construção de HQL
- **Usar `Dao.getInstance()`** (singleton do `GenericDao`) para operações CRUD: `.save()`, `.update()`, `.delete()`, `.get()`, `.list()`, `.paginationSlice()`
- Métodos de consulta são **estáticos** na classe DAO
> `EntityDao<BEAN>` foi um experimento que não vingou e não deve ser usado.

### HQLProvider / HQLQuery
Sistema fluente de construção de HQL. `HQLProvider` fornece métodos estáticos para cláusulas HQL; `HQLQuery<T>` monta a consulta de forma tipada e segura.

### Validator (Menta)
Classe que valida o estado de uma entidade ou ação. Estende `org.futurepages.menta.core.validation.Validator` e usa `error(chave, mensagem)` para registrar falhas. Pode operar em modo `breakOnFirst` (lança `UserException` no primeiro erro) ou coletivo (acumula erros no `validationMap`).
> Apenas o Validator do pacote menta é vigente. O `org.futurepages.core.validation.*` está @Deprecated e não deve ser usado.

#### UserException
Exceção da camada de controle que carrega mensagens de erro para o usuário. Pode conter um `validationMap` com erros de validação. A ação captura exceções de domínio (service layer) e as converte em `UserException` para a view.

### Service
Classe de negócio que contém a lógica de domínio. Operações que podem falhar lançam **exceções de modelo** (domínio). A Action captura essas exceções e as converte em `UserException` para exibição na view. Idealmente, validação de negócio reside nos services, não nos validators.

### Tag / TagLib
Componente JSP customizado. Anotado com `@Tag` e `@TagAttribute`. O `TagDeclarationBuilder` lê as anotações e gera automaticamente o arquivo `taglib.tld` (Tag Library Descriptor).

### Tag File de Aplicacao
Arquivo `.tag` definido pelo projeto consumidor em `WEB-INF/tags/` e incorporado ao mesmo namespace da taglib da aplicacao. Nao deve ser confundido com tag core do framework implementada em Java.

### TemplateServlet
Servlet que processa arquivos `.page` — um sistema de templates alternativo ao fluxo Action→Forward. Configurado por `app-template.xml` que define regras de layout por padrão de nome de página.

### JerseyAction
Classe base da camada REST Jersey no ecossistema Futurepages. Expõe `Input`, `Output`, helpers de redirect/JSP e respostas utilitárias reaproveitando parte da infraestrutura das Actions MVC.

### JerseyController
Servlet da camada Jersey que delega para o `ServletContainer`, ajusta encoding e garante fechamento/rollback de sessão Hibernate ao fim da requisição REST.

### ApplicationListener
Implementa `ServletContextListener`. Ponto de entrada do framework na inicialização da aplicação (`contextInitialized`). Ordem de execução:
1. `Apps.init(contextName)` — carrega `app-params.xml`
2. Inicializa `AppLogger`, `MailConfig`
3. Escaneia módulos do diretório `MODULES_CLASSES_REAL_PATH`
4. `SchemaGeneration` — gera/atualiza schema do banco (se não estiver em produção)
5. `InstallersManager.initialize()` — instala dados iniciais
6. `DataModelMigrationController.execute()` — executa migrações versionadas
7. `QuartzManager.initialize()` — inicia agendador Quartz
8. `TagLibBuilder` — gera `taglib.tld` (se `GENERATE_TAGLIB=true`)
9. `ResourceMinifier` — minifica JS/CSS

### InitManager
Estende `AbstractApplicationManager` e configura o bootstrap do MVC:
- Registra filtros globais na ordem: `AutoRedirectDomainFilter`, `HibernateFilter` (ou `ExceptionFilter`), `HeadTitleFilter`, `FileUploadFilter`, `InjectionFilter`
- Mapeia consequências globais (`EXCEPTION`, `NOT_FOUND`, `REDIR`, etc.)
- Registra a Action Inicial (`INIT_ACTION` → `START_PAGE_NAME`)

### QuartzManager
Gerenciador de jobs agendados. Integra o Quartz scheduler ao framework.
- **Ativação:** `QUARTZ_MODE=on` no `app-params.xml`
- **Descoberta automática:** Escaneia o diretório `jobs/` de cada módulo por classes que implementam `org.quartz.Job` e têm a anotação `@CronTrigger(expression = "...")`
- **`@CronTrigger`** — anotação que define a expressão cron do job
- **`QuartzManager.newDelayedJob()`** — utilitário para criar `ScheduledExecutorService` com intervalos fixos (ex: processar fila de emails a cada 20s)
- **Desligamento:** `shutdown()` é chamado no `contextDestroyed` do `ApplicationListener`

### Migration (DataModelMigrationController)
Sistema de migrações versionadas de esquema de banco de dados.
- **Interface:** `VersionedDataModel` — define `getVersion()`, `addVersion()`, `registerNoChanges()`
- **Implementação concreta:** entidade JPA que persiste o histórico de versões (ex: `sistema_datamodel`)
- **Diretório:** `src/migration/versions/` com arquivos nomeados `V_{numero}_{subnumero}__{descricao}.(sql|class)`
  - `.sql` → executa comandos SQL diretamente
  - `.class` → instancia classe via reflexão, lógica no construtor
- **Ordenação:** numérica (`V_12_1` = 12.1, vem antes de `V_12_2`)
- **Compatibilidade:** Se `SCHEMA_GENERATION_TYPE` ou `INSTALL_MODE` não forem `none`/`off`, as migrações são registradas mas não executadas (skipped)
- **Pastas auxiliares:** `past/` (versões arquivadas), `stage/` (versões em desenvolvimento, não executadas)
- **Configuração:** `APP_DATA_MODEL_CLASS` aponta para a classe que implementa `VersionedDataModel`

### InstallersManager
Sistema de instalação programática de dados iniciais.
- **Ativação:** `INSTALL_MODE` no `app-params.xml`
  - `off` — não executa
  - `on` — Resources + módulos + Examples
  - `modules` — Resources + módulos apenas
  - `production` — Resources + módulos (sem Examples)
  - `script:nome.sql` — executa apenas um SQL
- **Diretório:** `install/` (global) e `modules/*/install/` (por módulo)
- **Interface:** `Installation` com método `execute()`
- **Classe base:** `Installer` — já chama `execute()` no construtor e mede tempo; provê `install(subInstalador)` e `executeSQLFromFile(path)`
- **Ordem de execução:**
  1. `install.Resources` (global)
  2. Script SQL (se `script:nome.sql`)
  3. Instaladores de cada módulo (ordenados alfabeticamente)
  4. Extra: `install.Examples` (se `on`/`examples`) ou `install.{Modo}` personalizado

### Automations
Classe base para `InstallersManager` e `QuartzJobsRegister`. Fornece `getModulesDirectoryClasses(superKlass, annotation)` que escaneia diretórios de módulos por classes que estendem `superKlass` ou têm a anotação.

### Core Layer — Subsistemas

- **Persistence**: Hibernate 4 com `GenericDao`, `EntityDao<BEAN>`, suporte a múltiplos schemas
- **Auth**: `DefaultUser`/`DefaultRole`/`DefaultModule` interfaces; `AuthenticationFilter` + `ModulePermissionFilter`
- **Validation**: `Validator` (menta) + `ServiceValidator`/`EntityValidator` (core, @Deprecated)
- **Locale**: Sistema de internacionalização via arquivos TXT + `LocaleManager`
- **Formatter**: 20+ formatadores (CPF, CNPJ, telefone, data, moeda, etc.)
- **Mail**: Envio de email simples, HTML e multipart
- **Migration**: Versões de migração SQL + classes Java versionadas
- **Quartz**: Agendamento de jobs
- **JasperReports**: Integração com relatórios
- **Services**: Integração SOAP e REST (Jersey)
