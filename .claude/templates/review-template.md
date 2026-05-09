# Template — Tour de Revisão

> Use ao final de uma fase ou conjunto significativo de tarefas. **Obrigatório** — não pular alegando que "o aluno já sabe".

## 1. Inventário
- Liste todos os arquivos criados ou modificados na fase
- Agrupe por camada/módulo

## 2. Para cada arquivo

### a. Pergunta-âncora (antes de explicar)
Escolha uma:
- "Por que essa classe está nesse módulo e não em outro?"
- "O que aconteceria se removêssemos esse `final`?"
- "Qual princípio SOLID esse padrão está aplicando?" (resposta esperada por extenso)
- "O que diferencia esse DTO da entidade de domínio?"
- "Por que esse método é `private`?"
- "O que esse `@Version` está prevenindo?"

### b. Aluno responde
- Aguardar — não responder por ele

### c. Validação
- Resposta correta → reforçar e conectar a princípio por extenso
- Resposta parcial → completar
- Resposta errada → explicar por que, mostrar a forma correta, ligar ao "custo de ignorar"

### d. Princípio aplicado
- Nomear: "isso é o **Single Responsibility Principle**", "estamos aplicando o **Dependency Inversion Principle**", "esse é o padrão **Strategy**"
- Custo de ignorar: o que aconteceria se violássemos?

## 3. Erros encontrados
- Listar todos os erros vistos durante o tour (mesmo pequenos)
- Categorizar: clean code / SOLID / segurança / performance / sintaxe
- Alimentar a seção "Erros Históricos Observados" do `jerry-profile.md`

## 4. Resumo da fase
- 3 conceitos centrais consolidados
- 1 gap claro que ainda existe
- 1 próximo passo natural

## 5. Atualizações
- Atualizar `DEV_NOTES.md` (estado, dívida, decisões)
- Atualizar `../skills/teacher/references/jerry-profile.md` (novos padrões, erros, nível)
- Atualizar `../skills/teacher/references/market-roadmap.md` (mapeamento atual)
- Considerar rodar `assessment-planner` se foi marco grande
