# Rubrica — Mock Interview

> Use ao final de uma simulação de entrevista (algoritmo, system design, code review). Pontue por dimensão.

## Modo Algoritmo (0–5 por dimensão)

### Comunicação
- Esclareceu requisitos antes de codar?
- Explicou o raciocínio em voz alta?
- Pediu confirmação de entendimento?

### Resolução
- Apresentou brute force antes de otimizar?
- Identificou o gargalo correto?
- Escolheu estrutura/algoritmo adequado?

### Análise
- Calculou Big O de tempo e espaço corretamente?
- Justificou trade-offs (tempo × memória)?

### Codificação
- Código compila / tem chance de compilar?
- Tratou casos de borda (vazio, único, duplicatas, máximo)?
- Variáveis e métodos com nomes claros?

### Validação
- Rodou mentalmente com exemplo?
- Encontrou bug e corrigiu?

## Modo System Design (0–5 por dimensão)

### Requisitos
- Levantou funcionais e não-funcionais (latência, throughput, disponibilidade)?
- Estimou escala (usuários, QPS, storage)?

### Modelo de dados
- Entidades coerentes, relacionamentos claros, tipo de DB justificado?

### API/Contratos
- Endpoints/payloads consistentes, idempotência considerada?

### Arquitetura
- Começou simples e justificou complicações?
- Identificou gargalos de leitura vs escrita?

### Escala e falhas
- Aplicou caching, fila, sharding com trade-off explícito?
- Pensou em degradação graciosa?

### Trade-offs
- Comparou alternativas com fato (custo, latência, complexidade), não preferência?

## Modo Code Review (0–5 por dimensão)

### Clareza
- Identificou nomes ruins, métodos longos, comentários desnecessários?

### SOLID
- Apontou violações nomeando o princípio por extenso?

### Testabilidade
- Reconheceu acoplamento que dificulta teste?

### Segurança
- Detectou riscos (input não validado, secret, log com PII)?

### Performance
- Detectou N+1, full scan, alocação desnecessária?

### Mentoria
- Perguntou "por quê" antes de criticar?
- Sugeriu alternativa concreta?

## Cálculo

- 0–10 por modo: ainda em formação
- 11–18: Júnior consolidado
- 19–24: Pleno em formação
- 25–30: Pleno sólido

## Feedback

Sempre devolver:
1. **2 pontos fortes** observados na sessão
2. **1 gap principal** com sugestão de reforço
3. **1 próximo passo concreto** (problema, leitura, exercício)
