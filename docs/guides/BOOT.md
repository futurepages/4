# Inicialização da Aplicação

## Ordem de Boot

O boot é coordenado pelo `ApplicationListener` (`ServletContextListener` configurado no `web.xml`):

```
1. Apps.init(contextName)
   ├─ Carrega app-params.xml
   └─ Inicializa parâmetros da aplicação

2. `System.setProperty("file.encoding", Apps.get("PAGE_ENCODING"))`
   e `sun.jnu.encoding`

3. AppLogger.getInstance().init()

4. MailConfig.initialize()
   └─ Se EMAIL_ACTIVE=true, configura SMTP

5. Leitura dos módulos
   └─ Escaneia MODULES_CLASSES_REAL_PATH

6. SchemaGeneration
   └─ Se SCHEMA_GENERATION_TYPE != none e não produção
      ├─ update → atualiza schema via Hibernate
      └─ export → gera SQL DDL

7. InstallersManager.initialize(modules, installMode)
   └─ Se `INSTALL_MODE` nao for `off`, `none` nem vazio

8. [Produção] Micro-migração via classe `MIGRATION_CLASSPATH`

9. DataModelMigrationController.execute()
   └─ Executa migrações versionadas

10. QuartzManager.initialize(modules)
    └─ Se QUARTZ_MODE=on

11. TagLibBuilder
    └─ Se GENERATE_TAGLIB=true, gera taglib.tld

12. ResourceMinifier
    └─ Se MINIFY_RESOURCE_MODE != none, minifica JS/CSS

13. Thread de fallback do Controller
    └─ So tenta chamar a start page se o `Controller` ainda nao tiver sido inicializado
```

## Ciclo entre Listener, Controller e Managers

O ponto mais facil de interpretar errado e este:

1. `ApplicationListener` prepara ambiente, banco, installers, migrations, Quartz, taglib e recursos
2. o servlet `Controller` sobe e instancia `ApplicationManager`
3. `ApplicationManager.registerManagers()` registra o manager configurado em `INIT_MANAGER_CLASS` e os `ModuleManager`s
4. so entao rodam `init()`, `loadLocales()`, `loadActions()`, `registerChains()` e `loadFormatters()`

Ou seja: o `InitManager` nao nasce diretamente no `ApplicationListener`; ele e registrado dentro do `ApplicationManager` durante a inicializacao do `Controller`.

## InitManager

O `InitManager` e o manager configurado por `INIT_MANAGER_CLASS` e normalmente cuida de filtros globais, consequences globais e action inicial:

1. Registra filtros globais (na ordem):
   - `AutoRedirectDomainFilter` se `AUTO_REDIRECT_DOMAIN` estiver configurado
   - `HibernateFilter` (ou `ExceptionFilter` se Hibernate off)
   - `HeadTitleFilter` se `GLOBAL_HEAD_TITLE` estiver configurado
   - `FileUploadFilter`
   - `InjectionFilter`

2. Mapeia consequências globais:
   - `NULL` → `NullConsequence`
   - `EXCEPTION` → `Forward`
   - `NOT_FOUND` → `Forward`
   - `DYN_EXCEPTION` → `Forward`
   - `STRING` → `StringConsequence`
   - `REDIR` → `Redirect`
   - `AJAX_REDIR`, `AJAX_ERROR`, `AJAX_SUCCESS` → renderer JSON
   - `REDIR_APPEND_OUTPUT` → redirect preservando output

3. Registra a Action Inicial (`INIT_ACTION` → `START_PAGE_NAME`)

## ApplicationManager

O `ApplicationManager` (multi-manager padrão) faz:
1. Instancia o `InitManager` (padrão ou customizado via `INIT_MANAGER_CLASS`)
2. Escaneia módulos e instancia os `ModuleManager` de cada um
3. Cada `ModuleManager.loadActions()` registra suas próprias actions
4. Depois carrega locales, actions, chains e formatters

## Configurações Relevantes (app-params.xml)

```xml
<param name="INIT_ACTION" value="init.Index"/>
<param name="START_PAGE_NAME" value="Index"/>
<param name="START_CONSEQUENCE" value="/Index"/>
<param name="INIT_MANAGER_CLASS" value="org.futurepages.menta.core.InitManager"/>
<param name="GENERATE_TAGLIB" value="true"/>
```

## Alertas de Manutencao

- Nao trate `ApplicationListener` e `Controller` como um unico boot linear; eles se complementam em momentos diferentes do startup.
- Mudancas em `INIT_MANAGER_CLASS`, `START_PAGE_NAME`, `AUTO_REDIRECT_DOMAIN`, `PAGE_ENCODING` ou `DEPLOY_MODE` tendem a ter impacto transversal.
- Em problemas de startup, confirme primeiro se a falha veio do listener (infra) ou do controller/managers (MVC).
- Para Jersey/REST, confirme tambem o wiring adicional do projeto consumidor em `web.xml` e na classe `javax.ws.rs.Application`; essa parte pode existir fora do boot MVC classico.
