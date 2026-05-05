# Rubrica — Pleno → Sênior (Backend Java)

> Critério para considerar o aluno **Sênior**. Pleno faz a coisa funcionar; Sênior faz a coisa funcionar **em escala, com falhas, com time, com dívida controlada**.

## Convenção

- ✅ Demonstrado em projeto real (idealmente em ambiente profissional)
- 🟡 Demonstrado quando guiado, ainda não reflexo
- ❌ Não demonstrado

## 1. Profundidade técnica

- Resolve problemas de concorrência reais (race conditions, deadlocks, contention)
- Diagnostica problemas de performance com profiling (não só com logs)
- Lê e interpreta plano de execução de query complexa e otimiza
- Entende o trade-off entre nível de isolamento e performance
- Conhece pelo menos um GC moderno em profundidade (G1 ou ZGC)

## 2. Arquitetura

- Projeta sistema do zero considerando escala (QPS, storage, latência alvo)
- Escolhe entre síncrono e assíncrono com base em requisitos, não preferência
- Aplica padrões distribuídos quando indicado (saga, outbox, idempotência, retry com backoff)
- Reconhece quando microserviços são solução vs quando são complicação
- Decide consistência forte vs eventual com clareza

## 3. Resiliência e operação

- Projeta para falhar — circuit breaker, bulkhead, timeout, fallback
- Implementa observabilidade útil (não só métrica que ninguém olha)
- Sabe reagir a incidente — runbook, postmortem, ação preventiva
- Avalia capacidade — sabe quanto a app aguenta antes de escalar

## 4. Qualidade em escala

- Faz code review que ensina, não só aponta erro
- Identifica padrão sistêmico (não só bug pontual)
- Escreve testes que servem como documentação executável
- Reduz dívida técnica de forma estratégica, com priorização

## 5. Liderança técnica

- Mentora Pleno e Júnior — explica trade-off, dá contexto, não dá resposta pronta
- Toma decisão técnica em time e sabe justificar para stakeholder não-técnico
- Influencia roadmap técnico — propõe melhoria de arquitetura, não só tarefa
- Modera discussão técnica com base em fatos, não opinião

## 6. Domínio de negócio

- Entende o domínio do produto, não só o código
- Traduz requisito ambíguo em decisão técnica defensável
- Reconhece quando "não fazer" é a melhor solução

## 7. Influência além do código

- Documenta decisão arquitetural (ADR, RFC) de forma reaproveitável
- Padroniza prática que beneficia o time (template, linter, CI gate)
- Compartilha conhecimento (PR, talk, doc interna)

## Critério de promoção

**Sênior = 70% em ✅ por dimensão**, com pelo menos uma dimensão de "Liderança técnica" ou "Influência além do código" claramente positiva. Sem ❌ em "Profundidade técnica" e "Arquitetura" em itens fundamentais (concorrência, design distribuído, performance).

## Como avaliar com o Jerry

A maioria dessas dimensões depende de **contexto profissional real** (time, produto, escala). Sem isso, alguns itens só podem ser simulados. Marque honestamente quando o item exige cenário ainda não vivido.
