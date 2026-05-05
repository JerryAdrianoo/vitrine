# Rubrica — Avaliação de Projeto

> Use ao revisar um projeto concluído (fase ou todo). Pontue cada dimensão de 0 a 5.

## Dimensões

### 1. Funcional (0–5)
- 0: não roda
- 3: cobre o caminho feliz e alguns erros
- 5: cobre golden path, edge cases e falhas previsíveis com mensagem útil

### 2. Arquitetura (0–5)
- 0: tudo numa classe, acoplamento fortíssimo
- 3: camadas claras, mas vazamento ocasional
- 5: camadas com responsabilidade única, dependências em direção certa, fácil de trocar implementação

### 3. Clean Code (0–5)
- 0: nomes ruins, métodos longos, duplicação
- 3: legível, mas com pontos de melhoria
- 5: nomes precisos, métodos curtos com propósito único, zero duplicação evitável

### 4. SOLID e Patterns (0–5)
- 0: viola SRP/DIP em pontos óbvios
- 3: aplica os básicos sob orientação
- 5: aplica naturalmente; cita o princípio por extenso ao decidir

### 5. Testes (0–5)
- 0: sem testes
- 3: testes unitários do caminho feliz
- 5: unitários + integração, casos de borda, asserções específicas

### 6. Segurança (0–5)
- 0: senhas em texto, SQL injection possível, secrets commitados
- 3: hash de senha, validação de entrada, secret fora do repo
- 5: defesa em profundidade, mensagens que não vazam info, auth correta

### 7. Performance e dados (0–5)
- 0: full scans onde poderia ter índice, N+1 em vários lugares
- 3: queries básicas razoáveis, índices óbvios
- 5: planos analisados, índices justificados, paginação, fetch correto

### 8. Operação (0–5)
- 0: roda só na máquina do dev
- 3: containeriza, README mínimo
- 5: container + CI + logs estruturados + métrica básica + secret externo

### 9. Documentação (0–5)
- 0: nenhum DEV_NOTES/README útil
- 3: README com como rodar e estrutura
- 5: DEV_NOTES vivo + ADRs de decisões importantes + diagrama de alto nível

## Pontuação total

- 0–18: ainda em fase inicial — focar em fundamentos
- 19–27: Júnior consolidado
- 28–36: Pleno em formação
- 37–45: Pleno sólido / sinais de Sênior

## Como conduzir

1. Ler o projeto inteiro antes de pontuar
2. Pontuar cada dimensão com 1 frase de justificativa
3. Identificar a dimensão de menor pontuação como foco da próxima fase
4. Atualizar `../skills/teacher/references/jerry-profile.md` com gaps observados
