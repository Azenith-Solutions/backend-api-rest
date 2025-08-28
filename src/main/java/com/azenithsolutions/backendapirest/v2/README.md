# Exemplo de Clean Architecture – Módulo Order (v2)

> Objetivo: Servir como guia prático para replicar a mesma estrutura (ports & adapters + use cases) em novos contextos do projeto.

## 🎯 Visão Geral
A versão **v2** introduz uma implementação de **Clean Architecture** focada em isolamento do domínio, independência de frameworks e facilidade de evolução. O módulo `Order` foi utilizado como experimento/blueprint.

### Metas
- Separar domínio de detalhes de infraestrutura (JPA, Web, Swagger, Segurança)
- Facilitar testes unitários (mock apenas de portas)
- Permitir múltiplas implementações de persistência sem alterar regras de negócio
- Tornar o fluxo de uma requisição legível e explícito

### Camadas (Mapeamento)
| Camada | Objetivo | Pasta | Exemplo Chave |
|--------|----------|-------|---------------|
| Domain (Enterprise Business Rules) | Modelo rico e estável | `core/domain/model` | `Order`, `OrderStatus` |
| Ports (Interfaces) | Contratos que o domínio exige | `core/domain/repository` | `OrderRepositoryPort` |
| Use Cases (Application Business Rules) | Orquestram regras e portas | `core/usecase/order` | `CreateOrderUseCase` |
| Infrastructure – Persistence | Implementa portas (adapters) | `infrastructure/persistence` | `OrderRepositoryAdapter`, `OrderEntity` |
| Infrastructure – Web (Delivery) | Exposição HTTP (controllers / DTO) | `infrastructure/web` | `OrderController`, `OrderRest` |
| Configuration | Montagem dos casos de uso | `infrastructure/config` | `OrderUseCaseConfig` |

> Regra de dependência: setas sempre de fora para dentro. Camadas externas conhecem internas; internas não conhecem externas.

## 🧬 Modelo de Domínio
Arquivo: `core/domain/model/Order.java`
- Responsável apenas por representar o estado e regras internas (no momento, estado + timestamps).
- Independente de anotações JPA / Spring.
- Evolução futura: adicionar value objects (ex: `Email`, `Money`) e invariantes (ex: validar formato de CNPJ).

Enum: `core/domain/model/enums/OrderStatus.java` – restrições de valores válidos.

## 🔌 Porta (Output Port)
Arquivo: `core/domain/repository/OrderRepositoryPort.java`
```java
public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
```
- O domínio e os use cases dependem **apenas** desta interface.
- Qualquer detalhe de banco/JPA é invertido para a implementação do adapter.

## 🧠 Use Cases
Local: `core/usecase/order`
Cada caso de uso é uma classe pequena com um único método público `execute(...)`.

| Use Case | Responsabilidade | Entrada | Saída | Observações |
|----------|------------------|---------|-------|-------------|
| `CreateOrderUseCase` | Criar pedido | `Order` | `Order` | Define timestamps iniciais (se aplicado) |
| `UpdateOrderUseCase` | Atualizar campos mutáveis | id + `Order` | `Order` | Preserva `createdAt`, atualiza `updatedAt` |
| `GetOrderByIdUseCase` | Buscar um | id | `Order` | Lança `NoSuchElementException` se não encontrado |
| `ListOrdersUseCase` | Listar todos | – | `List<Order>` | Livre de infra |
| `DeleteOrderUseCase` | Remover | id | void | Verifica existência |

Exemplo resumido (fluxo de update):
```java
public Order execute(Long id, Order newOrder) {
    Order existing = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    existing.setCodigo(newOrder.getCodigo());
    existing.setNomeComprador(newOrder.getNomeComprador());
    // ... demais campos mutáveis
    existing.setUpdatedAt(LocalDateTime.now());
    return repository.save(existing);
}
```
> A lógica de preservação de `createdAt` e atualização granular fica aqui – fora de controllers e repositórios, **ou seja, no caso de uso de atualização (update), o valor de `createdAt` não é alterado**.

## 🧱 Adapter de Persistência
Local: `infrastructure/persistence/adapter/OrderRepositoryAdapter.java`
Funções:
1. Converte Domain ↔ Entity (`Order` ↔ `OrderEntity`)
2. Delegação para `SpringDataOrderRepository` (interface JPA)
3. Implementa apenas a porta (`OrderRepositoryPort`)

Por que mapear? Para isolar o domínio de anotações JPA (`@Entity`, `@Column`, etc.).

`OrderEntity` contém somente detalhes específicos de persistência.

## 🌐 Camada Web (Delivery)
Local: `infrastructure/web`
- `OrderController` expõe rotas REST (`/v2/orders`) – depende apenas de casos de uso + mapper.
- `OrderRest` (DTO) é o contrato HTTP; evita expor diretamente o domínio (permite evolução interna).
- `OrderRestMapper` traduz Domain ↔ DTO.

Exemplo de fluxo (POST /v2/orders):
1. Controller recebe JSON → desserializa em `OrderRest`.
2. Mapper converte `OrderRest` → `Order` (domínio).
3. `CreateOrderUseCase.execute(order)` executa regras.
4. Retorno `Order` é convertido para `OrderRest` novamente.
5. Response HTTP 201 enviado.

## 🔄 Sequência de Requisição (Exemplo GET /v2/orders/{id})
```
[HTTP Request]
   ↓
OrderController.getById(id)
   ↓ (chama) GetOrderByIdUseCase.execute(id)
   ↓ (usa) OrderRepositoryPort.findById(id)
   ↓ Adapter → SpringDataOrderRepository.findById(id)
   ↓ JPA → Banco de Dados
   ↑ Entity mapeada
Adapter converte Entity → Domain
Use Case retorna Domain
Controller mapeia Domain → DTO (OrderRest)
HTTP 200 + JSON
```

## 🏗️ Configuração (Composition Root)
Arquivo: `infrastructure/config/OrderUseCaseConfig.java`
- Define beans explicitamente dos use cases.
- Injeta a porta (`OrderRepositoryPort`) – que na prática é satisfeita pelo adapter.
- Ponto único onde Spring conhece as classes de aplicação; o core continua puro.

## 🧪 Testabilidade
Para testar um caso de uso:
- Mock `OrderRepositoryPort`
- Instanciar o use case diretamente (sem subir contexto Spring)

Exemplo (pseudo):
```java
var repo = mock(OrderRepositoryPort.class);
var useCase = new UpdateOrderUseCase(repo);
when(repo.findById(1L)).thenReturn(Optional.of(existingOrder));
when(repo.save(existingOrder)).thenReturn(existingOrder);
var result = useCase.execute(1L, updatedOrderData);
assertEquals(updatedOrderData.getCodigo(), result.getCodigo());
```
Nenhum framework de web/persistência é necessário – objetivo atingido.

## ♻️ Como Replicar para Outro Agregado (Ex: Product)
1. Criar domínio: `Product` + enums/value objects
2. Criar porta: `ProductRepositoryPort`
3. Criar casos de uso (CRUD ou específicos) em `core/usecase/product`
4. Criar entity JPA `ProductEntity`
5. Criar repository Spring Data (ex: `SpringDataProductRepository`)
6. Implementar adapter `ProductRepositoryAdapter` (mapear Domain ↔ Entity)
7. Criar DTO(s) HTTP + mapper (ex: `ProductRest`, `ProductRestMapper`)
8. Criar controller `ProductController` em `/v2/products`
9. Registrar beans de use case (config)
10. Adicionar documentação OpenAPI (tags + operations)
11. Escrever testes de use case (mock da porta) antes de integrar

Checklist de Qualidade:
- Domínio sem anotações de framework
- Controller sem lógica de negócio
- Adapter sem regras de negócio (apenas mapeamento)
- Use case sem classes de infra (apenas porta)
- Fluxo validado com testes unitários

## 🔍 Decisões de Design Notáveis
| Decisão | Motivo | Trade-off |
|---------|-------|-----------|
| Porta única (Command + Query) | Simplificação inicial | Pode ser dividida depois (ISP) |
| DTO separado (`OrderRest`) | Estabilidade de contrato | Duplicação de campos |
| Mapeamento manual | Controle explícito | Mais código |
| Atualização campo-a-campo | Preservar `createdAt` + clareza | Necessita manutenção ao adicionar campos |
| Bean name explícito do controller v2 | Evitar conflito com v1 | Ideal renomear classe futuramente |

## 🚩 Pontos de Evolução
- Dividir `OrderRepositoryPort` em `OrderQueryPort` / `OrderCommandPort`
- Introduzir Value Objects (telefone, email, dinheiro)
- Eventos de domínio para side effects (ex: enviar email após criação)
- Exceptions específicas (`OrderNotFoundException`) + handler v2
- Testes de contrato (component tests) na camada web

## 🧭 Guia Rápido (Mental Map)
```
Controller (HTTP) → Mapper → Use Case → Porta → Adapter → Spring Data → DB
                     ↑                                 ↓
                  DTO/JSON ← Mapper ← Domain ← Adapter ← Entity
```

## 📌 Exemplos de Payload
Request POST `/v2/orders`:
```json
{
  "codigo": "ORD-2025-0007",
  "nomeComprador": "Joao Silva",
  "emailComprador": "joao.silva@example.com",
  "cnpj": "12.345.678/0001-99",
  "valor": "1500.00",
  "status": "PENDENTE",
  "telCelular": "+55 11 98877-6655"
}
```
Response 201:
```json
{
  "id": 42,
  "codigo": "ORD-2025-0007",
  "nomeComprador": "Joao Silva",
  "emailComprador": "joao.silva@example.com",
  "cnpj": "12.345.678/0001-99",
  "valor": "1500.00",
  "status": "PENDENTE",
  "telCelular": "+55 11 98877-6655",
  "createdAt": "2025-05-25T10:15:30",
  "updatedAt": "2025-05-25T10:15:30"
}
```

## ✅ Resumo Essencial
- Domínio limpo + portas → desacoplamento real
- Use cases são a fonte da verdade das regras
- Adapters convertem e isolam frameworks
- Controller só orquestra entrada/saída
- Fácil de testar, evoluir e replicar
