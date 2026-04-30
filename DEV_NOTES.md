# DEV_NOTES.md — Vitrine

Arquivo de consulta rápida: padrões, princípios e decisões arquiteturais do projeto.

---

## Princípios SOLID aplicados

### S — Single Responsibility Principle (SRP)
Cada classe tem uma única razão para mudar.
- `HibernateUtil` → só cria e fornece o `EntityManagerFactory`
- Repositórios → só acessam dados
- Serviços → só executam lógica de negócio
- Endpoints → só recebem e respondem requisições HTTP

### D — Dependency Inversion Principle (DIP)
Módulos de alto nível não dependem de módulos de baixo nível — ambos dependem de abstrações (interfaces).
- `vitrine-service` depende de interfaces de repositório, não do Hibernate diretamente
- Isso permite trocar a implementação (ex: de Hibernate para Spring Data JPA) sem tocar na lógica de negócio

---

## Design Patterns

### Repository Pattern
Abstrai o acesso ao banco de dados atrás de uma interface.
- **Interface** (contrato): define os métodos disponíveis (`findById`, `save`, etc.) — fica em `vitrine-api`
- **Implementação**: usa o `EntityManager` do Hibernate para executar as queries — fica em `vitrine-persistence`
- **Benefício**: a camada de serviço não sabe como os dados são buscados — só sabe que pode pedir

### Por que as interfaces ficam em `vitrine-api`
As interfaces de repositório e os modelos de domínio ficam em `vitrine-api` para que a camada de serviço
dependa apenas do módulo de API — nunca do Hibernate. Isso garante que, ao migrar para Spring Data JPA,
só as implementações precisam mudar. O contrato (interface) permanece intocado.

---

## Boas Práticas

### `Optional<T>` em vez de `null`
Métodos que podem não encontrar um resultado retornam `Optional<T>`, nunca `null`.
- Força quem chama a tratar o caso "não encontrado" explicitamente
- Elimina `NullPointerException` silenciosos
- Exemplo: `Optional<Customer> findByEmail(String email)`

### `EntityManagerFactory` como Singleton
Criar um `EntityManagerFactory` é uma operação cara (abre pool de conexões, lê configurações).
Deve existir **uma única instância** durante toda a vida da aplicação.
`HibernateUtil` usa um campo `static final` para garantir isso.

### Separação de propriedades de conexão
Credenciais de banco não ficam no `persistence.xml` — ficam em `database.properties`.
- `persistence.xml` → declara entidades e nome da unidade de persistência
- `database.properties` → URL, usuário, senha, configurações do Hibernate
- `database.properties` deve estar no `.gitignore` para não expor credenciais

### `transaction-type="RESOURCE_LOCAL"`
Usado quando não há um servidor de aplicação (JEE container) gerenciando as transações.
No nosso caso usamos Tomcat simples, então gerenciamos as transações manualmente via `EntityTransaction`.

### Rollback em toda operação de escrita
Todo método que altera estado (`save`, `update`, `delete`) deve fazer `rollback` se ocorrer qualquer exceção,
garantindo que o banco nunca fique em estado inconsistente.

---

## Domínio — E-commerce (Vitrine)

### Entidades e relacionamentos
```
Customer   1 ──< Order      (um cliente tem muitos pedidos)
Order      1 ──< OrderItem  (um pedido tem muitos itens, cascade ALL)
OrderItem >── 1 Product     (cada item referencia um produto)
Product   >── 1 Category    (cada produto pertence a uma categoria)
Product    1 ── 1 Stock     (cada produto tem um registro de estoque)
Order      1 ── 1 Payment   (cada pedido tem no máximo um pagamento)
```

### Regras de negócio centrais
- Ao fazer um pedido → estoque é **reservado** (não deduzido ainda)
- Ao confirmar pagamento → estoque é **deduzido** de verdade
- Ao cancelar pedido → estoque é **liberado**
- Não é possível fazer pedido com quantidade maior que o estoque disponível
- O `unitPrice` em `OrderItem` é fixado no momento da compra (imune a mudanças futuras de preço)

---

## Estrutura de Módulos e Dependências

```
vitrine-api          ← modelos de domínio, interfaces de repositório e serviço, DTOs
vitrine-persistence  ← implementações dos repositórios, HibernateUtil
vitrine-service      ← lógica de negócio (depende de vitrine-api)
vitrine-web          ← endpoints JAX-RS (depende de vitrine-api + vitrine-service + vitrine-persistence)
```

**Regra:** camadas superiores nunca importam camadas que estão abaixo delas na cadeia de dependência inversa.
`vitrine-web` nunca importa `vitrine-persistence` diretamente.

## Camada Web — JAX-RS + Jersey

### Poor Man's DI (injeção de dependência manual)
Sem framework de DI (como CDI ou Spring), a classe `AppConfig` instancia manualmente
toda a cadeia: repositórios → serviços → resources, e os registra no Jersey.
- Vantagem: simples, explícito, sem magia
- Onde fica: `vitrine-web/AppConfig.java`

### Códigos HTTP usados
- `201 CREATED` → recurso criado com sucesso (POST)
- `204 NO CONTENT` → operação bem-sucedida sem corpo de resposta (PUT, DELETE)
- `404 NOT FOUND` → recurso não encontrado
- `200 OK` → consulta bem-sucedida

### Rota base
Todas as rotas têm prefixo `/api` (definido em `VitrineApplication` via `@ApplicationPath`).
Exemplo: `POST /api/orders?customerId=1`

---

## Autenticação JWT (Fase 5)

### Fluxo de autenticação
1. Cliente envia `POST /api/auth/login` com `{ email, password }`
2. `AuthServiceImpl` busca o customer por email e verifica a senha com BCrypt
3. Se válido, `AuthResource` gera um JWT via `JwtUtil` e retorna `{ token }`
4. O cliente inclui `Authorization: Bearer <token>` em todas as requisições seguintes
5. `AuthFilter` intercepta as requisições `@Secured`, valida o token e aborta com 401 se inválido

### Decisões de segurança
- **Mesma mensagem para email/senha inválidos** (`"Invalid credentials"`) — evita enumeração de usuários
- **BCrypt** para hash de senhas — resistente a rainbow tables e brute force
- **JWT HS256** com chave de 256+ bits — assina e verifica sem consultar o banco
- **Expiração de 1 hora** — tokens de longa duração são um risco se vazarem
- **Chave secreta em código** — aceitável para desenvolvimento; em produção deve vir de variável de ambiente

### Princípios aplicados
- **SRP**: `AuthService` só autentica, `JwtUtil` só gera/valida token, `AuthFilter` só intercepta
- **DIP**: `AuthResource` depende de `AuthService` (interface), não de `AuthServiceImpl`
- **Separation of Concerns**: JWT é preocupação da camada web — `AuthService` não conhece JWT

---

## Plano do Projeto

### Estado Atual

| Fase | Status | Descrição |
|------|--------|-----------|
| Fase 1 — Domínio | ✅ Concluída | Modelos, enums, interfaces |
| Fase 2 — Persistência + Serviço | ✅ Concluída | Hibernate, repositórios, services |
| Fase 3 — Web | ✅ Concluída | JAX-RS, recursos, exception handling |
| Fase 3b — Estabilização | ✅ Concluída | Bean Validation, Flyway, Logback |
| Fase 4 — Testes Unitários | ✅ Concluída | JUnit 5 + Mockito, 10 testes |
| Fase 5 — Segurança JWT | ✅ Concluída | JWT + BCrypt, AuthFilter, @Secured, testes atualizados, testado end-to-end |

### Próximas Etapas Imediatas (Fase 6)

1. `CategoryResource` — CRUD completo (hoje categorias só existem via SQL)
2. Paginação nos endpoints de lista (`findAll`)
3. `@Version` no `Stock` — Optimistic Locking para concorrência

### Roadmap Médio Prazo (Fases 6–7)

| Fase | Descrição |
|------|-----------|
| Fase 6a | `CategoryResource` — CRUD completo (hoje categorias só existem via SQL) |
| Fase 6b | Paginação nos endpoints de lista (`findAll`) |
| Fase 6c | `@Version` no `Stock` — Optimistic Locking para concorrência |
| Fase 7a | Docker — containerizar app + MySQL |
| Fase 7b | GitHub Actions — CI com build + testes |
| Fase 7c | OpenAPI/Swagger — documentação automática dos endpoints |

### Roadmap Longo Prazo (Fase 8+)

| Fase | Descrição |
|------|-----------|
| Fase 8 | Testes de integração com Testcontainers + MySQL real |
| Futuro | Migrar para Spring Boot — após Vitrine concluído, novo projeto para comparar |

### Dívida Técnica

| Item | Severidade | Quando resolver |
|------|-----------|-----------------|
| `SECRET` do JWT fixo no código | Alta | Antes de qualquer deploy — usar variável de ambiente |
| `mysql-connector-j` como `implementation` (deveria ser `runtimeOnly`) | Baixa | Fase 7 (Docker/CI) |
| Sem testes para `AuthServiceImpl` | Média | Fase 8 |
| `CustomerResource.update` recebe `Customer` diretamente (sem DTO) | Média | Fase 6 — criar `CustomerUpdateRequest` sem campo `password` |

---

## Progresso do Aluno

### Pontos Fortes
- Raciocínio arquitetural sólido — entende separação de camadas sem decorar
- Absorve conceitos de segurança rapidamente (JWT, BCrypt, enumeração de usuários)
- Faz perguntas certas na hora certa (ex: "devo mover o V1 para o módulo correto?")
- Aprende conectando conceito → código → impacto real
- Entende padrões quando explicados e consegue aplicá-los com orientação

### Gaps e Pontos a Reforçar
- Modificadores de acesso: tendência a esquecer `final` e usar `public` em campos sensíveis
- Imports desnecessários gerados por autocomplete
- Nomenclatura: às vezes nomeia parâmetro com tipo errado (ex: `CustomerRequest customer`)
- Single Responsibility Principle, Open/Closed Principle e demais princípios SOLID ainda não são reflexo automático

### Erros Históricos Observados
- Campo `public` quando deveria ser `private` (ex: `JwtUtil.KEY`)
- Campo sem `final` em injeção por construtor (ex: `AuthResource.authService`)
- `static` indevido em campo de instância
- Arquivo Flyway com `v` minúsculo e underscore duplo no nome
- Typo em string literal (`"Unauthorize"` em vez de `"Unauthorized"`)
- Código de outro teste inserido dentro do teste errado

### Padrões e Princípios Demonstrados
- Repository Pattern, Poor Man's Dependency Injection, DTO Pattern
- Single Responsibility Principle, Dependency Inversion Principle, Open/Closed Principle
- Interceptor Pattern, Chain of Responsibility, Marker Annotation Pattern

### Nível Atual
**Júnior** (consolidado — evoluiu de Iniciante durante a Fase 5)
Demonstra raciocínio arquitetural e absorve padrões com facilidade.
O próximo passo é internalizar Clean Code e SOLID como reflexo automático, sem precisar ser lembrado.