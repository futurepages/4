---
name: futurepages4-framework
description: >
  Framework legado Java 8 / JEE6 para manutencao em projetos Futurepages4.
  Use ao editar codebases com ModuleManager, Action, Dao.getInstance(),
  HQLProvider, JSP/.page, installers, migrations ou jobs Quartz do ecossistema.
---

# Futurepages4 Framework

Use esta skill para **manutencao legada** em projetos baseados no Futurepages Web Framework v4.

Objetivo: fazer a menor alteracao correta, **seguindo o padrao ja existente no projeto** e sem introduzir camadas novas, bibliotecas novas ou reescritas arquiteturais.

## Escopo

- Stack base: Java 8, JEE6, Servlet 3.0, JSP 2.2, Hibernate 4, Quartz, Jersey.
- O framework e **legado** e **nao esta em evolucao ativa**.
- A prioridade nao e modernizar. A prioridade e **manter compatibilidade operacional**.

## Fontes de Verdade

Leia nesta ordem quando precisar de contexto:

1. `futurepages4/CONTEXT.md`
2. `futurepages4/docs/README.md`
3. `futurepages4/docs/architecture/ARCHITECTURE.md`
4. `futurepages4/docs/architecture/REQUEST_FLOW.md`
5. Guias especificos em `futurepages4/docs/`
6. O projeto alvo

Se a documentacao geral e o projeto alvo divergirem, siga esta regra:

- Para **conceitos do framework**, use `futurepages4/CONTEXT.md`.
- Para **comportamento local**, use o padrao ja consolidado no projeto alvo.
- Se houver divergencia relevante, trate-a como excecao do projeto, nao como convite para "corrigir tudo".

## Regra Principal de Trabalho

Antes de editar, descubra o padrao local no projeto:

1. ache um modulo parecido
2. leia o `ModuleManager.java`
3. leia a Action equivalente
4. leia a DAO/Service/Validator equivalente
5. copie o estilo existente

No ecossistema Futurepages4, o melhor padrao quase sempre e **o que aquele projeto ja repete**.

Se a tarefa envolver REST/Jersey, troque a heuristica para:

1. leia o `web.xml` do projeto alvo
2. descubra a classe `javax.ws.rs.Application` configurada
3. leia a `ResourceConfig` concreta
4. descubra os packages reais de endpoints
5. so entao altere endpoint, provider, seguranca ou serializacao

## Modelo Mental Canonico

Use estes termos exatamente como no glossario:

- **Action**: classe que atende a requisicao e retorna uma **Result String**
- **Consequence**: handler do resultado (`Forward`, `Redirect`, `Chain`, AJAX, stream)
- **ModuleManager**: configurador do modulo
- **Inner Action**: metodo chamado por `Action-innerAction`
- **Type Parameter**: `?type=explore|show|create|update|delete`
- **DAO**: classe com consultas HQL, normalmente estatica, estendendo `HQLProvider`
- **Service**: regra de negocio
- **Validator (Menta)**: `org.futurepages.menta.core.validation.Validator`

Nao troque esses nomes por nomenclatura de Spring, Rails ou DDD moderno.

## O Que Nao Fazer

Nao introduza sem necessidade concreta:

- Spring, CDI, Guice, Lombok, MapStruct, JPA Repository, Criteria API moderna
- controllers/rest handlers paralelos para substituir Actions
- services stateful/injetados quando o projeto usa metodos estaticos
- refatoracoes amplas de transacao, boot, filters ou persistencia
- substituicao de JSP/taglib por frontend novo
- trocas de estrutura de modulo

Tambem nao assuma que o framework esta sendo usado do jeito "ideal" da documentacao. Em projetos reais ha muitos padroes hibridos.

## Estrutura Real de Modulo

Estrutura comum:

```text
src/modules/{modulo}/
  ModuleManager.java
  actions/
  beans/
  dao/
  services/
  validators/
  filters/
  install/
  jobs/

web/modules/{modulo}/
  *.jsp
  *.page
  dyn/
  includes/
  template/
```

Nem toda pasta existe em todo modulo. Nao crie diretorios desnecessarios so para "completar o padrao".

## Actions e ModuleManager

Hierarquia base:

- `FreeAction`: publica
- `LoginAction`: publica e oculta request sensivel em logs
- `ProtectedAction`: autenticada
- `CrudActions`: CRUD com `type` e inner actions
- `FreeCrudActions` e `ProtectedCrudActions`: especializacoes de `CrudActions`

Interfaces importantes:

- `AuthenticationFree`
- `RedirectAfterLogin`
- `AllModulesFree`
- `GlobalFilterFree`
- `AjaxAction`
- `DynAction`
- `DontTrackURL`

Nuances verificadas no codigo do framework e no projeto exemplo:

- `AllModulesFree` **nao significa publico**. Significa que uma action protegida pelo `ModulePermissionFilter` pode ser acessada por quem tem permissao para qualquer outro modulo.
- `CrudActions` resolve o fluxo por `?type=` e, se nao houver `type`, usa a inner action; se ambos faltarem, cai em `SHOW`.
- O hook realmente sobrescrito nos projetos costuma ser `listDependencies()`. O metodo `doListDependencies()` existe como wrapper interno em `AbstractAction`/`CrudActions`.
- `dynAction(MinhaDyn.class)` registra `SUCCESS` e `ERROR` para `fwIn("dyn/MinhaDynSemPrefixo.jsp")` por convencao.
- `ajaxAction(MinhaAjax.class)` registra `SUCCESS` e `ERROR` com renderer JSON padrao.
- O projeto real usa aliases e multiplos nomes em `action("Index,evento", Index.class)`; preserve esse estilo quando ja existir.
- `AuthenticationFilter` pode devolver `LOGIN`, `AJAX_DENIED` ou `DYN_LOGIN` conforme o tipo do fluxo.
- `GlobalFilterFree` pode impedir a aplicacao dos filtros globais.
- Se nenhuma consequence for registrada, o `DefaultConsequenceProvider` tenta fallback para `/modules/{action}[-{innerAction}].page`.

Em `ModuleManager`, prefira repetir a convencao local:

- `.on(SUCCESS, fwIn("Pagina.page"))` para pagina com template
- `.on(SUCCESS, fwIn("dyn/Fragmento.jsp"))` para fragmento
- `fwd("outroModulo", "Pagina.page")` para forward entre modulos
- `redir(...)` para redirect HTTP

## Assincronismo

Ha duas camadas diferentes e ambas aparecem no ecossistema:

- interfaces `AjaxAction` e `DynAction`
- anotacao `@AsynchronousAction(AsynchronousActionType.AJAX|DYN)` em type ou metodo

Nao remova uma so porque a outra parece redundante. Primeiro veja como aquele fluxo ja foi implementado no projeto alvo.

## Persistencia

Padrao preferencial do framework:

- DAO estende `HQLProvider`
- consultas em metodos estaticos
- CRUD via `Dao.getInstance()`

Exemplos de uso valido:

- `Dao.getInstance().get(...)`
- `Dao.getInstance().list(...)`
- `Dao.getInstance().uniqueResult(...)`
- `Dao.getInstance().paginationSlice(...)`
- `Dao.getInstance().save/update/delete(...)`

Use `HQLProvider.field(...)`, `ands(...)`, `ors(...)`, `hql(...)` e helpers do proprio ecossistema antes de inventar SQL manual.

### Alerta Importante de Transacao

O framework suporta `@Transactional`, `@NonTransactional` e `@MultiTransactional`, mas o projeto real tambem usa muito:

- `beginTransaction()`
- `commitTransaction()`
- `rollBackTransaction()`

inclusive em:

- Actions
- beans
- utils
- classes de migration
- endpoints REST

Por isso:

- nao converta transacao manual para anotacao automaticamente
- nao misture os dois estilos no mesmo fluxo sem entender o comportamento atual
- antes de alterar persistencia, procure no proprio arquivo e no fluxo chamador como a transacao ja esta sendo aberta

## Validacao e Erros

Padrao canonico:

- validator vigente: `org.futurepages.menta.core.validation.Validator`
- `error("campo", "mensagem")` para montar `validationMap`
- Action converte falha exibivel em `UserException`

Evite usar o pacote `org.futurepages.core.validation.*` para codigo novo de manutencao. Ele existe, mas ha partes deprecated e o glossario do framework marca o validator do pacote menta como o vigente.

## Views, JSP e TagLib

Ha dois fluxos principais:

- `.page`: pagina completa processada pelo `TemplateServlet`
- `.jsp`: fragmento direto, comum em `dyn/`, AJAX, PDF e saidas especificas

Distincao obrigatoria:

- **tags core do framework**: implementadas em Java e publicadas na taglib `futurepagesApp`
- **tag files da aplicacao**: arquivos `.tag` em `WEB-INF/tags/`, tambem expostos no namespace `fpg:` pelo projeto consumidor

Taglib principal:

```jsp
<%@ taglib uri="futurepagesApp" prefix="fpg" %>
```

Padroes reais observados em `convite_in_web`:

- `<fpg:page .../>` em JSPs completas
- `<fpg:importLayout .../>` para CSS/layout do modulo
- muitos fragments em `web/modules/{modulo}/dyn/`

Importante:

- em `convite_in_web`, tags como `<fpg:page>`, `<fpg:importLayout>`, `<fpg:ajaxSelect>`, `<fpg:paginationOnDemand>` e `<fpg:uploadImage>` sao **tag files do projeto**, nao API Java core do framework
- nao trate uma tag `fpg:*` usada no projeto como recurso nativo do framework sem verificar se ela vem de `WEB-INF/tags/`
- o `TemplateServlet` nao serve `.page` livremente fora de `/init/*` quando nao ha request associada a Action

Nao assuma que existe separacao moderna entre view-model e JSP. Muitas paginas dependem diretamente dos atributos enviados pela Action.

## Boot, Filtros e Inicializacao

Pontos chave:

- `ApplicationListener` coordena boot, schema generation, installers, migrations, Quartz, taglib e minificacao
- o `Controller` sobe separadamente e instancia `ApplicationManager`
- `ApplicationManager` registra o manager configurado em `INIT_MANAGER_CLASS` e os `ModuleManager`s
- `InitManager` registra filtros globais e consequencias globais

Nuances operacionais:

- `AutoRedirectDomainFilter` so entra se `AUTO_REDIRECT_DOMAIN` estiver configurado
- `HeadTitleFilter` so entra se `GLOBAL_HEAD_TITLE` estiver configurado
- `HibernateFilter` ou `ExceptionFilter` dependem de Hibernate estar ativo ou nao
- para problemas de startup, diferencie falha do listener/infra de falha do controller/managers/MVC
- para REST/Jersey, parte do bootstrap pode existir fora desse fluxo, no `web.xml` e na classe `javax.ws.rs.Application` do projeto consumidor

Nao altere boot, ordem de filtros, configuracao global ou parametros de inicializacao sem necessidade explicita. Isso e facil de quebrar e dificil de reverter.

## Migrations, Installers e Jobs

### Migrations

- ficam em `src/migration/versions/`
- nome no formato `V_{numero}_{subnumero}__descricao.sql|java`
- SQL e o caminho preferido; Java so quando a logica for necessaria
- se mexer em migration, respeite a numeracao e a ordem existentes

### Installers

- podem estender `Installer` ou implementar `Installation`
- ficam em `src/install/` ou `src/modules/*/install/`
- rodam na inicializacao conforme `INSTALL_MODE`

### Jobs Quartz

- ficam em `src/modules/*/jobs/`
- normalmente usam `@CronTrigger`
- no projeto exemplo, varios jobs estendem `LoggableJob`

Nao crie migration, installer ou job so porque a feature "parece pedir". Primeiro confirme se aquele tipo de mudanca ja faz parte do fluxo local.

## REST no Ecossistema

O framework oferece **blocos de construcao** para Jersey, mas o wiring concreto costuma ser decidido pelo projeto consumidor.

Nao presuma que o projeto alvo:

- usa `JerseyApiManager` pronto do framework
- escaneia `www.api`
- usa `www.Globals`
- usa markers `UserSessionProtected` / `RestTokenProtected`
- serializa tudo como JSON do mesmo jeito
- trata erro, CORS, multipart e autenticacao de forma uniforme

Exemplo observado em `convite_in_web`:

- o servlet REST e mapeado em `/rest/*` no `web.xml`
- a classe configurada e `jersey.RestApp`
- o projeto usa `ResourceConfig` propria com `packages("rest")`
- providers como `JsonWithGsonMapper`, `CORSFilter`, `AppExceptionMapper` e `MultiPartFeature` sao registrados explicitamente no projeto
- os endpoints vivem em `rest/**`, `rest/mobile/**` e `rest/deprecated/**`
- ha contratos heterogeneos: `Response`, JSON e respostas textuais legadas
- ha muito uso de transacao manual e rollback defensivo nos endpoints

### O Que Inspecionar Antes de Alterar REST

1. `web.xml`
   - qual servlet atende REST
   - qual `url-pattern` esta exposto
   - qual `javax.ws.rs.Application` foi configurada
   - se SSE ou outros servlets Jersey estao ativos, desativados ou comentados

2. bootstrap Jersey do projeto
   - qual `ResourceConfig` esta em uso
   - quais `packages(...)` sao escaneados
   - quais `register(...)` habilitam providers, mappers, filters e features

3. endpoints e contracts existentes
   - packages reais (`rest`, `rest/mobile`, `rest/deprecated`, `sse`)
   - convencao de versionamento dos `@Path`s
   - coexistencia de endpoints antigos e novos
   - formato de resposta esperado pelo cliente

4. seguranca e autorizacao
   - markers Jersey, se houver
   - filtros/interceptors Jersey registrados
   - validacao/autorizacao inline no endpoint
   - validacao/autorizacao delegada a services/helpers

5. serializacao e erro
   - provider JSON real
   - uso de `Response` com status HTTP
   - exception mappers registrados
   - endpoints que retornam protocolos textuais legados

6. transacao
   - abertura manual no endpoint
   - rollback defensivo em `catch`
   - transacao iniciada em services/utilitarios chamados pelo endpoint

7. relacao MVC/REST
   - se a funcionalidade tambem existe em `Action`s MVC
   - se ha reutilizacao de `Service`, `Validator` e `Bean`
   - se os contratos REST espelham ou divergem das rotas MVC

Regra final: **nao extrapole a partir das Actions MVC para endpoints REST sem verificar o wiring real do projeto alvo**.

## Estrategia de Manutencao

Para quase toda tarefa, siga esta ordem:

1. localizar o modulo dono do fluxo
2. localizar a Action ou endpoint equivalente
3. localizar a JSP/fragmento mapeado no `ModuleManager`
4. localizar DAO, Service, Validator e filtros relacionados
5. repetir o padrao local com a menor alteracao possivel
6. validar se ha impacto em installer, migration, job ou parametro de `app-params.xml`

Se a tarefa for REST/Jersey, ajuste a ordem para:

1. localizar o `url-pattern` REST no `web.xml`
2. localizar a classe `javax.ws.rs.Application` / `ResourceConfig`
3. localizar o endpoint e o package realmente escaneado
4. localizar providers, mappers, filtros e features registrados
5. localizar services, validators e transacoes usados pelo endpoint
6. repetir o padrao local com a menor alteracao possivel

## Checklist Antes de Entregar

- usei nomes e conceitos do glossario do Futurepages4
- preservei o estilo do modulo existente
- nao introduzi framework ou abstracao nova
- conferi o mapeamento no `ModuleManager`
- conferi o tipo de transacao ja usado naquele fluxo
- conferi se a view e `.page` ou `.jsp`
- nao troquei validator vigente por pacote deprecated
- nao alterei boot/config global sem necessidade explicita

Se toquei em REST/Jersey:

- confirmei o `web.xml` e a classe `javax.ws.rs.Application`
- confirmei o package realmente escaneado pelo projeto
- confirmei providers e mappers ativos
- confirmei a estrategia de autenticacao/autorizacao real
- confirmei o formato de resposta e o contrato legado do endpoint

## Referencias Rapidas

- Glossario: `futurepages4/CONTEXT.md`
- Indice: `futurepages4/docs/README.md`
- Actions: `futurepages4/docs/guides/ACTION_PATTERNS.md`
- Persistencia: `futurepages4/docs/guides/PERSISTENCE.md`
- Views: `futurepages4/docs/guides/VIEWS.md`
- Boot: `futurepages4/docs/guides/BOOT.md`
- Migrations: `futurepages4/docs/guides/MIGRATIONS.md`
- Installers: `futurepages4/docs/guides/INSTALLERS.md`
- Estrutura de modulo: `futurepages4/docs/modules/OVERVIEW.md`
- Projeto real de referencia: `convite_in_web/`

## Leitura Final para o Agente

Se voce estiver em duvida entre "usar a forma ideal do framework" e "copiar o jeito que esse projeto funciona hoje", escolha a segunda opcao, exceto quando houver bug claro, quebra de contrato ou instrucao explicita em contrario.
