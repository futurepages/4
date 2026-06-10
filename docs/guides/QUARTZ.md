# Jobs Agendados (Quartz)

## Ativação

```xml
<param name="QUARTZ_MODE" value="on"/>   <!-- ou "off" em desenvolvimento -->
```

## Criação de Job

1. Crie uma classe no diretório `jobs/` do módulo
2. Implemente `org.quartz.Job`
3. Anote com `@CronTrigger`

```java
package modules.meumodulo.jobs;

import org.futurepages.core.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@CronTrigger(expression = "0 0 2 * * ?")  // Todos os dias às 2h
public class MeuJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Entidade> itens = Dao.getInstance().list(
            hql(Entidade.class, "ativo = true")
        );
        for (Entidade item : itens) {
            // processa...
        }
    }
}
```

## Expressões Cron

Formato padrão Quartz: `segundo minuto hora dia-do-mes mês dia-da-semana [ano]`

Exemplos do projeto convite_in_web:
- `0 0 2 * * ?` — 2h da manhã todos os dias
- `0 45 2 * * ?` — 2h45
- `0 0/15 * ? * * *` — a cada 15 minutos
- `0 0 2,4,8,10,14,16,20,22 * * ?` — múltiplos horários

## Jobs com Intervalo Fixo

Para jobs que precisam rodar em intervalos constantes (não cron):

```java
Thread worker = new Thread(() -> {
    // trabalho recorrente
});

QuartzManager.newDelayedJob(this, 20, TimeUnit.SECONDS, worker);
```

Ou, quando precisar de controle mais fino:

```java
QuartzManager.newDelayedJob(1, "nome-do-job", 0, 20, TimeUnit.SECONDS, worker);
```

## Boa Prática: LoggableJob

Para jobs que precisam de auditoria:

```java
public class MeuJobLoggable extends LoggableJob {
    
    @Override
    public void run() {
        // Lógica do job
    }
}
```

O `LoggableJob` automaticamente:
- Mede tempo de execução
- Loga em tabela de auditoria (tipo `SYSTEM`)
- Faz rollback se houver transacao ativa e loga excecoes com `AppLogger`

> `LoggableJob` e um **padrao de projeto**, nao um contrato central do framework Quartz. Use quando o projeto alvo ja tiver uma classe equivalente.

## Exemplos Reais

| Job | Expressão | Função |
|-----|-----------|--------|
| `RenovacaoPlano` | `0 40 1 * * ?` | Renova planos automaticamente |
| `PagamentosNotificadorChecker` | `0 0/15 * ? * * *` | Verifica notificações a cada 15min |
| `LimpaCaches` | `0 30 2 * * ?` | Limpa caches |
| `Carteiro` | `0 6 0,2,4,6...23 * * ?` | Processa fila de envio de emails |
