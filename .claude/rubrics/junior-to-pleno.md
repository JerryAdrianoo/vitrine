# Rubrica — Júnior → Pleno (Backend Java)

> Critério para considerar o aluno **Pleno**. Cada item exige evidência observável (projeto, PR, decisão registrada). "Sabe quando perguntado" não conta.

## Convenção

- ✅ Demonstrado em projeto real
- 🟡 Demonstrado quando guiado, ainda não reflexo
- ❌ Não demonstrado

## 1. Linguagem e plataforma (Tier S)

- Domina POO, generics, Optional e streams em casos não triviais
- Entende e usa concorrência básica (`ExecutorService`, `CompletableFuture`)
- Entende o que acontece na JVM (heap, stack, GC em alto nível)
- Aplica imutabilidade onde faz sentido; sabe contrato de `equals`/`hashCode`

## 2. Frameworks e dados (Tier S/A)

- Cria aplicação Spring Boot do zero com configuração apropriada
- Usa Spring Data JPA com derived queries e `@Query` quando necessário
- Diagnostica e corrige N+1
- Modela relacionamentos com fetch type adequado (sem `EAGER` por reflexo)
- Escreve SQL avançado (JOINs múltiplos, GROUP BY, subqueries) sem ajuda
- Lê plano de execução (`EXPLAIN`) e propõe índice quando indicado

## 3. APIs e segurança (Tier S/B)

- Projeta API REST consistente (status codes, idempotência, versionamento básico)
- Aplica validação de entrada (Bean Validation) e tratamento de erro centralizado
- Implementa autenticação JWT funcional do zero
- Entende ataques comuns (SQL injection, XSS, CSRF, enumeração de usuários) e como mitigar

## 4. Qualidade (Tier A)

- Escreve teste unitário com bom isolamento (mocks no lugar certo)
- Escreve teste de integração que toca o banco real (Testcontainers)
- Cobertura útil — testa caminho crítico, não só "happy path"
- Aplica Clean Code, SOLID e padrões clássicos sem ser lembrado a todo momento
- Faz code review identificando acoplamento, responsabilidade misturada, duplicação

## 5. Operação (Tier A/B)

- Containeriza app Java com Dockerfile multi-stage funcional
- Configura pipeline CI básico (build + test) em GitHub Actions
- Produz logs estruturados e adiciona correlation ID
- Sabe ler dashboards básicos (latência, taxa de erro, throughput)

## 6. Arquitetura e decisão

- Justifica escolhas com trade-offs (não "achei melhor")
- Reconhece quando dívida é aceitável vs quando precisa atacar
- Sabe quando dividir em microserviços é prematuro
- Sabe quando cache resolve e quando esconde problema

## 7. Processo e comunicação

- Mantém plano de projeto vivo (estado, roadmap, decisões, dívida)
- Escreve commit message clara e PR description útil
- Pede ajuda com contexto suficiente — não joga problema cru no time

## Critério de promoção

**Pleno = pelo menos 80% em ✅ por dimensão**, sem nenhuma dimensão abaixo de 60%. Itens em ❌ críticos para o cargo bloqueiam mesmo com média alta.

## Snapshot atual do Jerry (atualize após avaliação)

- Marcar cada item conforme evidência. Carregar `../skills/teacher/references/jerry-profile.md` e cruzar.
- Última avaliação: **a preencher na próxima sessão de `assessment-planner`**
