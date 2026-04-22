# PROJECT_GUIDE.md — Vitrine

## Visão Geral
Marketplace de e-commerce, desenvolvido com foco em aprendizado profundo de Java e arquitetura backend.
Inspirado na abordagem do Liferay (modularização, separação de camadas, service layer), **sem** OSGi, JSP ou Spring Boot.
Projeto preparado para migração futura para Spring e desenvolvimento de frontend.

---

## Stack Definida
| Camada | Tecnologia |
|---|---|
| API REST | JAX-RS |
| ORM | Hibernate 6.6 / JPA |
| Container | Apache Tomcat (ou similar) |
| Build | Gradle (multi-módulo) |
| Frontend | React (escopo futuro) |

---

## Arquitetura (Multi-Módulo)
```
vitrine/
├── vitrine-api/          # Modelos de domínio, interfaces de repositório, contratos públicos
├── vitrine-service/      # Lógica de negócio (implementações dos serviços)
├── vitrine-persistence/  # Implementações dos repositórios, config Hibernate
└── vitrine-web/          # Endpoints JAX-RS, configuração Tomcat/deployment
```

### Dependências entre módulos
- `vitrine-persistence` → `vitrine-api`
- `vitrine-service` → `vitrine-api` + `vitrine-persistence`
- `vitrine-web` → `vitrine-api` + `vitrine-service`

---

## Domínio

### Entidades
| Entidade | Descrição |
|---|---|
| `Customer` | Comprador (nome, email, CPF, endereço) |
| `Category` | Categoria de produto (ex: Eletrônicos, Roupas) |
| `Product` | Produto à venda (nome, descrição, preço, categoria) |
| `Stock` | Estoque de um produto (quantity disponível) |
| `Order` | Pedido de um cliente (status, data, lista de itens) |
| `OrderItem` | Item dentro de um pedido (produto, quantidade, preço fixado) |
| `Payment` | Pagamento de um pedido (método, status, valor) |

### Enums
| Enum | Valores |
|---|---|
| `OrderStatus` | PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED |
| `PaymentMethod` | CREDIT_CARD, DEBIT_CARD, PIX, BOLETO |
| `PaymentStatus` | PENDING, APPROVED, REJECTED, REFUNDED |

### Regras de negócio
- Estoque é **reservado** ao fazer pedido, **deduzido** ao confirmar pagamento, **liberado** ao cancelar
- Pedido não pode ser feito se quantidade solicitada > estoque disponível
- `unitPrice` em `OrderItem` é fixado no momento da compra

---

## Diretrizes de Desenvolvimento

- **Claude guia, o usuário executa** — Claude nunca cria, altera ou remove arquivos por conta própria, exceto quando o usuário pedir explicitamente
- Todo arquivo criado, removido ou alterado deve ter o **porquê** explicado antes — qual princípio, padrão ou decisão arquitetural justifica a ação
- O projeto segue **Clean Code** e **Design Patterns** de forma explícita e consciente — não por acidente
- Cada camada tem responsabilidade única (SRP); nenhuma camada conhece os detalhes internos da outra (DIP)
- Consulte `DEV_NOTES.md` para referência rápida de princípios e padrões aplicados

---

## Estado Atual

### Concluído
- [x] Estrutura Gradle multi-módulo (`settings.gradle`, `build.gradle` raiz e por módulo)
- [x] Modelos de domínio em `vitrine-api/model/`: `Customer`, `Category`, `Product`, `Stock`, `Order`, `OrderItem`, `Payment`
- [x] Enums: `OrderStatus`, `PaymentMethod`, `PaymentStatus`
- [x] Configuração Hibernate: `persistence.xml`, `database.properties`, `HibernateUtil.java`
- [x] Interfaces de repositório em `vitrine-api/repository/`: `CustomerRepository`, `CategoryRepository`, `ProductRepository`, `StockRepository`, `OrderRepository`, `PaymentRepository`
- [x] Implementações dos repositórios em `vitrine-persistence/repository/impl/`

### Em andamento
- [ ] Interfaces de serviço em `vitrine-api/service/`
- [ ] Implementações dos serviços em `vitrine-service/`

---

## Roadmap

### Fase 1 — Fundação ✅
- [x] Modelagem de domínio (entidades, relacionamentos, regras)
- [x] Estrutura Gradle multi-módulo
- [x] Modelos de domínio em `vitrine-api/model/`
- [x] Configuração Hibernate (`persistence.xml`, `HibernateUtil`)
- [x] Interfaces de repositório em `vitrine-api/repository/`
- [x] Implementações dos repositórios (`*RepositoryImpl`) em `vitrine-persistence/repository/impl/`

### Fase 2 — Camada de Serviço
- [ ] Interfaces de serviço em `vitrine-api/service/` (`CustomerService`, `ProductService`, `OrderService`, `PaymentService`)
- [ ] Implementações em `vitrine-service/`
- [ ] Regras de negócio (reserva/dedução/liberação de estoque, validação de pedido, fluxo de pagamento)

### Fase 3 — Camada Web
- [ ] Configuração JAX-RS
- [ ] DTOs de request/response em `vitrine-api/dto/`
- [ ] Endpoints REST (`CustomerResource`, `ProductResource`, `OrderResource`, `PaymentResource`)
- [ ] Tratamento de erros global (ExceptionMapper)
- [ ] Deploy no Tomcat

### Fase 4 — Frontend
- [ ] Setup React
- [ ] Telas: listagem de produtos, carrinho, checkout, pedidos, painel admin

### Fase 5 — Qualidade e Evolução
- [ ] Testes unitários (camada de serviço)
- [ ] Testes de integração (repositórios)
- [ ] Autenticação JWT
- [ ] Migração para Spring Boot (Spring Data JPA + Spring MVC)
