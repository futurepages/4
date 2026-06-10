# Instalação de Dados (InstallersManager)

## Ativação

```xml
<param name="INSTALL_MODE" value="on"/>
```

Modos:
| Valor | Comportamento |
|-------|---------------|
| `off` | Não executa nada |
| `on` | Resources + Módulos + Examples |
| `modules` | Resources + Módulos (sem Examples) |
| `production` | Resources + Módulos (sem Examples) |
| `examples` | Resources + Módulos + Examples |
| `script:nome.sql` | Apenas SQL específico |

## Estrutura

```
src/
    install/
        Resources.java       ← Instalação global (diretórios, etc.)
        Examples.java        ← Dados de exemplo/demonstração
    modules/{modulo}/
        install/
            MeuInstalador.java  ← Instalador do módulo
```

## Criação de Instalador

```java
package modules.meumodulo.install;

import org.futurepages.core.install.Installer;

public class MeuInstalador extends Installer {

    public MeuInstalador() throws Exception {
        // execute() é chamado automaticamente pelo construtor
    }

    @Override
    public void execute() {
        // Salva parâmetros iniciais
        Dao.getInstance().save(new Parametro("chave", "valor"));
        
        // Executa sub-instalador
        install(new SubInstalador());
        
        // Executa SQL de resource
        executeSQLFromFile("res/meu_script.sql");
    }
}
```

## Ordem de Execução

1. `install.Resources` — recursos globais (ex: criar diretórios de upload)
2. Script SQL (se `script:nome.sql`)
3. Instaladores dos módulos (ordem alfabética dos nomes dos módulos)
4. Extra (dependendo do modo):
   - `install.Examples` — dados de demonstração
   - `install.{Modo}` — modo personalizado

Tudo executa em **uma única transação**: commit se tudo ok, rollback em caso de erro.
