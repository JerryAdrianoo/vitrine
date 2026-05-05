# Glossário — Critérios de Avaliação

> Vocabulário compartilhado entre rubricas e avaliações. Use sempre os mesmos termos para evitar ambiguidade.

## Estados de demonstração

- **✅ Demonstrado em projeto real** — código, PR ou decisão registrada provam o domínio
- **🟡 Demonstrado quando guiado** — funciona com ajuda, ainda não é reflexo
- **❌ Não demonstrado** — sem evidência ou demonstrado de forma incorreta

## Dimensões de avaliação

- **Funcional:** o software faz o que se propõe nos casos previstos
- **Arquitetura:** estrutura de módulos, camadas, dependências e responsabilidades
- **Clean Code:** legibilidade, nomes, tamanho de método, duplicação
- **SOLID e Patterns:** aplicação dos princípios e padrões clássicos (sempre por extenso)
- **Testes:** cobertura útil, isolamento, casos de borda
- **Segurança:** auth correta, input validado, secrets fora do repo, mensagens que não vazam
- **Performance e dados:** queries, índices, fetch, paginação, ausência de N+1
- **Operação:** containerização, CI, logs, métricas, secrets externos
- **Documentação:** README, plano vivo, ADRs, diagramas
- **Comunicação técnica:** commit, PR, justificativa de decisão

## Pontuação genérica (0–5)

- **0** — ausente / errado
- **1** — tentativa, ainda inadequado
- **2** — funcional mas frágil ou superficial
- **3** — adequado, lacunas conhecidas e aceitas
- **4** — sólido, sem lacunas óbvias
- **5** — exemplar, serve de referência

## Princípios de avaliação

1. **Evidência > opinião** — sempre citar arquivo, linha, decisão concreta
2. **Erros recorrentes pesam mais** — 3 erros do mesmo tipo > 3 erros distintos
3. **Promoção é piso, não teto** — só sobe quando consistentemente atende
4. **Gap declarado > gap escondido** — reconhecer falta é parte da avaliação

## Termos a evitar

- "Parece que sabe" — sem evidência
- "Está quase lá" — sem definição de "lá"
- "Bom o suficiente" — sem critério
- Sigla sem extensão — sempre escrever "Single Responsibility Principle", não "SRP"
