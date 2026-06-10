# Fluxo de Requisição

## Ciclo de Vida Completo

```
HTTP Request
    │
    ▼
[web.xml] Filtros/servlets do container
    │
    ▼
[Controller.service()] — Front-controller
    │
    ├─ 1. fixEncoding(request, response)
    │      → usa `PAGE_ENCODING`
    │
    ├─ 2. appManager.service(appContext, req, res)
    │      → hook de pre-processamento do manager
    │
    ├─ 3. Parse da URL → extrai módulo, nome da action, inner action
    │      Ex: /admin/User-explore?nome=Joao
    │      → módulo: admin
    │      → action: User
    │      → innerAction: explore
    │      → parâmetros: nome=Joao
    │
    ├─ 4. Resolve ActionConfig
    │      → tenta primeiro (actionName + innerAction)
    │      → depois apenas actionName
    │      → se não achar, tenta defaultAction
    │
    ├─ 5. Instancia a Action
    │      → via ActionConfig.getAction()
    │
    ├─ 6. Prepara a Action:
    │      setInput(PrettyURLRequestInput)
    │      setOutput(ResponseOutput)
    │      setSession(SessionContext)
    │      setApplication(ApplicationContext)
    │      setCookies(CookieContext)
    │      setLocale(LocaleManager)
    │      setCallback(MapContext)
    │
    ├─ 7. Cria InvocationChain (Action + Filters)
    │      Ordem dos filtros:
    │        1. FirstFilters (da ActionConfig)
    │        2. GlobalFilters (non-last)
    │        3. ActionFilters
    │        4. GlobalFiltersLast
    │      Observação:
    │        `GlobalFilterFree` pode impedir globais
    │        já `InjectionFilter`/`OutjectionFilter` globais são forçados para o fim
    │
    ├─ 8. InvocationChain.invoke()
    │      → cada Filter chama chain.invoke()
    │
    ├─ 9. Action.execute() ou innerAction via reflexão
    │      → se innerAction for null, executa `execute()`
    │
    ├─ 10. Action retorna String de resultado
    │       → se método retorna void, resultado = SUCCESS
    │       → se retorna null, resultado = NULL
    │
    ├─ 11. Resolve Consequence (prioridade):
    │       1. Inner action-specific
    │       2. Action-specific
    │       3. Global
    │       4. DefaultConsequenceProvider (convenção)
    │          → fallback para `/modules/{action}[-{innerAction}].page`
    │
    ├─ 12. Consequence.execute() — Forward/Redirect/Chain/Stream/Ajax
    │
    └─ 13. AfterConsequenceFilters rodam
           → ex: `HibernateFilter.afterConsequence()` fecha sessões

HTTP Response
```

## Módulos e Resolução de URL

A URL segue o padrão: `/{modulo}/{Action}[-{innerAction}]`

O separador real de inner action no `Controller` e `-`.

O módulo é identificado pelo primeiro segmento da URL, que deve estar mapeado no `web.xml`:

```xml
<servlet-mapping>
    <servlet-name>Controller</servlet-name>
    <url-pattern>/admin/*</url-pattern>
</servlet-mapping>
```

## Views

| Tipo | Extensão | Processador | Uso |
|------|----------|-------------|-----|
| Página completa | `.page` | TemplateServlet | Renderiza JSP + template layout |
| Fragmento AJAX | `.jsp` | Controller (Forward direto) | Conteúdo sem template |
| Conteúdo especial | `.jsp` | Controller | PDF, XML, etc. sem template |

### Fluxo com TemplateServlet (.page)

```
Action → Forward("/modules/{modulo}/{pagina}.page")
    ↓
TemplateServlet processa a extensão .page
    ↓
Lê app-template.xml para escolher layout (por regex)
    ↓
Renderiza JSP como corpo + layout escolhido
    ↓
Layout define <fpg:block id="body"/> para o conteúdo
```

## Observacoes de Manutencao

- O `TemplateServlet` rejeita acesso direto a `.page` quando nao ha Action associada, exceto no contexto especial de `/init/*`.
- Em producao, `AUTO_REDIRECT_DOMAIN` altera a estrategia de `Paths` usada pelo `Controller`.
- Para endpoints REST Jersey, este fluxo **nao** se aplica; consulte `guides/REST.md`.
- Em projetos consumidores, o fluxo REST costuma ser definido separadamente no `web.xml` por um servlet Jersey proprio, como `/rest/*`.
