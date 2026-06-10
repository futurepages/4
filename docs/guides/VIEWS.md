# Views e TagLib

## Dois Sistemas de View

Este guia mistura dois niveis diferentes que precisam ficar separados:

- **tags/classes core do framework**, definidas em Java e publicadas na taglib `futurepagesApp`
- **tag files da aplicacao**, geradas a partir de `WEB-INF/tags/**/*.tag` e incorporadas ao mesmo namespace `fpg:`

Em manutencao, nao assuma que toda tag `fpg:*` usada em um projeto pertence ao core do framework.

**Exemplos práticos:**

| Framework Core | Aplicação (alunoonline) | Aplicação (convite_in_web) |
|---|---|---|
| `<fpg:contextPath/>` | Usada em links | Usada em links e AJAX |
| `<fpg:Select>` | Usada em forms | Raramente (usa custom tags) |
| `<fpg:hasSuccess/>` | Usada para alertas | Usa `<fpg:page design="...">` |
| — | `<fpg:aluno>` (tag file simples) | `<fpg:page>`, `<fpg:importLayout>`, `<fpg:ajaxSelect>` (tag files complexas) |

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

O `TemplateServlet` (mapeado para `.page`) aplica um layout template a uma página de conteúdo. O arquivo `app-template.xml` define as regras de encaixe.

### Exemplo: alunoonline (simples)

```xml
<?xml version='1.0' encoding='utf-8'?>
<app-template-configuration>
    <page rule="^.*$" base="template/layout.jsp">
        <block id="cabecalho"   value="template/cabecalho.jsp"/>
        <block id="rodape"      value="template/rodape.jsp"/>
    </page>
</app-template-configuration>
```

Layout correspondente (`template/layout.jsp`):

```jsp
<%@ taglib uri="futurepagesApp" prefix="fpg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" ...>
<html>
    <head>
        <title>Aluno Online QuickStart Application</title>
        <!-- Estilos CDN simples -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"/>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <fpg:block id="cabecalho"/>
        <hr/>
        <fpg:block id="body"/>  <!-- Conteúdo da página .page -->
        <hr/>
        <fpg:block id="rodape"/>
    </body>
</html>
```

O elemento `<fpg:block id="body"/>` é **obrigatório** — é onde o conteúdo da página é inserido.

### Exemplo: convite_in_web (complexo com múltiplos designs)

Em projetos mais sofisticados, o layout pode variar por "design" (Bootstrap 3, MDL, etc.):

```jsp
<%@ taglib uri="futurepagesApp" prefix="fpg"%>
<%--@elvariable id="params" type="java.util.Map"--%>

<fpg:set var="CORE_RELEASE_QUERY" value="?coreRelease=2022.01.01"/>
<fpg:set var="templatePath"><fpg:templatePath/></fpg:set>

<!-- Design selection via conditional logic -->
<fpg:if value="${design=='bootstrap'}" context="true">
    <fpg:then>
        <fpg:head>
            <link type="text/css" href="${templatePath}/core/embeded/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet"/>
        </fpg:head>
        <fpg:footer>
            <script src="${templatePath}/core/embeded/jquery/1.11.2/jquery.min.js${CORE_RELEASE_QUERY}"></script>
            <script src="${templatePath}/core/embeded/bootstrap/3.3.4/js/bootstrap.min.js${CORE_RELEASE_QUERY}"></script>
            <script src="${templatePath}/core/embeded/futurepages/2.0/futurepages.js${CORE_RELEASE_QUERY}"></script>
        </fpg:footer>
    </fpg:then>
    <!-- Outras variantes: bootstrap3, mdl, none -->
</fpg:if>
```

### Observações importantes

- **Localização:** `app-template.xml` é lido de `src/conf/` (fica em `classes/` após build)
- **Regras:** cada `<page>` pode usar `rule` (regex) **ou** `path` (match exato)
- **Precedência:** o primeiro match define o layout; **regras são processadas em ordem**
- **TemplateServlet:** só serve `.page` livremente para rotas de `/init/*`; fora disso, espera uma request associada a Action
- **Blocos:** além de `id="body"`, você pode injetar outros blocos (header, footer, sidebar, etc.)

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

#### Estrutura de Namespacing (convite_in_web como exemplo)

```
WEB-INF/tags/
├── core/               # Tags globais/transversais
│   ├── page.tag        # Layout page (design selection)
│   └── importLayout.tag    # CSS/JS inclusion helpers
├── modules/
│   ├── convitin/       # Módulo específico
│   │   ├── boxConfirmacao.tag
│   │   ├── tabelaConvidados.tag
│   │   └── compraBox.tag
│   ├── lojas/
│   │   ├── cardProduto.tag
│   │   └── templateLoja.tag
│   └── sistema/        # Módulo sistema (helpers globais)
│       ├── templateEvento.tag
│       ├── tokenSecurityForm.tag
│       └── templatePublicForms/
│           └── tpf_fieldset.tag
└── resource/           # Componentes reutilizáveis
    ├── page.tag        # Wrapper para páginas com design
    ├── uploadImage.tag # Upload com preview
    ├── ajaxSelect.tag  # Select com carregamento AJAX
    ├── inputDateTime.tag   # Date/time picker
    ├── toastr.tag      # Notificações
    ├── relativeTime.tag    # Timestamps relativos
    └── paginationOnDemand.tag
```

#### Exemplo 1: Tag File Simples (alunoonline)

`web/WEB-INF/tags/aluno.tag`:

```jsp
<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="futurepagesApp" prefix="fpg"%>
<%--@elvariable id="params" type="java.util.Map"--%>

<%@attribute name="aluno" type="modules.escola.beans.Aluno" required="true"%>
<%@attribute name="green" type="java.lang.Boolean" required="true"%>

<tr ${green? 'style="color:green"':'style="color:red"'}>
    <td>${aluno.id}</td>
    <td style="text-align: center">
        <img src="${params.UPLOADS_URL_PATH}/alunos/${aluno.id}.jpg" style="width: 24px;"/>
    </td>
    <td>${aluno.nomeCompleto}</td>
    <td>${aluno.matricula}</td>
    <td>${aluno.turma.nome}</td>
    <td colspan="2">
        <a class="btn btn-warning" href="<fpg:contextPath/>/escola/Aluno?type=update&id=${aluno.id}">
            editar
        </a>
        <a class="btn btn-danger" href="javascript:confirmaExclusao('${aluno.id}', '${aluno.nomeCompleto}', '${aluno.matricula}');">
            apagar
        </a>
    </td>
</tr>
```

Uso em JSP:

```jsp
<fpg:loop var="aluno">
    <fpg:aluno aluno="${aluno}" green="${aluno.turma!=null}"/>
</fpg:loop>
```

#### Exemplo 2: Tag File com Lógica Complexa (convite_in_web)

`web/WEB-INF/tags/resource/ajaxSelect.tag`:

```jsp
<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="futurepagesApp" prefix="fpg"%>

<%@attribute name="id"        required="true"%>
<%@attribute name="name"      required="true"%>
<%@attribute name="sourceValue" required="true"%>
<%@attribute name="url"       required="true"%>
<%@attribute name="selected"  required="true"%>
<%@attribute name="cssClass"  required="false"%>
<%@attribute name="defaultText" required="false"%>
<%@attribute name="defaultValue" required="false"%>

<select id="${id}" name="${name}" class="${cssClass} ajaxSelect">
    <fpg:if value="${defaultValue!=null || defaultText!=null}">
        <option value="${defaultValue!=null ? defaultValue : ''}">
            ${defaultText!=null ? defaultText : ''}
        </option>
    </fpg:if>
</select>

<fpg:footer>
<script type="text/javascript">
    $(function() {
        var $ajaxSelect = $('#${id}');
        var $sourceSelect = $('#${sourceValue}');
        var carregandoOption = '<option>Carregando...</option>';
        
        $ajaxSelect.attr('disabled', 'disabled');
        
        $sourceSelect.change(function() {
            $ajaxSelect.attr('disabled', 'disabled');
            if ($sourceSelect.val() && $sourceSelect.val() != '${defaultValue!=null ? defaultValue : ''}') {
                $ajaxSelect.html(carregandoOption);
                $.getJSON('<fpg:contextPath/>${url}', {sourceValue: $sourceSelect.val()}, function(j) {
                    if (j.obj.length > 0) {
                        var options = '<option value="${defaultValue!=null ? defaultValue : ''}">${defaultText}</option>';
                        for (var i = 0; i < j.obj.length; i++) {
                            options += '<option value="' + j.obj[i].key + '">' + j.obj[i].value + '</option>';
                        }
                        $ajaxSelect.html(options).removeAttr('disabled');
                        if ('${selected}' != '') {
                            $ajaxSelect.val('${selected}').change();
                        }
                    }
                });
            }
        });
    });
</script>
</fpg:footer>
```

Uso em JSP:

```jsp
<fpg:ajaxSelect id="municipioSelect"
                name="municipio"
                sourceValue="estadoSelect"
                url="/rest/municipios"
                selected="${entity.municipio.id}"
                defaultValue="0"
                defaultText="Selecione um município"
                cssClass="form-control"/>
```

#### Exemplo 3: Design Pattern com `<fpg:head>` e `<fpg:footer>`

`web/WEB-INF/tags/core/page.tag` (layout wrapper):

```jsp
<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="futurepagesApp" prefix="fpg"%>
<%--@elvariable id="params" type="java.util.Map"--%>

<%@attribute name="design" type="java.lang.String" required="true"%>
<%@attribute name="layout" type="java.lang.Boolean" required="true"%>
<%@attribute name="functions" type="java.lang.Boolean" required="true"%>

<!-- Design selection: bootstrap, mdl, none -->
<fpg:if value="${design=='bootstrap'}" context="true">
    <fpg:then>
        <fpg:head>
            <link type="text/css" href="<fpg:templatePath/>/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
        </fpg:head>
        <fpg:footer>
            <script src="<fpg:templatePath/>/jquery/1.12.4/jquery.min.js"></script>
            <script src="<fpg:templatePath/>/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        </fpg:footer>
    </fpg:then>
</fpg:if>

<!-- Extra: aplicação-específico -->
<fpg:if value="${layout}">
    <fpg:head>
        <link type="text/css" href="<fpg:templatePath module="sistema"/>/layout.css${params.RELEASE_QUERY}" rel="stylesheet"/>
    </fpg:head>
</fpg:if>
```

Uso em JSP:

```jsp
<fpg:page design="bootstrap" layout="true" functions="true"/>
<div class="container">
    <!-- Conteúdo da página -->
</div>
```

### Regras de Criação de Tag Files

1. **Namespace** — use a estrutura `WEB-INF/tags/{namespace}/{tagname}.tag`
2. **Atributos** — declare com `<%@attribute name="..." type="..." required="..."/>`
3. **Variáveis Implícitas** — acesse `${params}` (mapa de parâmetros globais) e `${jspContext}`
4. **CSS/JS Condicional** — use `<fpg:head>` e `<fpg:footer>` dentro da tag para injetos
5. **Documentação** — adicione comentários sobre parâmetros esperados e exemplos

### Diferenças entre alunoonline e convite_in_web

| Aspecto | alunoonline | convite_in_web |
|---|---|---|
| **Nº de tag files** | ~2 (aluno.tag, turma.tag) | ~90+ (organizado por namespace) |
| **Complexidade** | Simples renderização de dados | Lógica complexa (AJAX, designs, forms) |
| **Namespacing** | Raiz de `WEB-INF/tags/` | Estruturado em `core/`, `modules/`, `resource/` |
| **Assets** | CDN externo (Bootstrap 4.6) | Locais embarcados + CDN fallback |
| **AJAX** | Inline em JSP | Encapsulado em tag files |
| **Reutilização** | Por módulo | Global + por módulo |

## Acesso a Atributos na View

Atributos setados na Action com:
```java
output("objects", lista);
output("entity", objeto);
```

Ficam disponíveis na JSP como `${objects}`, `${entity}`.

## Alertas de Manutencao

1. **Origem de Tag** — Se uma tag aparece no projeto mas nao no core Java do framework, procure primeiro em `WEB-INF/tags/` do projeto consumidor.

2. **Nao documente como API do framework** — Uma tag validada apenas no projeto exemplo (ex: `<fpg:ajaxSelect>`) é **aplicação-específica**, não framework core.

3. **Namespace agrega ambos** — O namespace `futurepagesApp` contém:
   - Tags Java core do framework (`<fpg:contextPath/>`, `<fpg:Select>`, etc.)
   - Tag files da aplicação (`<fpg:page>`, `<fpg:ajaxSelect>`, etc.)

4. **Cuidado com versão de jQuery** — Diferentes tag files podem carregar versões diferentes de jQuery (1.11.2, 1.12.4, 3.5.1). Conferir conflitos:
   ```jsp
   <script>
       console.log($.fn.jquery); // Verifica qual jQuery está ativo
   </script>
   ```

5. **CSS Reset e Namespace** — Se múltiplos designs coexistem (Bootstrap 3 + MDL), cuidado com conflitos de classe. Exemplo:
   ```jsp
   <!-- Ambos definem .btn, .btn-primary, etc. -->
   <link href=".../bootstrap/3.3.7/css/bootstrap.min.css"/>
   <link href=".../mdl/1.0.4/material.min.css"/>
   ```

6. **Assets Locais vs CDN** — `convite_in_web` prefere assets locais (`${templatePath}/core/embeded/...`) com fallback para CDN. Isso protege contra desconexões e oferece minificação controlada.

7. **Variável `params`** — Sempre disponível em tag files; contém configurações globais da aplicação:
   ```jsp
   ${params.CONTEXT_PATH}         <!-- Raiz da app -->
   ${params.UPLOADS_URL_PATH}     <!-- Pasta de uploads -->
   ${params.RELEASE_QUERY}        <!-- Versão para cache-busting -->
   ```

## Padrões Observados

### Padrão 1: Inclusão Condicional de JS

```jsp
<fpg:if value="${design=='bootstrap'}" context="true">
    <fpg:then>
        <fpg:footer>
            <script src="...jquery.min.js"></script>
            <script src="...bootstrap.min.js"></script>
        </fpg:footer>
    </fpg:then>
</fpg:if>
```

### Padrão 2: Injeção de CSS/JS em Tag File

```jsp
<%@tag pageEncoding="UTF-8" %>
<%@taglib uri="futurepagesApp" prefix="fpg"%>

<!-- Componente visual -->
<div id="myUploader" class="uploader">
    <input type="file" id="uploadInput"/>
    <div id="preview"></div>
</div>

<!-- Scripts injetados ao footer da página -->
<fpg:footer>
    <script type="text/javascript" src="<fpg:templatePath/>/js/uploader.js"></script>
    <script type="text/javascript">
        $(function() {
            $('#uploadInput').on('change', function() {
                // Lógica específica
            });
        });
    </script>
</fpg:footer>
```

### Padrão 3: Delegação para Outra Tag File

```jsp
<!-- Página que encapsula design + layout -->
<fpg:page design="bootstrap" layout="true" functions="true"/>

<!-- Internamente, <fpg:page> chama: -->
<fpg:importLayout fileName="layout.css" app="${appName}"/>
```

## Fluxo Completo: Request → JSP → Tag Files → HTML

```
1. Request para /escola/Aluno-explore
   ↓
2. AlunoActions.execute() retorna SUCCESS + output("alunos", lista)
   ↓
3. ModuleManager mapeia SUCCESS → fwIn("Aluno-explore.jsp")
   ↓
4. TemplateServlet detecta ".page"? Não → Controller renderiza JSP
   ↓
5. Aluno-explore.jsp:
   - importa taglib fpg: <%@ taglib uri="futurepagesApp" prefix="fpg"%>
   - usa tag file: <fpg:aluno aluno="${aluno}" green="..."/>
   ↓
6. web/WEB-INF/tags/aluno.tag renderiza linha de tabela
   ↓
7. HTML final retornado ao navegador
```

Se fosse `.page`:

```
1-3. [idem]
   ↓
4. TemplateServlet detecta ".page" → busca layout em app-template.xml
   ↓
5. Renderiza Aluno-explore.page dentro do layout (template/layout.jsp)
   ↓
6-7. [idem]
```
