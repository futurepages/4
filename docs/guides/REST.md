# REST API (Jersey)

## Escopo

O Futurepages4 possui uma camada REST baseada em Jersey. Ela **coexiste** com o MVC classico (`Controller` + `Action`) e nao o substitui.

Em manutencao legada, trate REST como um subsistema proprio:

- classes REST vivem fora de `modules/*/actions`
- o fluxo de seguranca e transacao pode diferir do MVC classico
- boa parte do wiring depende do projeto consumidor

Regra central deste guia:

- o framework fornece **blocos de construcao** para Jersey
- o projeto consumidor decide **como esses blocos serao ligados**
- antes de alterar REST, confirme o wiring no projeto alvo

## Componentes Principais

### Managers Jersey

O framework oferece `ResourceConfig` prontos:

- `org.futurepages.jersey.core.JerseyApiManager`
- `org.futurepages.jersey.core.JerseyAppManager`
- `org.futurepages.jersey.core.JerseySseManager`

Exemplo observado no framework:

```java
public class JerseyApiManager extends ResourceConfig {
    public JerseyApiManager() {
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        packages("www.api");
        register(GlobalFilters.Api.class);
        register(AppExceptionMapper.class);
        register(CORSFilter.class);
        register(JsonWithGsonMapper.class);
    }
}
```

Ponto importante: os pacotes escaneados (`www.api`, `www.app`, etc.) sao definidos pelo manager e podem variar por projeto.

Isso e apenas uma opcao de uso do framework, nao uma obrigacao.

## Onde o Wiring Realmente Mora

No ecossistema Futurepages4, o wiring REST/Jersey costuma nascer da combinacao de:

1. `web.xml` do projeto consumidor
2. classe `javax.ws.rs.Application` configurada no servlet Jersey
3. `ResourceConfig` concreto (`packages(...)`, `register(...)`)
4. packages reais onde os endpoints vivem
5. providers, filters, mappers e features registrados

Ou seja: o framework nao garante sozinho uma topologia unica para REST.

## Exemplo Concreto: `convite_in_web`

Em `convite_in_web`, o wiring observado e este:

### 1. Mapeamento do servlet no `web.xml`

```xml
<servlet>
    <servlet-name>jerseyAPI</servlet-name>
    <servlet-class>org.futurepages.jersey.core.JerseyController</servlet-class>
    <init-param>
        <param-name>javax.ws.rs.Application</param-name>
        <param-value>jersey.RestApp</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>jerseyAPI</servlet-name>
    <url-pattern>/rest/*</url-pattern>
</servlet-mapping>
```

Conclusao: as rotas REST nao sao descobertas por `ModuleManager`. Elas entram pela arvore `/rest/*`.

### 2. Classe de bootstrap Jersey propria do projeto

```java
public class RestApp extends ResourceConfig {
    public RestApp() {
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        packages("rest");
        register(MultiPartFeature.class);
        register(AppExceptionMapper.class);
        register(CORSFilter.class);
        register(JsonWithGsonMapper.class);
    }
}
```

Conclusoes importantes:

- o projeto **nao** usa `JerseyApiManager` pronto do framework
- o pacote escaneado e `rest`, nao `www.api`
- os providers sao registrados explicitamente no projeto
- multipart esta habilitado no projeto, nao por padrao universal do framework

### 3. Endpoint packages e contratos reais

No exemplo, os endpoints vivem em packages como:

- `src/rest/**`
- `src/rest/mobile/**`
- `src/rest/deprecated/**`
- `src/sse/**` para SSE quando aplicavel

Os `@Path`s sao versionados e heterogeneos, por exemplo:

- `/evento.v6`
- `/login.v6`
- `/mobile-login.v0`
- `/site-presentes.v2`
- `/whatsapp/messages/status`

Isso reforca que versao, agrupamento de paths e formato de contrato sao decisoes do projeto consumidor.

### 4. Providers e serializacao

No exemplo, o projeto registra explicitamente:

- `JsonWithGsonMapper`
- `CORSFilter`
- `AppExceptionMapper`
- `MultiPartFeature`

Portanto, antes de assumir JSON, CORS, multipart ou mapeamento padrao de erro, confirme se esses providers realmente estao ativos no projeto alvo.

### 5. Seguranca e autorizacao

No exemplo analisado:

- nao apareceu um uso claro dos markers `UserSessionProtected` e `RestTokenProtected`
- varios endpoints fazem validacao/autorizacao dentro do proprio metodo ou via services/helpers
- a estrategia de seguranca REST nao replica automaticamente `AuthenticationFilter` + `ModulePermissionFilter`

Conclusao: em REST, protecao pode ser feita por marker, filtro Jersey, helper local, validacao inline ou combinacao deles. Inspecione o projeto antes de assumir qualquer padrao.

### 6. Transacao e erro

No exemplo, ha uso recorrente de:

- `beginTransaction()` / `commitTransaction()`
- `if (Dao.getInstance().isTransactionActive()) rollBackTransaction()` em `catch`
- respostas heterogeneas: `Response`, JSON, strings delimitadas, codigos HTTP especificos

Conclusao: o projeto consumidor define boa parte da politica operacional de erro, serializacao e rollback.

### JerseyController

`JerseyController` e o servlet que envolve o `ServletContainer` do Jersey.

Responsabilidades relevantes:

- delegar a request ao Jersey
- ajustar encoding quando `NEW_JERSEY_MODE=true`
- garantir `HibernateFilter.finallly()` ao final da request

Isso significa que a camada REST compartilha parte da infraestrutura Hibernate, mas **nao replica exatamente** o pipeline de filtros do MVC classico.

### JerseyAction

Muitos endpoints do ecossistema estendem `JerseyAction`:

```java
@Path("/evento.v6")
public class EventoRest extends JerseyAction {
}
```

`JerseyAction` oferece:

- `action`, `getInput()`, `getOutput()`
- `output(key, value)`
- `jsp(path)` para `Viewable`
- `redir(path)` e `redir_out(path)`
- helpers `success(obj)` e `error(msgs)`

Ela reaproveita uma `NullAction` internamente para expor partes do contrato MVC na camada REST.

## Seguranca

### Markers da camada Jersey

Existem markers especificos:

- `UserSessionProtected`
- `RestTokenProtected`

Esses markers **nao bastam sozinhos** para proteger um endpoint. O comportamento efetivo depende do wiring do projeto, normalmente via `GlobalFilters`/`www.Globals` e filtros registrados no `ResourceConfig`.

No exemplo `convite_in_web`, o wiring observado passa por `web.xml` + `jersey.RestApp`, sem evidencia de um `www.Globals` exercendo esse papel. Isso mostra que nem mesmo esse ponto e uniforme entre projetos.

Regra pratica:

1. confirme quais filtros globais o projeto registra
2. veja se o endpoint estende `JerseyAction`
3. confira como sessao, token e callback de erro sao tratados naquele projeto

Nao extrapole a partir do `AuthenticationFilter` MVC.

## Transacoes

Em REST, o ecossistema costuma usar uma combinacao de:

- fechamento de sessao no `JerseyController`
- transacoes manuais com `Dao.getInstance().beginTransaction()`
- rollback defensivo em `catch`

Exemplo observado em `convite_in_web`:

```java
Dao.getInstance().beginTransaction();
try {
    EventoServices.newFromPublicForm(evento);
    Dao.getInstance().commitTransaction();
} catch (Exception ex) {
    if (Dao.getInstance().isTransactionActive()) {
        Dao.getInstance().rollBackTransaction();
    }
    throw ex;
}
```

Nao presuma que `@Transactional` esta cuidando de tudo na camada REST.

## Padroes Reais em `convite_in_web`

Observacoes uteis para manutencao:

- endpoints usam `@Path` versionado, como `login.v6`, `evento.v6`, `mobile-login.v0`
- respostas podem ser JSON, texto simples ou protocolos legados baseados em strings delimitadas
- `JerseyAction` e usado como classe base em varios endpoints
- validacao e feita tanto com validators do ecossistema quanto com validacoes inline
- rollback defensivo aparece com frequencia em `catch`
- bootstrap Jersey e definido pelo proprio projeto em `jersey.RestApp`
- o servlet REST esta mapeado em `/rest/*` no `web.xml`

Isso mostra que a camada REST e **operacionalmente heterogenea**. Preserve o contrato ja exposto pelo endpoint antes de tentar uniformizar respostas.

## O Que Inspecionar no Projeto Consumidor Antes de Alterar REST

1. `web.xml`
   - qual servlet atende REST
   - qual `url-pattern` esta exposto
   - qual `javax.ws.rs.Application` foi configurada
   - se SSE ou outros servlets Jersey estao ativos ou comentados

2. classe de bootstrap Jersey do projeto
   - `ResourceConfig` usada de fato
   - `packages(...)` registrados
   - `register(...)` de providers, features, mappers e filtros

3. endpoints e paths ja expostos
   - packages reais (`rest`, `rest/mobile`, `rest/deprecated`, `sse`)
   - convencao de versionamento (`.v0`, `.v2`, `.v6`, etc.)
   - coexistencia de endpoints antigos/deprecated com novos

4. relacao entre MVC e REST
   - se a funcionalidade tambem existe em Actions MVC
   - se ha redirects, JSP/Viewable ou reutilizacao de services compartilhados
   - se contratos REST espelham ou divergem das rotas MVC

5. autenticacao e autorizacao
   - markers Jersey, se houver
   - filtros/interceptors Jersey registrados
   - validacao inline no endpoint
   - verificacoes em services/helpers chamados pelo endpoint

6. serializacao e tratamento de erro
   - provider JSON real (ex: Gson mapper)
   - uso de `Response` com status HTTP
   - endpoints que retornam texto ou protocolos legados
   - exception mappers registrados

7. transacao
   - abertura manual de transacao no endpoint
   - rollback defensivo em `catch`
   - transacao iniciada em services/utilitarios chamados

8. multipart, SSE e features opcionais
   - `MultiPartFeature`
   - `SseFeature`
   - CORS e outros providers transversais

## Checklist Rápido de Manutencao REST

- confirme o `url-pattern` REST no `web.xml`
- confirme a classe `Application`/`ResourceConfig` efetivamente usada
- confirme o package onde o endpoint e descoberto
- confirme se o endpoint estende `JerseyAction`
- confirme o formato real de resposta esperado pelo cliente
- confirme onde a autenticacao/autorizacao esta sendo aplicada
- confirme providers de serializacao e exception mappers ativos
- confirme se ha transacao manual no endpoint ou no fluxo chamado
- nao converter endpoint legado textual para JSON sem necessidade explicita

## Relacao com o MVC Classico

Pontos em comum:

- compartilha Hibernate/Dao
- pode reaproveitar services, validators e beans
- pode redirecionar para JSP/Viewable quando o projeto faz esse tipo de composicao

Pontos diferentes:

- nao passa pelo `Controller` MVC tradicional
- nao usa `ModuleManager` para mapear endpoints
- seguranca, packages, providers e wiring dependem do setup Jersey do projeto consumidor

## Quando Desconfiar

Desconfie de qualquer mudanca que:

- troque o formato de resposta de um endpoint legado
- remova rollback manual sem provar quem fecha a transacao
- assuma que marker Jersey protege sozinho
- aplique padroes de `Action` MVC sem conferir o wiring REST
- assuma que o exemplo `convite_in_web` representa obrigatoriamente todos os projetos Futurepages4
