# Template — Projeto

> Estrutura para todo projeto novo. Cada seção é obrigatória — não pular.

## 1. Identificação
- **Nome:** ...
- **Objetivo profissional:** que Tier(s) este projeto fecha?
- **Inspiração de mercado:** que tipo de empresa constrói algo assim?

## 2. Escopo

### Funcional (o que faz)
- Lista enumerada de funcionalidades
- Marcar prioridade (must / should / nice)

### Não-funcional
- Latência alvo (se aplicável)
- Throughput estimado
- Volume de dados estimado
- Restrições conhecidas

## 3. Stack escolhida
- Linguagem/runtime
- Framework principal (justificar)
- Banco (justificar)
- Outras dependências relevantes

## 4. Decisões arquiteturais iniciais
- Camadas / módulos
- Padrões adotados (com nome por extenso)
- Estilo de comunicação (síncrono/assíncrono, REST/gRPC, etc.)
- Trade-offs já assumidos

## 5. Fases (progressivas, cada uma entregável)
- **Fase 1:** ... (entregável: ...)
- **Fase 2:** ... (entregável: ...)
- **Fase 3:** ... (entregável: ...)
- ...

Cada fase deve:
- Ter critério verificável de "pronto"
- Cobrir ao menos um item de Tier do roadmap
- Render um tour de revisão ao final

## 6. Critérios de "pronto"
Lista do que precisa estar verdade para o projeto contar como concluído.

## 7. Dívida técnica aceita
- O que será deixado de lado intencionalmente
- Por quê
- Quando deve ser revisitado

## 8. Riscos conhecidos
- O que pode dar errado
- Sinal de alerta a observar
- Plano de mitigação

## 9. Plano vivo
- Atualizar `DEV_NOTES.md` (ou equivalente) ao final de cada fase
- Rodar `assessment-planner` ao final de cada fase importante
- Atualizar `jerry-profile.md` com novos padrões/erros observados

## 10. Conexão com o roadmap
- Tiers cobertos por este projeto:
  - Tier S: ...
  - Tier A: ...
  - Tier B: ...
  - Tier C: ...
- Atualizar `../skills/teacher/references/market-roadmap.md` ao final
