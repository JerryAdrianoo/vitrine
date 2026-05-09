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

## Paginação (Fase 6b)

### Convenção dos endpoints `findAll`
Todos os endpoints de lista aceitam `?page=0&size=20` (query params, defaults razoáveis) e devolvem
um `PageResponse<T>` em vez de `List<T>`.

### `PageResponse<T>` — formato de resposta
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 137
}
```
Genérico (`<T>`) para reaproveitar entre `Customer`, `Product`, `Category`, `Order` etc.
Fica em `vitrine-api` para que a camada de serviço também devolva `PageResponse`.

### Fluxo entre camadas
- **Repositório**: dois métodos — `findAll(int page, int size)` (com `setFirstResult`/`setMaxResults`)
  e `count()` (devolve `Long` — `COUNT(e)` no JPQL).
- **Serviço**: chama os dois e monta o `PageResponse`.
- **Resource**: passa `page`/`size` adiante e devolve o `PageResponse` direto.

### Decisões
- Não usar `Pageable` do Spring — o projeto é sem Spring, e o tipo nativo `PageResponse` mantém
  o contrato explícito.
- `totalElements` é `Long` (não `int`) — tabelas grandes podem ultrapassar `Integer.MAX_VALUE`.
- Defaults `page=0`, `size=20` — evita listas sem fim quando o cliente esquece dos parâmetros.

---

## Optimistic Locking (Fase 6c)

### Por que `Stock` precisa de `@Version`
Estoque é o ponto de maior contenção: vários pedidos competindo pela mesma linha. Sem controle
de concorrência, dois pedidos paralelos podem ler `quantity=1`, ambos reservar e gerar `quantity=-1`.

### Como funciona
```java
@Version
private Long version;
```
A cada `UPDATE`, o Hibernate adiciona `WHERE version = ?` e incrementa o campo. Se outra
transação já atualizou a linha, o `WHERE` não bate, 0 linhas afetadas, e o Hibernate dispara
`OptimisticLockException`.

### Optimistic vs Pessimistic
- **Optimistic** (escolhido): assume que conflitos são raros. Não trava linhas. Mais escalável.
  Quem perde a corrida recebe exceção e pode tentar de novo.
- **Pessimistic** (`SELECT ... FOR UPDATE`): trava a linha durante toda a transação. Garante
  exclusividade, mas serializa o acesso e pode gerar deadlocks.

Para e-commerce com muitas leituras e poucas escritas concorrentes na mesma linha, optimistic
é a escolha padrão.

### Tratamento da exceção
`AppExceptionMapper` converte `OptimisticLockException` em `409 CONFLICT` com mensagem clara
("Resource was modified by another transaction. Please retry."). O cliente decide se tenta
de novo.

---

## OpenAPI / Swagger (Fase 7c)

### Stack
- `swagger-jaxrs2` (Swagger Core) — gera a especificação OpenAPI 3 a partir das anotações JAX-RS.
- `OpenApiResource` registrado em `AppConfig` — expõe `/api/openapi.json` e `/api/openapi.yaml`.

### Anotações usadas
- `@Operation(summary = "...", description = "...")` — descreve o endpoint.
- `@ApiResponse(responseCode = "...", description = "...")` — documenta cada possível resposta.
- `@Parameter` — descreve query/path params quando o nome não é auto-explicativo.
- `@Tag` (no nível da resource class) — agrupa endpoints na UI por domínio.

### Decisão: anotar manualmente, não gerar do código
O Swagger Core consegue inferir muita coisa, mas anotar explicitamente força o aluno a pensar
no contrato da API (códigos de erro, exemplos, descrições). Documentação não é só para o cliente —
é também uma forma de revisar o próprio design.

---

## Docker (Fase 7a)

### Multi-stage build
O `Dockerfile` usa duas stages para separar build de runtime:
- **builder** (`eclipse-temurin:21-jdk`): compila o projeto e gera o `.war`.
- **runtime** (`tomcat:10.1-jre21`): só copia o `.war` para `webapps/ROOT.war` e roda o Catalina.

Imagem final fica enxuta — só JRE + Tomcat + a app, sem Gradle nem código-fonte.
Renomear o `.war` para `ROOT.war` faz a app responder em `/api/...` na raiz, em vez de
`/vitrine-web-1.0-SNAPSHOT/api/...`.

### Cache de dependências
A ordem dos `COPY` no Dockerfile é deliberada: primeiro `gradlew`, `gradle/`, `settings.gradle`
e os `build.gradle` de cada módulo, e *só depois* o `modules/` com o código-fonte. Quando você
muda só código (não build.gradle), o Docker reaproveita as layers anteriores e pula o
`./gradlew dependencies` — economiza minutos no rebuild.

### Externalized Configuration (12-Factor App #3)
`HibernateUtil` e `JwtUtil` agora seguem a ordem de prioridade
**env var → `database.properties` → default**:
- Em **dev local**: `database.properties` no classpath fornece os valores.
- Em **container**: o arquivo é excluído pelo `.dockerignore`, e os valores vêm do
  `docker-compose.yml` via env vars (`DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`).
- **Defaults sensatos** cobrem casos onde nem env nem property estão presentes
  (`com.mysql.cj.jdbc.Driver`, `validate`, `CamelCaseToUnderscoresNamingStrategy`).

Princípio: **credenciais e config de ambiente nunca dentro da imagem**. Quem rodar a imagem
em produção injeta os valores via secret manager / variável de ambiente. A imagem em si é
o mesmo artefato em dev, staging e prod.

### Healthcheck e ordem de inicialização
O `mysql` define um healthcheck (`mysqladmin ping`); o `app` declara
`depends_on: { mysql: { condition: service_healthy } }`. Resultado: o app só sobe quando o
MySQL está aceitando conexões, evitando o crash clássico de "connection refused" no boot.
`start_period: 30s` dá folga para a primeira inicialização do MySQL (cria `mysql.user`,
indexa, etc).

### `.dockerignore`
Filtra o que não vai pro build context: `.gradle/`, `**/build/`, `.git/`, `.idea/`,
`**/database.properties`, `*.md`, `.claude/`. Reduz o tempo de envio do contexto e impede
que credenciais ou IDE configs vazem para a imagem.

### Conflito de porta no host
`mysql` mapeia `host:3307 → container:3306` (e não 3306:3306) para evitar conflito com
um MySQL local instalado no Windows que costuma ocupar a 3306. **A app dentro da rede Docker
NÃO usa essa porta** — ela acessa o banco via `mysql:3306` no DNS interno do compose.
A porta exposta serve só para conectar com cliente SQL externo (DBeaver/Workbench).

### Gotcha — `gradlew` com CRLF
Em projeto clonado no Windows, o `gradlew` costuma vir com line endings CRLF. No Linux do
container, o shebang `#!/bin/sh\r` aponta para um interpretador chamado `sh\r` que não existe,
e `RUN ./gradlew ...` falha com `not found` (mensagem enganosa — o arquivo existe; o
interpretador é que não). Correção no Dockerfile: `RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew`.

### Lazy initialization vs falha em boot
`HibernateUtil` tem `EntityManagerFactory` em campo `static final` — só é construído na
primeira chamada à classe (lazy class loading). Resultado: se a config do banco está quebrada,
a app **sobe normalmente** e só explode no primeiro request que toca o banco. Boot saudável
não significa app saudável — sempre faça smoke test de um endpoint que percorre todas as
camadas (login serve bem).

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
| Fase 6a — CategoryResource | ✅ Concluída | CRUD completo de categorias (VT-00006) |
| Fase 6b — Paginação | ✅ Concluída | `PageResponse<T>` em todos os `findAll` (VT-00006) |
| Fase 6c — Optimistic Locking | ✅ Concluída | `@Version` em `Stock` + `409 CONFLICT` no mapper (VT-00006) |
| Fase 7c — OpenAPI/Swagger | ✅ Concluída | Swagger Core, anotações em todas as resources (VT-00007) |
| Fase 7a — Docker | ✅ Concluída | Multi-stage Dockerfile + docker-compose com MySQL, env vars, `@JsonIgnore` em `passwordHash` (VT-00008) |

### Próximas Etapas Imediatas

1. **Fase 7b — GitHub Actions** — CI com build + testes em cada push/PR
2. **Fase 8 — Testcontainers** — testes de integração com MySQL real
3. **DTO de saída** — `CustomerResponse` para substituir o `@JsonIgnore` paliativo

### Roadmap

| Fase | Descrição |
|------|-----------|
| Fase 7b | GitHub Actions — CI com build + testes |
| Fase 8 | Testes de integração com Testcontainers + MySQL real |
| Futuro | Migrar para Spring Boot — após Vitrine concluído, novo projeto para comparar |

### Dívida Técnica

| Item | Severidade | Quando resolver |
|------|-----------|-----------------|
| Sem testes para `AuthServiceImpl` | Média | Fase 8 |
| `CustomerResource.update` recebe entidade `Customer` direto (sem DTO de entrada) | Média | Criar `CustomerUpdateRequest` sem campo `password` |
| `CustomerResource` retorna entidade `Customer` direto (sem DTO de saída) | Média | Criar `CustomerResponse` — `@JsonIgnore` no `passwordHash` é paliativo, mas vaza decisões internas (campos JPA, lazy proxies) |
| `JWT_SECRET` default inseguro no código se env var ausente | Baixa | Aceitável em dev; em produção o `docker-compose.yml` deve falhar se a env não estiver setada |

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
- `Properties.load(null)` — não checar `getResourceAsStream` antes (Fase 7a)
- Vazamento de `passwordHash` por serializar entidade JPA direto na resposta (Fase 7a)

### Padrões e Princípios Demonstrados
- Repository Pattern, Poor Man's Dependency Injection, DTO Pattern
- Single Responsibility Principle, Dependency Inversion Principle, Open/Closed Principle
- Interceptor Pattern, Chain of Responsibility, Marker Annotation Pattern
- Generic Wrapper (`PageResponse<T>`), Optimistic Concurrency Control (`@Version`)
- Externalized Configuration (12-Factor App #3), Multi-stage Docker Build

### Nível Atual
**Júnior** (consolidado — evoluiu de Iniciante durante a Fase 5)
Demonstra raciocínio arquitetural e absorve padrões com facilidade.
O próximo passo é internalizar Clean Code e SOLID como reflexo automático, sem precisar ser lembrado.