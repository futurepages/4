# Padrões de Actions

## Hierarquia

```
Action (interface)
  └── AbstractAction
        ├── FreeAction            → implements AuthenticationFree
        ├── LoginAction           → implements AuthenticationFree, HiddenRequestAction
        ├── ProtectedAction       → implements RedirectAfterLogin
        ├── CrudActions           → implements AuthenticationFree, RedirectAfterLogin
        │     ├── FreeCrudActions
        │     └── ProtectedCrudActions
        └── NullAction            → extends FreeAction, implements DontTrackURL
```

## Interfaces Marcadoras

| Interface | Método | Efeito |
|-----------|--------|--------|
| `AuthenticationFree` | `bypassAuthentication(innerAction)` | Bypass do `AuthenticationFilter` |
| `RedirectAfterLogin` | `shouldRedirect(innerAction)` | Redireciona para action após login |
| `AllModulesFree` | — | Dispensa a exigencia de possuir o modulo da action; nao torna a action publica |
| `GlobalFilterFree` | `isGlobalFilterFree(innerAction)` | Bypass de filtros globais |
| `DynAction` | — | Action dinâmica (AJAX) |
| `AjaxAction` | — | Action AJAX |
| `HiddenRequestAction` | — | Request oculto em logs |
| `DontTrackURL` | — | Não rastreada pelo URLTracker |

## Inner Actions

Permitem que uma Action atenda múltiplos comportamentos:

```java
public class UserActions extends ProtectedCrudActions {

    @Override
    public String execute() {
        // /admin/User  ou  /admin/User?type=explore
        return super.execute();
    }

    // /admin/User-explore?nome=Joao
    public String explore() {
        String nome = input.getStringValue("nome");
        output("objects", UserDao.buscarPorNome(nome));
        return SUCCESS;
    }

    // /admin/User-create
    public String create() {
        User user = (User) input.getValue("user");
        try {
            UserServices.criar(user);
            return SUCCESS;
        } catch (IllegalArgumentException e) {
            throw new UserException(e.getMessage());
        }
    }

    // /admin/User-update
    public String update() { ... }

    // /admin/User-delete
    public String delete() { ... }
}
```

## CrudActions — Hooks

```java
public class ProdutoActions extends ProtectedCrudActions {

    @Override
    protected void listObjects() {
        // EXPLORE: carrega lista de objetos
        output("objects", ProdutoDao.listarTodos());
    }

    @Override
    protected void listDependencies() {
        // CREATE/UPDATE: prepara combos
        output("categorias", CategoriaDao.listar());
    }

    @Override
    protected void restoreObject() {
        // SHOW/UPDATE/DELETE: carrega objeto pelo ID
        Long id = input.getLongValue("id");
        output("entity", ProdutoDao.getById(id));
    }
}
```

### Observacao Importante

`CrudActions.execute()` chama internamente `doListDependencies()`, mas o hook de customizacao normalmente sobrescrito nos projetos e `listDependencies()`. O `doListDependencies()` atua como wrapper do ciclo de vida.

Os nomes das chaves de output (`"objects"`, `"entity"`, `"categorias"`) sao convencoes de projeto e podem variar. O contrato nativo da Action e `output("chave", valor)`.

## Result Strings (Manipulable)

```java
SUCCESS, ERROR, LOGIN, REDIR,
ACCESS_DENIED, AJAX_DENIED, DYN_DENIED,
EXCEPTION, NOT_FOUND, NULL,
SUBMIT, EXPLORE, SHOW, CREATE, UPDATE, DELETE,
AJAX_REDIR, AJAX_ERROR, AJAX_SUCCESS, AJAX_DENIED
```

## Boas Práticas

1. **Action fina**: Action só roteia e converte exceções
2. **Service grosso**: Lógica de negócio estática em Services
3. **Service lança exceções de domínio**: Action captura e converte para `UserException`
4. **Inner actions para variações**: Evite classes separadas para cada fluxo CRUD
5. **Conferir o estilo transacional local**: o framework suporta `@Transactional`, mas projetos reais tambem usam `beginTransaction()`/`commitTransaction()` manualmente
6. **Preservar aliases do `ModuleManager`**: `action("Index,evento", Index.class)` e comum em projetos reais
7. **Nao remover redundancias sem leitura do fluxo**: `DynAction`/`AjaxAction` podem coexistir com `@AsynchronousAction`
