# Filtros Built-in

## Filtros do Framework

| Filtro | Pacote | Função |
|--------|--------|--------|
| `InjectionFilter` | `menta.filters` | Injeta parâmetros HTTP na Action via setters |
| `OutjectionFilter` | `menta.filters` | Copia atributos da Action para o Output |
| `HibernateFilter` | `menta.filters` | Gerencia sessão/transação Hibernate |
| `AuthenticationFilter` | `menta.filters` | Bloqueia acesso não autenticado |
| `ModulePermissionFilter` | `menta.filters` | Verifica permissão do usuário no módulo |
| `FileUploadFilter` | `menta.filters` | Processa upload multipart |
| `ExceptionFilter` | `menta.filters` | Captura exceções não tratadas |
| `HeadTitleFilter` | `menta.filters` | Gerencia título da página |
| `VOFilter` | `menta.filters` | Popula Value Object da Action |
| `RedirectAfterLoginFilter` | `menta.filters` | Redireciona após login |
| `CalendarInjectionFilter` | `menta.filters` | Injeta Calendar/Date do input |
| `DateTimeInjectionFilter` | `menta.filters` | Injeta DateTime |
| `PersistenceInjectionFilter` | `menta.filters` | Injeta entidades persistentes |
| `ConsequenceCallbackFilter` | `menta.filters` | Callbacks assíncronos pós-consequence |
| `AutoRedirectDomainFilter` | `menta.filters` | Redireciona domínio automaticamente |

### AfterConsequenceFilter

Filtros que executam **após** a Consequence. Ex: `HibernateFilter` fecha sessão depois de renderizar a view.

## Anotações de Transação (para HibernateFilter)

| Anotação | Efeito |
|----------|--------|
| `@Transactional` | Action dentro de transação |
| `@NonTransactional` | Action sem transação |
| `@MultiTransactional` | Transação distribuída |

## Ordem dos Filtros Globais (InitManager)

```
1. AutoRedirectDomainFilter (se AUTO_REDIRECT_DOMAIN configurado)
2. HibernateFilter (ou ExceptionFilter se Hibernate off)
3. HeadTitleFilter (se GLOBAL_HEAD_TITLE configurado)
4. FileUploadFilter
5. InjectionFilter
```

## Observacoes de Manutencao

- `AuthenticationFilter` devolve respostas diferentes para fluxos diferentes: `LOGIN`, `AJAX_DENIED` e `DYN_LOGIN`.
- `ModulePermissionFilter` avalia `AllModulesFree` de forma diferente de `AuthenticationFree`; nao confunda permissao de modulo com acesso publico.
- `HibernateFilter` fecha sessoes em `afterConsequence()` e tambem decide commit/rollback com base no resultado retornado pela Action.
