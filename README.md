# Vitrine

Marketplace de e-commerce backend desenvolvido em Java puro, com foco em arquitetura em camadas, separação de responsabilidades e boas práticas de engenharia de software.

---

## System Design

```
┌─────────────────────────────────────────────────────┐
│                    vitrine-web                       │
│         JAX-RS Resources  +  Jersey Servlet          │
│   CustomerResource  OrderResource  PaymentResource   │
└───────────────────────┬─────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────┐
│                  vitrine-service                     │
│      CustomerServiceImpl   OrderServiceImpl          │
│      ProductServiceImpl    PaymentServiceImpl        │
└──────────┬────────────────────────────┬─────────────┘
           │                            │
┌──────────▼──────────┐   ┌─────────────▼─────────────┐
│    vitrine-api       │   │    vitrine-persistence     │
│                      │   │                            │
│  Domain Models       │   │  *RepositoryImpl (JPA)     │
│  Repository ifaces   │   │  HibernateUtil             │
│  Service ifaces      │   │  persistence.xml           │
└──────────────────────┘   └─────────────────────────┬─┘
                                                      │
                                          ┌───────────▼──────────┐
                                          │      MySQL 8          │
                                          │  (schema auto-criado) │
                                          └──────────────────────┘
```

### Fluxo de uma requisição

```
HTTP Request
    → Tomcat 10
        → Jersey (JAX-RS)
            → Resource (vitrine-web)
                → Service (vitrine-service)
                    → Repository Interface (vitrine-api)
                        → RepositoryImpl (vitrine-persistence)
                            → Hibernate / JPA
                                → MySQL
```

### Módulos

| Módulo | Responsabilidade |
|---|---|
| `vitrine-api` | Modelos de domínio, interfaces de repositório e serviço — contrato público |
| `vitrine-persistence` | Implementações dos repositórios com Hibernate, `HibernateUtil` |
| `vitrine-service` | Lógica de negócio — orquestra repositórios e aplica regras de domínio |
| `vitrine-web` | Endpoints REST JAX-RS, configuração Jersey, deploy Tomcat |

### Dependências entre módulos

```
vitrine-api          ← base, sem dependências internas
vitrine-persistence  → vitrine-api
vitrine-service      → vitrine-api + vitrine-persistence
vitrine-web          → vitrine-api + vitrine-service + vitrine-persistence
```

---

## Domínio

```
Customer   1 ──< Order      (um cliente tem muitos pedidos)
Order      1 ──< OrderItem  (um pedido tem muitos itens)
OrderItem >── 1 Product     (cada item referencia um produto)
Product   >── 1 Category    (cada produto pertence a uma categoria)
Product    1 ── 1 Stock     (cada produto tem um estoque)
Order      1 ── 1 Payment   (cada pedido tem no máximo um pagamento)
```

### Regras de negócio

- Estoque é **deduzido** no momento do pedido
- Não é possível fazer pedido com quantidade maior que o estoque disponível
- O `unitPrice` em `OrderItem` é fixado no momento da compra — imune a variações futuras de preço
- Pedido cancelado muda de status para `CANCELLED` — nunca é deletado
- Pagamento é imutável após processado

---

## Endpoints REST

Base URL: `http://localhost:8080/vitrine-web/api`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/customers` | Cadastrar cliente |
| `GET` | `/customers` | Listar clientes |
| `GET` | `/customers/{id}` | Buscar cliente por id |
| `PUT` | `/customers/{id}` | Atualizar cliente |
| `DELETE` | `/customers/{id}` | Remover cliente |
| `POST` | `/products?initialStock=N` | Cadastrar produto com estoque |
| `GET` | `/products` | Listar produtos |
| `GET` | `/products/{id}` | Buscar produto por id |
| `GET` | `/products/category/{id}` | Listar produtos por categoria |
| `PUT` | `/products/{id}` | Atualizar produto |
| `DELETE` | `/products/{id}` | Remover produto |
| `POST` | `/orders?customerId=N` | Criar pedido |
| `GET` | `/orders/{id}` | Buscar pedido por id |
| `GET` | `/orders/customer/{id}` | Listar pedidos de um cliente |
| `POST` | `/orders/{id}/cancel` | Cancelar pedido |
| `POST` | `/payments?orderId=N&method=X` | Processar pagamento |
| `GET` | `/payments/order/{id}` | Buscar pagamento por pedido |

---

## Tecnologias

### Implementadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Gradle | 9.x | Build e gerenciamento de módulos |
| Jakarta Persistence (JPA) | 3.2 | API de persistência |
| Hibernate ORM | 6.6 | Implementação JPA |
| JAX-RS | 3.1 | API REST |
| Jersey | 3.1.11 | Implementação JAX-RS |
| Jackson | via Jersey | Serialização/deserialização JSON |
| Apache Tomcat | 10.1.24 | Servlet container |
| MySQL | 8.x | Banco de dados relacional |

### Planejadas

| Tecnologia | Finalidade |
|---|---|
| JUnit 5 + Mockito | Testes unitários da camada de serviço |
| Testcontainers | Testes de integração com banco real |
| JWT | Autenticação e autorização |
| React | Frontend — listagem, carrinho, checkout, painel admin |
| Spring Boot | Migração futura — Spring Data JPA + Spring MVC |
| Docker | Containerização da aplicação e do banco |

---

## Como executar

### Pré-requisitos

- Java 21
- MySQL 8 rodando localmente
- Apache Tomcat 10.1.x

### Configuração do banco

```sql
CREATE DATABASE vitrine;
```

### Configuração da aplicação

Crie o arquivo `modules/vitrine-persistence/src/main/resources/database.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/vitrine?useSSL=false&serverTimezone=UTC
db.user=seu_usuario
db.password=sua_senha
hibernate.hbm2ddl.auto=update
hibernate.show_sql=true
```

### Build

```bash
./gradlew clean build
```

### Deploy

```bash
./gradlew :vitrine-web:war
```

Copie o `.war` gerado em `modules/vitrine-web/build/libs/vitrine-web.war` para a pasta `webapps` do Tomcat, ou use o plugin SmartTomcat no IntelliJ.

---

## Arquitetura e princípios

- **SRP** — cada classe tem uma única razão para mudar
- **DIP** — camadas superiores dependem de interfaces, nunca de implementações concretas
- **Repository Pattern** — acesso a dados abstraído atrás de interfaces em `vitrine-api`
- **Poor Man's DI** — injeção de dependência manual via construtor, sem framework
- **Layered Architecture** — apresentação → serviço → persistência → domínio
