# DEV_NOTES.md — Vitrine

Arquivo de consulta rápida: padrões, princípios e decisões arquiteturais do projeto.

---

## Princípios SOLID aplicados

### S — Single Responsibility Principle (SRP)
Cada classe tem uma única razão para mudar.
- `HibernateUtil` → só cria e fornece o `EntityManagerFactory`
- Repositórios → só acessam dados
- Serviços → só executam lógica de negócio
- Endpoints → só recebem e respondem requisições HTTP

### D — Dependency Inversion Principle (DIP)
Módulos de alto nível não dependem de módulos de baixo nível — ambos dependem de abstrações (interfaces).
- `vitrine-service` depende de interfaces de repositório, não do Hibernate diretamente
- Isso permite trocar a implementação (ex: de Hibernate para Spring Data JPA) sem tocar na lógica de negócio

---

## Design Patterns

### Repository Pattern
Abstrai o acesso ao banco de dados atrás de uma interface.
- **Interface** (contrato): define os métodos disponíveis (`findById`, `save`, etc.) — fica em `vitrine-api`
- **Implementação**: usa o `EntityManager` do Hibernate para executar as queries — fica em `vitrine-persistence`
- **Benefício**: a camada de serviço não sabe como os dados são buscados — só sabe que pode pedir

### Por que as interfaces ficam em `vitrine-api`
As interfaces de repositório e os modelos de domínio ficam em `vitrine-api` para que a camada de serviço
dependa apenas do módulo de API — nunca do Hibernate. Isso garante que, ao migrar para Spring Data JPA,
só as implementações precisam mudar. O contrato (interface) permanece intocado.

---

## Boas Práticas

### `Optional<T>` em vez de `null`
Métodos que podem não encontrar um resultado retornam `Optional<T>`, nunca `null`.
- Força quem chama a tratar o caso "não encontrado" explicitamente
- Elimina `NullPointerException` silenciosos
- Exemplo: `Optional<Customer> findByEmail(String email)`

### `EntityManagerFactory` como Singleton
Criar um `EntityManagerFactory` é uma operação cara (abre pool de conexões, lê configurações).
Deve existir **uma única instância** durante toda a vida da aplicação.
`HibernateUtil` usa um campo `static final` para garantir isso.

### Separação de propriedades de conexão
Credenciais de banco não ficam no `persistence.xml` — ficam em `database.properties`.
- `persistence.xml` → declara entidades e nome da unidade de persistência
- `database.properties` → URL, usuário, senha, configurações do Hibernate
- `database.properties` deve estar no `.gitignore` para não expor credenciais

### `transaction-type="RESOURCE_LOCAL"`
Usado quando não há um servidor de aplicação (JEE container) gerenciando as transações.
No nosso caso usamos Tomcat simples, então gerenciamos as transações manualmente via `EntityTransaction`.

### Rollback em toda operação de escrita
Todo método que altera estado (`save`, `update`, `delete`) deve fazer `rollback` se ocorrer qualquer exceção,
garantindo que o banco nunca fique em estado inconsistente.

---

## Domínio — E-commerce (Vitrine)

### Entidades e relacionamentos
```
Customer   1 ──< Order      (um cliente tem muitos pedidos)
Order      1 ──< OrderItem  (um pedido tem muitos itens, cascade ALL)
OrderItem >── 1 Product     (cada item referencia um produto)
Product   >── 1 Category    (cada produto pertence a uma categoria)
Product    1 ── 1 Stock     (cada produto tem um registro de estoque)
Order      1 ── 1 Payment   (cada pedido tem no máximo um pagamento)
```

### Regras de negócio centrais
- Ao fazer um pedido → estoque é **reservado** (não deduzido ainda)
- Ao confirmar pagamento → estoque é **deduzido** de verdade
- Ao cancelar pedido → estoque é **liberado**
- Não é possível fazer pedido com quantidade maior que o estoque disponível
- O `unitPrice` em `OrderItem` é fixado no momento da compra (imune a mudanças futuras de preço)

---

## Estrutura de Módulos e Dependências

```
vitrine-api          ← modelos de domínio, interfaces de repositório e serviço, DTOs
vitrine-persistence  ← implementações dos repositórios, HibernateUtil
vitrine-service      ← lógica de negócio (depende de vitrine-api)
vitrine-web          ← endpoints JAX-RS (depende de vitrine-api + vitrine-service + vitrine-persistence)
```

**Regra:** camadas superiores nunca importam camadas que estão abaixo delas na cadeia de dependência inversa.
`vitrine-web` nunca importa `vitrine-persistence` diretamente.

## Camada Web — JAX-RS + Jersey

### Poor Man's DI (injeção de dependência manual)
Sem framework de DI (como CDI ou Spring), a classe `AppConfig` instancia manualmente
toda a cadeia: repositórios → serviços → resources, e os registra no Jersey.
- Vantagem: simples, explícito, sem magia
- Onde fica: `vitrine-web/AppConfig.java`

### Códigos HTTP usados
- `201 CREATED` → recurso criado com sucesso (POST)
- `204 NO CONTENT` → operação bem-sucedida sem corpo de resposta (PUT, DELETE)
- `404 NOT FOUND` → recurso não encontrado
- `200 OK` → consulta bem-sucedida

### Rota base
Todas as rotas têm prefixo `/api` (definido em `VitrineApplication` via `@ApplicationPath`).
Exemplo: `POST /api/orders?customerId=1`