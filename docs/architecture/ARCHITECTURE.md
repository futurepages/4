# Arquitetura do Futurepages Web Framework v4

## Visão Geral

Futurepages é um framework MVC para aplicações web Java 8 / JEE6, construído sobre Servlet 3.0 e JSP 2.2. Ele é um fork/evolution do **Mentawai** framework (por Sérgio Oliveira), adicionando camadas completas de persistência, autenticação, serviços, agendamento, migração e utilitários.

## Pacotes Principais

```
org.futurepages.menta.*          ← MVC herdado do Mentawai
org.futurepages.core.*           ← Serviços e infraestrutura nativa
org.futurepages.util.*           ← Utilitários gerais
org.futurepages.enums.*          ← Enums do framework
org.futurepages.exceptions.*     ← Exceções do framework
org.futurepages.formatters.*     ← Formatadores
org.futurepages.jersey.*         ← Integração REST (Jersey)
org.futurepages.test.*           ← Infraestrutura de teste
org.futurepages.emails.*         ← Templates de email
org.futurepages.ant.*            ← Tasks Ant
```

> Para uso, ambos `menta.*` e `core.*` fazem parte da **mesma API** — o agente não precisa distinguir a origem.

## Camadas Arquiteturais

```
┌─────────────────────────────────────────────────────┐
│                    VIEW LAYER                        │
│  JSP + TagLib (fpg:) + TemplateServlet (.page)      │
├─────────────────────────────────────────────────────┤
│                 CONTROLLER LAYER                     │
│  Controller (Front-Controller Servlet)               │
│  Action / Inner Action / Filter Chain / Consequence  │
├─────────────────────────────────────────────────────┤
│                 SERVICE LAYER                        │
│  Services (lógica de negócio estática)               │
├─────────────────────────────────────────────────────┤
│               PERSISTENCE LAYER                      │
│  DAO (HQLProvider) → GenericDao (Dao.getInstance())  │
│  Hibernate 4 / HQLQuery / PaginationSlice            │
├─────────────────────────────────────────────────────┤
│                  DATA LAYER                          │
│  Beans/Entities (JPA/Hibernate annotations)          │
│  MySQL (InnoDB) via JNDI datasource                  │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│              INFRAESTRUTURA                          │
│  Quartz (jobs) │ Migration │ Installers │ Jersey     │
│  Mail │ JasperReports │ Formatters │ Locale          │
└─────────────────────────────────────────────────────┘
```

Observacao: a camada Jersey e um **subsistema adjacente** ao MVC classico. Ela compartilha infraestrutura (Hibernate, services, beans, validators), mas nao usa `Controller`/`ModuleManager` para roteamento. O bootstrap concreto de REST costuma ser decidido pelo projeto consumidor via `web.xml` + `ResourceConfig` propria.

## Princípios Arquiteturais

1. **Front-Controller**: Toda requisição passa pelo `Controller` (servlet), que roteia para a Action correta
2. **Module-oriented**: O projeto é organizado em módulos, cada um com seu próprio `ModuleManager`
3. **Command Pattern**: Action representa um comando, retorna string de resultado → Consequence executa
4. **Chain of Responsibility**: Filters interceptam a requisição antes/depois da Action
5. **Static Services**: Lógica de negócio em métodos estáticos (sem DI)
6. **Fluente HQL**: Consultas construídas com `HQLProvider` + `HQLQuery`
7. **Coexistência de estilos**: o ecossistema combina convenções do framework com extensões de projeto (tag files, transações manuais, endpoints REST heterogêneos)
8. **Legado**: Framework não será mais evoluído — o objetivo é manutenção
