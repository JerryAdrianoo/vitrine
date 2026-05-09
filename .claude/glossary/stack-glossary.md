# Glossário — Stack do Aluno

> Vocabulário comum usado pelos professores. Quando uma palavra aparecer pela primeira vez no projeto, use este glossário como referência. Atualize quando uma nova tecnologia entrar.

## Java e plataforma
- **JVM:** Java Virtual Machine — runtime que executa bytecode
- **GC:** Garbage Collector — libera memória de objetos sem referência
- **JIT:** Just-In-Time compiler — converte bytecode quente em código nativo
- **Heap / Stack:** memória de objetos / memória de chamada de método
- **Records / Sealed:** features modernas de Java para tipos imutáveis e hierarquias fechadas
- **CompletableFuture:** primitiva de composição assíncrona

## Frameworks JVM
- **Jakarta EE:** sucessor do Java EE — `jakarta.*` (a partir do JEE 9)
- **JAX-RS:** especificação de APIs REST em Jakarta (atualmente usado no Vitrine)
- **Jersey:** implementação de referência do JAX-RS
- **JPA:** Jakarta Persistence API — especificação de ORM
- **Hibernate:** implementação de JPA
- **Spring Boot:** framework dominante de mercado para backend Java
- **Spring Data JPA:** abstração do Spring sobre JPA
- **Spring Security:** módulo de auth do Spring
- **Spring MVC:** módulo HTTP do Spring (paralelo ao JAX-RS)

## Persistência e dados
- **MySQL 8 / PostgreSQL:** SGBDs relacionais
- **Flyway:** ferramenta de migrations versionadas (`V1__`, `V2__`)
- **Liquibase:** alternativa ao Flyway
- **N+1:** anti-padrão clássico do JPA — 1 query inicial gera N adicionais
- **Optimistic Locking:** controle de concorrência via `@Version`
- **Pessimistic Locking:** controle via `SELECT ... FOR UPDATE`
- **EXPLAIN:** comando que mostra plano de execução de query

## API e segurança
- **REST:** estilo arquitetural baseado em recursos e HTTP
- **DTO:** Data Transfer Object — objeto de transporte (separado da entidade)
- **JWT:** JSON Web Token — token assinado para auth
- **BCrypt:** algoritmo de hash de senha resistente a brute force
- **Bean Validation:** especificação Jakarta para validação declarativa (`@NotBlank`, etc.)
- **OAuth2 / OIDC:** protocolos modernos de autorização e identidade

## Build e versionamento
- **Gradle:** ferramenta de build (usada no Vitrine)
- **Maven:** alternativa dominante no mercado
- **Multi-módulo:** projeto com vários módulos com dependências entre si
- **Git rebase / squash:** operações para reorganizar histórico antes de mergear

## Operação
- **Docker:** containerização
- **Docker Compose:** orquestração local de múltiplos containers
- **Kubernetes:** orquestrador de containers em cluster
- **CI/CD:** Continuous Integration / Continuous Delivery
- **GitHub Actions:** plataforma de CI/CD nativa do GitHub
- **Logback / SLF4J:** stack de logging em Java
- **MDC:** Mapped Diagnostic Context — adiciona campos por thread no log
- **Prometheus / Grafana:** stack de métricas
- **OpenTelemetry:** padrão aberto de tracing/métricas/logs

## Termos pedagógicos
- **Tier S/A/B/C/D:** classificação de tópicos por frequência em vagas (ver `market-roadmap.md`)
- **Tour de fase:** revisão guiada após conclusão (ver `templates/review-template.md`)
- **Plano vivo:** documento atualizado ao longo do projeto (`DEV_NOTES.md`)
- **Perfil vivo:** avaliação contínua do aluno (`skills/teacher/references/jerry-profile.md`)
