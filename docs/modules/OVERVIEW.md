# Estrutura de Módulo

## O que é um Módulo?

Um módulo é a **unidade organizacional** do Futurepages. Cada módulo agrupa um conjunto coeso de funcionalidades: controllers (actions), models (beans), persistência (DAOs), lógica de negócio (services), validação (validators), views (JSPs) e configuração (ModuleManager).

## Estrutura Padrão

```
src/modules/{modulo}/
├── ModuleManager.java      ← OBRIGATÓRIO — configura actions, filtros, consequências
├── actions/                ← Controllers (FreeAction, ProtectedAction, CrudActions, AjaxAction...)
│   ├── core/               ←   Classes auxiliares de action
│   ├── ajax/               ←   Actions AJAX
│   └── dyn/                ←   Actions dinâmicas
├── beans/                  ← Entidades JPA/Hibernate
├── core/                   ← Lógica compartilhada do módulo
├── dao/                    ← DAOs (estendem HQLProvider)
├── enums/                  ← Enums específicos do módulo
├── exceptions/             ← Exceções do módulo
├── filters/                ← Filters customizados
├── formatters/             ← Formatadores específicos
├── install/                ← Instaladores de dados iniciais
├── jobs/                   ← Jobs Quartz
├── services/               ← Classes de serviço (lógica de negócio)
├── tags/                   ← Tags JSP customizadas
├── tos/                    ← Transfer Objects
├── util/                   ← Utilitários do módulo
└── validators/             ← Validators

web/modules/{modulo}/
├── *.jsp / *.page          ← Views
├── dyn/                    ← Fragments AJAX (sem template)
├── includes/               ← Includes JSP reutilizáveis
└── template/               ← Assets (CSS, JS, imagens)
```

## Módulo Mínimo

Um módulo funcional precisa de apenas 3 arquivos:

1. **`src/modules/{modulo}/ModuleManager.java`**
2. **`src/modules/{modulo}/actions/MinhaAction.java`**
3. **`web/modules/{modulo}/MinhaPagina.jsp`**

## Módulos do Projeto Exemplo (convite_in_web)

| Módulo | Função |
|--------|--------|
| `site` | Páginas públicas (index, preços, termos) |
| `admin` | Administração (usuários, logs, parâmetros) |
| `convitin` | Gestão de eventos (convidados, check-in, loja) |
| `sistema` | Core do sistema (login, auth, pagamentos) |
| `global` | Dados globais (cidades, CEPs, bancos) |
| `carteiro` | Envio de emails |
| `faq` | FAQ |
| `lembretes` | Lembretes |
| `link` | Links |
| `lojas` | Integração com lojas |
| `moip` | Gateway Moip |
| `pagarme` | Gateway PagarMe |
| `pagseguro` | Gateway PagSeguro |
| `juno` | Gateway Juno |
| `recaptcha` | Integração reCAPTCHA |
| `rsvpgo` | Transmissão RSVP |
| `tips` | Dicas |
