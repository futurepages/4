# Validação

## Sistema Vigente: Validator (Menta)

```java
import org.futurepages.menta.core.validation.Validator;

public class UserValidator extends Validator {

    public UserValidator(User user) {
        this(user, false); // breakOnFirst = false (acumula erros)
    }

    public UserValidator(User user, boolean breakOnFirst) {
        super(breakOnFirst);

        if (isEmpty(user.getNome())) {
            error("nome", "Nome é obrigatório.");
        }
        if (isNotEmpty(user.getEmail()) && !isValidEmail(user.getEmail())) {
            error("email", "Email inválido.");
        }
        if (user.getSenha() != null && user.getSenha().length() < 6) {
            error("senha", "Senha deve ter no mínimo 6 caracteres.");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
```

### Modos

| Modo | Comportamento |
|------|---------------|
| `breakOnFirst=false` | Acumula todos os erros no `validationMap` |
| `breakOnFirst=true` | Lança `UserException` no primeiro erro |

### Uso na Action

```java
// O validate() do AbstractAction instancia o Validator
// e devolve a instância configurada com breakOnFirst.
public String create() {
    User user = (User) input.getValue("user");
    validate(UserValidator.class, false).create(user);
    return SUCCESS;
}
```

Observacoes:

- `validate(UserValidator.class)` usa `breakOnFirst=true`.
- `validate(UserValidator.class, false)` acumula erros no `validationMap` e o proprio validator costuma chamar `validate()` ao final do metodo publico.
- O framework **nao** possui uma sobrecarga `validate(UserValidator.class, user)`.

### Sub-validators

Validators podem compor outros validators para reutilização:

```java
public class EventoValidator extends Validator {
    public EventoValidator(Evento evento) {
        super(false);
        // Validações do evento...
        new EnderecoValidator(evento.getEndereco(), this); // adiciona erros ao mesmo validationMap
    }
}
```

## UserException

Exceção da camada de controle para comunicação com o usuário:

```java
// Lançamento direto
throw new UserException("Mensagem para o usuário");

// Com validationMap
UserException ue = new UserException();
ue.setValidationMap(validationMap);
throw ue;
```

## Boa Prática: Service + Action

```java
// Service lança exceções de domínio
public class UserServices {
    public static User criar(User user) {
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        return Dao.getInstance().save(user);
    }
}

// Action captura e converte
public class UserAction extends ProtectedAction {
    public String create() {
        try {
            User user = (User) input.getValue("user");
            UserServices.criar(user);
            return SUCCESS;
        } catch (IllegalArgumentException e) {
            throw new UserException(e.getMessage());
        }
    }
}
```

> **Apenas o Validator do pacote `menta.core.validation` é vigente.**  
> `org.futurepages.core.validation.*` está `@Deprecated` e não deve ser usado.

## Padrao Real no Ecossistema

Em projetos como `convite_in_web`, e comum que cada validator exponha metodos de caso de uso, por exemplo:

```java
validate(EventoValidator.class, false).create(evento, usuario);
validate(EventoValidator.class, false).update(evento, momentoAntes, confirmacaoAntes, usuario);
```

Esses metodos publicos encapsulam a montagem das regras e chamam `validate()` no final.
