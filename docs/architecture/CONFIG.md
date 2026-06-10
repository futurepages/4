# Configuração

## app-params.xml

Arquivo principal de configuração da aplicação, carregado pelo `Apps.init()`:

```xml
<app-params>
    <!-- Aplicação -->
    <param name="APP_NAME" value="meuapp"/>
    <param name="APP_HOST" value="https://meuapp.com.br"/>
    <param name="APP_LOCAL_HOST" value="http://localhost:8080"/>
    <param name="APP_CONTEXT_PATH" value="/meuapp"/>
    <param name="APP_BUILD_ID" value="1.0.0"/>
    <param name="APP_DATA_MODEL_CLASS" value="modules.sistema.beans.DataModel"/>
    <param name="PAGE_ENCODING" value="UTF-8"/>
    <param name="DEPLOY_MODE" value="development"/>

    <!-- Schema -->
    <param name="SCHEMA_GENERATION_TYPE" value="none"/>  <!-- none / update / export -->

    <!-- Instalação -->
    <param name="INSTALL_MODE" value="off"/>  <!-- off / on / modules / production / examples -->

    <!-- Jobs -->
    <param name="QUARTZ_MODE" value="off"/>  <!-- on / off -->

    <!-- Email -->
    <param name="EMAIL_ACTIVE" value="false"/>
    <param name="EMAIL_HOST" value="smtp.example.com"/>
    <param name="EMAIL_USER" value="user@example.com"/>
    <param name="EMAIL_PASS" value="senha"/>
    <param name="EMAIL_FROM" value="noreply@example.com"/>

    <!-- Upload -->
    <param name="UPLOADS_PATH" value="/opt/uploads"/>

    <!-- Redirect / domínio -->
    <param name="AUTO_REDIRECT_DOMAIN" value="https://meuapp.com.br"/>
    <param name="LOGIN_URL_REDIRECT" value="/login"/>
    <param name="LOGIN_URL_REDIRECT_VAR_NAME" value="next"/>

    <!-- TagLib -->
    <param name="GENERATE_TAGLIB" value="true"/>

    <!-- Recursos -->
    <param name="MINIFY_RESOURCE_MODE" value="none"/> <!-- none / js / css / both -->

    <!-- Action Inicial -->
    <param name="INIT_ACTION" value="init.Index"/>
    <param name="START_PAGE_NAME" value="Index"/>
    <param name="START_CONSEQUENCE" value="/Index"/>

    <!-- InitManager customizado -->
    <param name="INIT_MANAGER_CLASS" value="org.futurepages.menta.core.InitManager"/>

    <!-- Módulos -->
    <param name="MODULES_CLASSES_REAL_PATH" value="/WEB-INF/classes/modules"/>
</app-params>
```

Acessado em código via:
```java
Apps.get("APP_NAME");
Apps.get("APP_HOST");
Apps.devMode();  // boolean: true se está em desenvolvimento
```

## hibernate.properties

```properties
hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
hibernate.connection.datasource=java:/comp/env/jdbc/convitin_dev
hibernate.hbm2ddl.auto=validate
hibernate.show_sql=false
hibernate.format_sql=true
```

## web.xml

Configura o `Controller`, `TemplateServlet`, `ApplicationListener`, filtros e mapeamentos de URL. Ver [REQUEST_FLOW.md](REQUEST_FLOW.md) para detalhes.

## Parametros Relevantes para Manutencao

- `PAGE_ENCODING`: usado no boot e tambem na camada Jersey quando `NEW_JERSEY_MODE=true`
- `DEPLOY_MODE`: influencia schema generation, installers, minificacao e comportamento de producao
- `AUTO_REDIRECT_DOMAIN`: altera o modo de resolucao de paths e pode melhorar performance em producao
- `MINIFY_RESOURCE_MODE`: dispara `ResourceMinifier` no startup
- `LOGIN_URL_REDIRECT` e `LOGIN_URL_REDIRECT_VAR_NAME`: afetam o `AuthenticationFilter`
