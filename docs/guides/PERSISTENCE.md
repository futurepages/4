# Persistência

## Stack

- **ORM:** Hibernate 4.1.7.Final
- **Dialect:** MySQL5InnoDBDialect
- **Conexão:** JNDI datasource (configurado no container)
- **Geração de schema:** controlada por `SCHEMA_GENERATION_TYPE` (update/export/none)

## Padrão Vigente

### DAO

O DAO **estende `HQLProvider`** e usa **`Dao.getInstance()`** para operações CRUD.

```java
package modules.admin.dao;

import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.core.persistence.PaginationSlice;

public class UserDao extends HQLProvider {

    public static PaginationSlice<User> paginar(String nome, int page, int pageSize) {
        HQLQuery<User> query = hql(User.class,
            The.concatIfContent("nome LIKE :nome", "%", nome, "%"),
            "nome asc");
        
        if (isNotEmpty(nome)) {
            query.setParameter("nome", "%" + nome + "%");
        }
        
        return Dao.getInstance().paginationSlice(query, page, pageSize);
    }

    public static List<User> listarAtivos() {
        return Dao.getInstance().list(
            hql(User.class, "ativo = true", "nome asc")
        );
    }

    public static User getById(Long id) {
        return Dao.getInstance().get(User.class, id);
    }

    public static User salvar(User user) {
        return Dao.getInstance().save(user);
    }
}
```

> `EntityDao<BEAN>` foi um experimento que não vingou. Não deve ser usado.

### HQLProvider — Métodos Fluentes

```java
// Construtor principal
hql(Entidade.class, whereClause, orderClause)

// Exemplos
hql(User.class, "nome like :nome", "nome asc")
hql(Evento.class, "data >= :hoje and ativo = true", "data desc")
hql(Produto.class)  // sem filtros

// Cláusulas auxiliares
field("nome")        // HQLField para joins
ands(c1, c2, c3)     // AND encadeado
isNotEmpty(valor)    // StringUtils.isEmpty()
```

### GenericDao (via `Dao.getInstance()`)

| Método | Descrição |
|--------|-----------|
| `.save(entity)` | Persiste nova entidade |
| `.update(entity)` | Atualiza entidade |
| `.delete(entity)` | Remove entidade |
| `.get(clazz, id)` | Busca por ID |
| `.load(clazz, id)` | Proxy (lazy) |
| `.persist(entity)` | Persiste sem flush imediato |
| `.merge(entity)` | Merge de entidade detached |
| `.refresh(entity)` | Recarrega do banco |
| `.list(hqlQuery)` | Lista registros |
| `.paginationSlice(query, page, pageSize)` | Resultado paginado |
| `.hql(query)` | Executa HQL |
| `.executeSQLs(sql)` | SQL nativo |
| `.detached(entity)` | Dessassocia da sessão |
| `.detachedList(list)` | Dessassocia lista |

### Paginação

```java
PaginationSlice<User> slice = Dao.getInstance().paginationSlice(query, 1, 20);

slice.getList()      // List<User> da página
slice.getPage()      // Número da página
slice.getPageSize()  // Itens por página
slice.getTotal()     // Total de registros
```

## Transações

O framework suporta dois estilos que **coexistem no ecossistema**:

### 1. Transacao anotada via `HibernateFilter`

```java
@Transactional
public String execute() {
    // Tudo dentro de uma transação
    Dao.getInstance().save(obj1);
    Dao.getInstance().save(obj2);
    return SUCCESS;
}
```

| Anotação | Efeito |
|----------|--------|
| `@Transactional` | Transação padrão (commit no success, rollback no erro) |
| `@NonTransactional` | Sem transação |
| `@MultiTransactional` | Transação distribuída (múltiplos schemas) |

### 2. Transacao manual via `Dao.getInstance()`

Projetos reais, como `convite_in_web`, usam com frequencia:

```java
Dao.getInstance().beginTransaction();
try {
    Dao.getInstance().save(obj1);
    Dao.getInstance().update(obj2);
    Dao.getInstance().commitTransaction();
} catch (Exception ex) {
    Dao.getInstance().rollBackTransaction();
    throw ex;
}
```

Esse estilo aparece em Actions, beans, utils, jobs, migrations e endpoints REST.

### Regra de manutencao

- Nao converta automaticamente transacao manual para anotacao.
- Nao misture os dois estilos no mesmo fluxo sem verificar quem abre e fecha a transacao.
- Antes de alterar persistencia, leia o chamador e procure `beginTransaction()`, `commitTransaction()`, `rollBackTransaction()` e `@Transactional`.

## Beans / Entidades

```java
package modules.admin.beans;

import javax.persistence.*;

@Entity
@Table(name = "admin_usuario")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil")
    private Profile perfil;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    // Getters e Setters...
}
```

## Múltiplos Schemas

Framework suporta múltiplos bancos por módulo via `CONNECT_EXTERNAL_MODULES=true`.
