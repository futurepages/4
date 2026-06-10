# Views e TagLib

## Dois Sistemas de View

Este guia mistura dois niveis diferentes que precisam ficar separados:

- **tags/classes core do framework**, definidas em Java e publicadas na taglib `futurepagesApp`
- **tag files da aplicacao**, geradas a partir de `WEB-INF/tags/**/*.tag` e incorporadas ao mesmo namespace `fpg:`

Em manutencao, nao assuma que toda tag `fpg:*` usada em um projeto pertence ao core do framework.

### 1. Páginas com Template (.page)

Para páginas completas com layout definido pelo `app-template.xml`:

```java
// ModuleManager
action("Index", IndexAction.class)
    .on(SUCCESS, fwIn("Index.page"));
```

O `TemplateServlet` processa a extensão `.page`:
1. Renderiza o JSP correspondente
2. Envolve no layout definido pela regex no `app-template.xml`
3. O layout usa `<fpg:block id="body"/>` para inserir o conteúdo

### 2. Fragmentos sem Template (.jsp)

Para fragments AJAX, PDF, XML:

```java
action("User", UserActions.class)
    .on(CREATE, fwIn("dyn/User-create.jsp"))
    .on(UPDATE, fwIn("dyn/User-update.jsp"));
```

São servidos diretamente pelo Controller (sem TemplateServlet).

## TemplateServlet — app-template.xml

```xml
<app-template-configuration>
    <page rule=".*_pdf" base="template/layout_pdf.jsp"/>
    <page rule=".*_print" base="template/layout_print.jsp"/>
    <page rule=".*_xls" base="template/layout_xls.jsp"/>
    <page rule=".*" base="template/layout.jsp"/>
</app-template-configuration>
```

O `rule` é uma regex aplicada ao nome da página. O primeiro match define o layout.

Observacoes importantes do codigo real:

- `app-template.xml` e lido do diretorio `conf/` em classes/resources
- cada entrada pode usar `rule` (regex) **ou** `path` (match exato)
- o `TemplateServlet` so serve `.page` livremente para rotas de `/init/*`; fora disso, espera uma request associada a Action

## TagLib — futurepagesApp (prefixo `fpg:`)

Declarada no JSP como:
```jsp
<%@ taglib uri="futurepagesApp" prefix="fpg"%>
```

### Tags de Configuração de Página

As tags abaixo sao **core** do framework e foram verificadas no codigo Java:

| Tag | Descrição |
|-----|-----------|
| `<fpg:head>` | Define meta tags, CSS, título |
| `<fpg:footer>` | Adicionar scripts ao final da página |
| `<fpg:webContainer>` | Container head/footer |

### Tags de Dados

| Tag | Descrição |
|-----|-----------|
| `<fpg:set var="nome" value="${valor}"/>` | Define variável no escopo |
| `<fpg:list var="item" items="${lista}">...` | Itera sobre coleção |

### Tags Condicionais

| Tag | Descrição |
|-----|-----------|
| `<fpg:if value="${condicao}">...` | Condicional |
| `<fpg:isEmpty value="${lista}">` | Verifica se vazio |
| `<fpg:hasError>` | Se há erro na action |
| `<fpg:hasSuccess>` | Se há sucesso na action |

### Tags de Path

| Tag | Descrição |
|-----|-----------|
| `<fpg:modulePath module="admin"/>` | Caminho do módulo |
| `<fpg:contextPath/>` | Context path |
| `<fpg:templatePath/>` | Caminho do template |
| `<fpg:resourcePath/>` | Caminho de recursos |

### Tags de Formulário

Tags core verificadas no framework:

| Tag | Descrição |
|-----|-----------|
| `<fpg:Select name="campo" list="${lista}" selected="${selecionado}"/>` | Combo box |

### Tags de Exibição

Tags core verificadas no framework:

| Tag | Descrição |
|-----|-----------|
| `<fpg:error/>` | Exibe mensagem de erro |
| `<fpg:success/>` | Exibe mensagem de sucesso |
| `<fpg:dateTime value="${data}" type="DATE"/>` | Formata data |
| `<fpg:valueFormatter value="${valor}" formatter="PhoneFormatter"/>` | Aplica formatador |
| `<fpg:pagination ...>` | Navegação paginada |
| `<fpg:pluralOrSingular count="${n}" singular="item" plural="itens"/>` | Singular/plural |

### Tag Files de Aplicacao

Projetos consumidores podem expor tags extras no mesmo namespace `fpg:` por meio de `WEB-INF/tags/**/*.tag`.

Exemplos observados em `convite_in_web`:

- `<fpg:page .../>`
- `<fpg:importLayout .../>`
- `<fpg:ajaxSelect .../>`
- `<fpg:paginationOnDemand .../>`
- `<fpg:uploadImage .../>`

Essas tags sao reais no projeto exemplo, mas **nao fazem parte do core Java do framework**. Sao conveniencias de aplicacao distribuida via tag files.

## Acesso a Atributos na View

Atributos setados na Action com:
```java
output("objects", lista);
output("entity", objeto);
```

Ficam disponíveis na JSP como `${objects}`, `${entity}`.

## Alertas de Manutencao

- Se uma tag aparece no projeto mas nao no core Java do framework, procure primeiro em `WEB-INF/tags/` do projeto consumidor.
- Nao documente como "API do framework" uma tag validada apenas no projeto exemplo.
- O namespace `futurepagesApp` agrega tanto tags Java do framework quanto tag files da aplicacao.
