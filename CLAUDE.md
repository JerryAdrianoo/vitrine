# CLAUDE.md — Vitrine

## Commits

Formato obrigatório:
```
VT-{N} Verb in sentence case describing the change
```

- `VT-{N}` — número sequencial do ticket/PR (VT-1, VT-2, VT-3...)
- Primeira letra maiúscula após o número
- Verbo no imperativo: Add, Fix, Update, Remove, Refactor
- Cada commit deve conter apenas uma categoria lógica de mudança

Exemplos válidos:
```
VT-1 Add Gradle multi-module project structure
VT-2 Add domain models and enums
VT-3 Fix null pointer in OrderServiceImpl
```

Exemplos inválidos:
```
add stuff                        ← sem prefixo VT, sem capitalização
VT-1 Add models and fix bug      ← mistura duas categorias
VT-1 added the customer model    ← verbo no passado, sem capitalização
```

## Stack

- Java 21, Gradle multi-módulo
- JAX-RS + Jersey 3.1.x (sem Spring)
- Hibernate 6.x + Jakarta Persistence 3.2
- MySQL 8, Tomcat 10.1
- Módulos: `vitrine-api`, `vitrine-persistence`, `vitrine-service`, `vitrine-web`

## Regras

- Nunca usar Spring — o projeto usa JAX-RS + Hibernate puro
- Manter `DEV_NOTES.md` atualizado com padrões e decisões arquiteturais
- `database.properties` nunca deve ser commitado
