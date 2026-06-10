# Migrações de Banco de Dados

## Sistema: DataModelMigrationController

Sistema de migrações versionadas executado na inicialização da aplicação (após schema generation e installers).

## Estrutura

```
src/migration/
    versions/           ← Migrações ativas (executadas em ordem numérica)
        V_2043_1__descricao.sql
        V_2044_1__outra_descricao.sql
        V_2054_1__script_migration.java
    past/               ← Migrações arquivadas (apenas em dev mode)
        v001_065/
        v066_v130/
    stage/              ← Migrações em desenvolvimento (NÃO executadas)
        V_X_1__proxima_migracao.sql
```

## Nomenclatura

```
V_{numero}_{subnumero}__{descricao}.(sql|class)
```

Exemplos:
- `V_1.sql`
- `V_12_1.sql`
- `V_93__novo_campo.sql`
- `V_2045_4__addCreditoDeEnvio.java`

A ordenação é **numérica**: `V_12_1` (12.1) vem antes de `V_12_2` (12.2).

## Tipos de Migração

### SQL (preferido)

```sql
ALTER TABLE minha_tabela ADD COLUMN novo_campo VARCHAR(255) DEFAULT '' NOT NULL;
```

### Java (apenas quando precisar de lógica)

```java
package migration.versions;

import org.futurepages.core.persistence.Dao;

public class V_2054_1__converte_data {
    public V_2054_1__converte_data() {
        Dao.getInstance().executeSQL("ALTER TABLE x ADD COLUMN y INT");
        Dao.getInstance().executeSQL("UPDATE x SET y = ...");
    }
}
```

## Ciclo de Vida

1. `DataModelMigrationController.execute()` é chamado pelo `ApplicationListener`
2. Lê a classe `VersionedDataModel` configurada em `APP_DATA_MODEL_CLASS`
3. Chama `getVersion()` para saber a versão atual no banco
4. Escaneia `migration/versions/` e ordena por versão
5. Compara: se arquivos têm versão > versão atual, executa pendentes
6. Cada migração roda dentro da **mesma transação**
7. Se falhar: rollback + `Controller.makeUnavailable()` (app fora do ar)
8. Se OK: `addVersion()` registra nova versão

## Regras

- **Se SCHEMA_GENERATION_TYPE** ou **INSTALL_MODE** não forem `none`/`off`, as migrações são **skipped** (apenas registradas)
- Migrações em `past/` são carregadas apenas em **dev mode**
- Não alterar manualmente o registro de versão no banco
- Arquivar versões antigas em `past/` periodicamente

## Configuração

```xml
<param name="APP_DATA_MODEL_CLASS" value="modules.sistema.beans.DataModel"/>
```

A classe `DataModel` implementa `VersionedDataModel` e persiste o histórico em tabela própria (ex: `sistema_datamodel`).
