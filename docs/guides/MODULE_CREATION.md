# Criação de Módulo

## Estrutura de Diretórios

```
src/modules/{modulo}/
    ModuleManager.java       ← OBRIGATÓRIO: Configurador do módulo
    actions/                 ← Actions (controllers)
        core/                ←   Classes auxiliares
        ajax/                ←   Actions AJAX
        dyn/                 ←   Actions dinâmicas
    beans/                   ← Entidades JPA/Hibernate
    core/                    ← Lógica compartilhada
    dao/                     ← DAOs (estendem HQLProvider)
    enums/                   ← Enumeradores
    exceptions/              ← Exceções
    filters/                 ← Filters customizados
    formatters/              ← Formatadores
    install/                 ← Instaladores de dados
    jobs/                    ← Jobs QuartZ
    services/                ← Services
    tags/                    ← Tags JSP customizadas
    validators/              ← Validators
```

```
web/modules/{modulo}/
    *.jsp / *.page           ← Views
    dyn/                     ← Fragments AJAX
    includes/                ← Includes JSP
    template/                ← Assets do template (CSS, JS, img)
```

## Passo a Passo

### 1. ModuleManager.java

```java
package modules.meumodulo;

import org.futurepages.menta.core.control.AbstractModuleManager;

public class ModuleManager extends AbstractModuleManager {

    @Override
    public void loadDependencies() {}

    @Override
    public void loadActions() {
        action("Index", IndexAction.class)
            .on(SUCCESS, fwIn("Index.page"));
    }

    @Override
    public void init(Context application) {}
}
```

### 2. Action

```java
package modules.meumodulo.actions;

import org.futurepages.menta.actions.ProtectedAction;

public class IndexAction extends ProtectedAction {
    
    private String nome;
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String execute() {
        output("mensagem", "Olá, " + nome + "!");
        return SUCCESS;
    }
}
```

### 3. View

**`web/modules/meumodulo/Index.page`** → TemplateServlet renderiza com layout

**`web/modules/meumodulo/Index.jsp`:**
```jsp
<%@ taglib uri="futurepagesApp" prefix="fpg"%>
<fpg:page design="bootstrap"/>
<h1><fpg:set var="msg" value="${mensagem}"/>${msg}</h1>
```

### 4. Mapeamento no web.xml

```xml
<servlet-mapping>
    <servlet-name>Controller</servlet-name>
    <url-pattern>/meumodulo/*</url-pattern>
</servlet-mapping>
```

## Convenções

- Os nomes dos módulos em `web.xml` devem coincidir com os nomes no código
- Actions são registradas no `ModuleManager.loadActions()` usando `action()`, `ajaxAction()` ou `dynAction()`
- Views de módulo ficam em `web/modules/{modulo}/`
- Fragments AJAX ficam em `web/modules/{modulo}/dyn/`
- A pasta `conf/` de cada módulo (opcional) contém configurações de locale e Hibernate
