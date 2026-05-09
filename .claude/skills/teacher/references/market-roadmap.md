# Roadmap de Mercado — Backend Java Pleno

> Norte para guiar o aluno. Baseado em frequência real de skills em vagas de Backend Java Pleno (LinkedIn, Gupy, Glassdoor, Indeed). Atualize quando observar mudança consistente do mercado.

## Tier S — Obrigatório (90–100% das vagas)

- Java 17/21 (POO, Streams, Optional, Concorrência, Generics)
- Spring Boot (DI, Spring MVC, REST APIs, DTOs, validação, exception handler)
- APIs REST profissionais
- SQL avançado (JOINs, índices, GROUP BY, subqueries)
- JPA/Hibernate + Spring Data JPA
- Git avançado

## Tier A — Esperado em Pleno (45–75% das vagas)

- Microsserviços (arquitetura, comunicação, contratos)
- Testes automatizados — JUnit 5, Mockito, integração, Spring Boot Test
- Clean Code + SOLID + Design Patterns (Factory, Strategy, Builder)
- CI/CD — GitHub Actions, GitLab CI, Jenkins
- Swagger / OpenAPI (documentação de contratos)

## Tier B — Diferencial Forte (20–60% das vagas)

- Cloud AWS (IAM, S3, RDS, EC2, CloudWatch, ECS)
- Docker + Docker Compose
- Spring Security + JWT + OAuth2 / OpenID Connect
- Mensageria — Kafka (principal), RabbitMQ, AWS SQS/SNS
- Redis (cache, sessões, performance)
- Observabilidade — logs estruturados, métricas, tracing (Prometheus, Grafana, ELK)

## Tier C — Diferenciais Premium (10–35% das vagas)

- Spring WebFlux / Programação Reativa (Mono/Flux)
- ElasticSearch (indexação, busca, queries)
- Kubernetes (Pods, Deployments, Services, Helm)
- Terraform / IaC

## Tier D — Fullstack Estratégico (10–30%)

- JavaScript ES6+ / TypeScript
- React + Hooks + React Router
- Next.js (diferencial)
- TailwindCSS ou Material UI

## Como usar este roadmap no ensino

1. **Conecte o que o aluno está aprendendo ao Tier correspondente** — deixe explícito o valor de mercado.
2. **Após o Vitrine, iniciar migração para Spring Boot** (Tier S obrigatório).
3. **Ordem sugerida pós-Spring Boot:** Testes de integração → Swagger/OpenAPI → CI/CD → Docker → Spring Security → AWS → Kafka → Redis.
4. **Cada novo projeto deve cobrir ao menos um Tier B ainda não trabalhado.**
5. **Nunca pule Tier S/A para ir a Tier C/D** — base sólida antes de diferenciais.

## Mapeamento atual do Jerry

| Tier | Status | Observações |
|------|--------|-------------|
| S — Java | ✅ Parcial | Java 21 com POO, Generics, Streams básicos — falta Concorrência |
| S — Spring Boot | ❌ Pendente | Usa JAX-RS hoje; migração após Vitrine |
| S — REST APIs | ✅ Sólido | JAX-RS completo, DTOs, validação, exception handling |
| S — SQL/JPA | ✅ Sólido | Hibernate 6, Flyway, JPQL, relacionamentos |
| S — Git | ✅ Parcial | Commits e branches — falta rebase/squash avançado |
| A — Testes | ✅ Parcial | JUnit 5 + Mockito unitários — falta integração |
| A — Clean Code/SOLID | ✅ Em progresso | Entende quando guiado — não é reflexo automático |
| A — CI/CD | ❌ Pendente | Fase 7 do Vitrine |
| A — Swagger | ✅ Sólido | OpenAPI com swagger-jaxrs2-jakarta, @Tag, @Operation — Fase 7 |
| B — JWT/Segurança | ✅ Sólido | JWT + BCrypt implementados e testados |
| B — Docker | ❌ Pendente | Fase 7 do Vitrine |
| B — Kafka/Mensageria | ❌ Pendente | Próximo projeto pós-Vitrine |
| B — Redis | ❌ Pendente | Próximo projeto pós-Vitrine |
| B — Cloud AWS | ❌ Pendente | Fase 7+ |

## Como atualizar este mapeamento

- Cada Fase concluída → atualize linha correspondente
- Cada novo projeto iniciado → marque os Tiers que ele cobre
- Revise o snapshot ao final de cada fase do projeto atual
