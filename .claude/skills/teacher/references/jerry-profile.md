# Perfil do Aluno — Jerry

> Perfil portável entre projetos. Calibre nível de explicação, padrões a reforçar e erros a vigiar a partir daqui. **Atualize sempre que houver evidência nova.**

## Identidade

- **Nome:** Jerry
- **Nível atual:** Júnior em consolidação (saiu de Iniciante durante o projeto Vitrine)
- **Foco:** Backend Java — quer se tornar desenvolvedor profissional competente
- **Decisão confirmada:** migrar para Spring Boot **após** concluir o Vitrine

## Tecnologias já trabalhadas

- Java 21 — sintaxe, POO, generics, Optional, lambdas básicas
- Gradle multi-módulo — estrutura de dependências entre módulos
- JAX-RS + Jersey 3.1 — endpoints REST, `@Path`, `@GET/POST/PUT/DELETE`, `@Valid`, `@PathParam`, `@QueryParam`
- Hibernate 6 + Jakarta Persistence — entidades, relacionamentos, `EntityManager`, `@Column`, `@ManyToOne`, `@OneToMany`
- MySQL 8 — schema, migrations
- Flyway 10 — migrations versionadas (`V1__`, `V2__`), regras de nomenclatura
- Bean Validation — `@NotBlank`, `@Email`, `@Size`, `@NotNull`, `@DecimalMin`
- SLF4J + Logback — loggers por classe, níveis INFO/WARN/ERROR
- JUnit 5 + Mockito — testes unitários, mocks, `@BeforeEach`, `assertThrows`, `verify`
- JWT (jjwt 0.12.6) — header/payload/signature, geração e validação
- BCrypt (at.favre.lib) — hashing, fator de custo, `hashToString`, `verifyer`
- Optimistic Locking — `@Version` no `Stock`
- Paginação — endpoints com `limit`/`offset`
- OpenAPI/Swagger — `@OpenAPIDefinition`, `@Tag`, `@Operation`, `@ApiResponse` com `swagger-jaxrs2-jakarta`

## Padrões e Princípios Demonstrados

- **Repository Pattern** — interfaces em `vitrine-api`, implementações em `vitrine-persistence`
- **Poor Man's DI** — injeção manual por construtor em `AppConfig`
- **Single Responsibility Principle** — cada classe com responsabilidade única (entendeu sem decorar)
- **Dependency Inversion Principle** — camadas superiores dependem de interfaces, não de implementações
- **Open/Closed Principle** — `@Secured` em métodos individuais sem modificar o `AuthFilter`
- **Interceptor Pattern** — `ContainerRequestFilter` como porteiro de requisições
- **DTO Pattern** — `LoginRequest`, `CustomerRequest`, `OrderRequest` separados das entidades
- **Chain of Responsibility** — `AppExceptionMapper` com cadeia de `if/instanceof`
- **Marker Annotation Pattern** — `@Secured` como anotação sem atributos

## Pontos Fortes

- Raciocínio arquitetural sólido — entende o "porquê" das camadas sem decorar
- Absorve conceitos de segurança rapidamente (JWT, BCrypt, enumeração de usuários)
- Faz perguntas certas na hora certa ("devo mover o V1 para o módulo correto?")
- Segue orientações com precisão após entender o motivo
- Aprende conectando conceito → código → impacto real
- Aceita correção quando justificada com fato/princípio

## Gaps e Pontos a Reforçar

- **Modificadores de acesso:** tendência histórica a usar `public` e esquecer `final` em campos — sempre verificar
- **Imports não utilizados:** autocomplete gera imports desnecessários com frequência
- **Atenção a detalhes de sintaxe:** ponto e vírgula faltando, casing errado em nomes de arquivo
- **Nomenclatura:** às vezes nomeia variável/parâmetro com o tipo errado (ex: `CustomerRequest customer`)
- **SOLID como reflexo:** entende quando explicado, ainda não aplica instintivamente — precisa ser lembrado
- **Concorrência Java:** ainda não trabalhada — gap do Tier S
- **Testes de integração:** só fez unitários até agora — gap do Tier A

## Erros Históricos Observados

- Campo `public` quando deveria ser `private` (ex: `JwtUtil.KEY`)
- Campo sem `final` em injeção por construtor (ex: `AuthResource.authService`)
- `static` indevido em campo de instância
- Arquivo Flyway com `v` minúsculo e underscore duplo (`v2__add__`)
- Import `CompletionService` não utilizado gerado por autocomplete
- Ponto e vírgula faltando (`return customer` sem `;`)
- Typo em string literal (`"Unauthorize"` em vez de `"Unauthorized"`)
- Código de outro teste inserido dentro do teste errado (violação de SRP nos testes)

## Estilo de Aprendizado

- **Comentários linha por linha com `//` são obrigatórios** quando há código
- Prefere entender o porquê **antes** de implementar
- Responde bem a perguntas direcionadas durante o tour de revisão
- Não gosta de receber código pronto sem contexto — quer ser guiado
- **Quando ele diz que terminou, leia o arquivo diretamente** — nunca peça para colar no chat
- Não economiza esforço quando entende o ganho — investe tempo em consolidar conceitos

## Posição no Roadmap de Mercado (snapshot)

- **Tier S:** ~70% — falta Spring Boot e Concorrência Java
- **Tier A:** ~45% — testes unitários OK, Swagger/OpenAPI OK, faltam integração e CI/CD
- **Tier B:** ~20% — JWT/segurança OK, faltam Docker, Kafka, Redis, Cloud
- **Meta curto prazo:** concluir Vitrine → Spring Boot → fechar Tier S
- **Meta médio prazo:** Tier A completo → entrar como Pleno

## Próximas Habilidades Sugeridas

1. Concluir Fase 7 do Vitrine (Docker + CI/CD) — Swagger já concluído
2. Concorrência Java — `CompletableFuture`, `ExecutorService`, `synchronized`, `Lock` (Tier S)
3. Testes de integração com Testcontainers + MySQL real (Tier A)
4. Migração para Spring Boot — projeto novo, comparativo com Vitrine
5. Mensageria (Kafka ou RabbitMQ) — Tier B

## Como atualizar este perfil

Sempre que houver evidência nova (acerto, erro, conceito dominado, decisão tomada), edite a seção correspondente. Mantenha as listas vivas — não acumule entradas obsoletas.
