# [API Rest, DTO, Mapper e Http Response] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-orange.svg)](https://maven.apache.org/)


# 📋 Sobre o Projeto
API RESTful de alto nível para gerenciamento de produtos, implementando as melhores práticas de arquitetura com Spring Boot 3: **DTOs**, **Mapper**, **respostas HTTP semânticas** e **padronização de envelopes**.


## Contexto

O sistema precisa expor uma API RESTful para operações de CRUD sobre recursos do domínio (ex.: produtos). Foi identificado que:

1. A exposição direta de entidades JPA para o cliente acoplava fortemente o contrato da API ao modelo de persistência.
2. Alterações no banco de dados (adição/remoção de campos) poderiam quebrar os clientes da API.
3. Não havia padronização nas respostas de sucesso e erro, dificultando o tratamento no frontend.
4. Os status HTTP eram utilizados de forma limitada, sem explorar códigos semânticos como `201 Created` com header `Location`.
5. A lógica de conversão entre entidade e objeto de transporte ficava espalhada nos controllers ou services, gerando duplicação.

## Decisão

Adotar uma arquitetura de **quatro camadas** com os seguintes componentes obrigatórios:

- **DTOs (Data Transfer Objects)**: classes separadas para requisição (`*RequestDTO`) e resposta (`*ResponseDTO`).
- **Mapper**: componente dedicado (`@Component`) para conversão entre entidade e DTOs.
- **Envelopes de resposta padronizados**: classes `ApiSucesso<T>` e `ApiErro` com timestamp, mensagem e dados/erros.
- **Uso semântico do HTTP**: `201 Created` com header `Location` no POST, `204 No Content` no DELETE, `200 OK` nos demais.
- **Separação de responsabilidades**:
  - Controller → orquestra requisição/resposta, validação e conversão com Mapper.
  - Service → lógica de negócio e acesso ao repositório.
  - Repository → apenas persistência (Spring Data JPA).
  - Entidade → sem exposição externa.

**Exemplo do fluxo implementado:**  
`Requisição (JSON)` → `ProdutoRequestDTO` → `Mapper.toEntity()` → `Produto (entidade)` → `Repository.save()` → `Mapper.toResponse()` → `ApiSucesso<ProdutoResponseDTO>` → `Resposta (JSON)`

## Consequências

### Positivas

- **Contrato estável**: mudanças na entidade não afetam a API, apenas os DTOs e mappers precisam ser ajustados.
- **Reutilização de conversão**: o Mapper centraliza a lógica de transformação, evitando duplicação.
- **Padronização frontend**: envelopes `ApiSucesso` e `ApiErro` permitem tratamento genérico no cliente.
- **Semântica HTTP**: melhora a usabilidade da API (header `Location` indica onde o recurso foi criado).
- **Segurança**: campos sensíveis da entidade (ex.: senha, versão de lock) não são expostos por engano.

### Negativas

- **Aumento de classes**: cada recurso requer pelo menos 2 DTOs, 1 Mapper e 2 envelopes, além da entidade e service.
- **Overhead de mapeamento**: pequena perda de desempenho devido à conversão objeto ↔ objeto (considerada insignificante para a maioria dos cenários).
- **Curva de aprendizado**: novos desenvolvedores precisam entender o padrão DTO/Mapper.

## Alternativas Consideradas

| Alternativa | Motivo da rejeição |
|-------------|---------------------|
| Expor entidades JPA diretamente nos controllers | Alto acoplamento entre API e schema do banco; quebra fácil do contrato. |
| Usar `ModelMapper` ou `MapStruct` sem camada dedicada | Reduz a clareza e a centralização; MapStruct exige configuração adicional e gera código que pode dificultar debug. |
| Retornar apenas o recurso criado (sem envelope) no POST | Falta padronização; frontend precisaria tratar diferentes formatos de sucesso/erro. |
| Usar apenas `200 OK` para todas as operações | Viola a semântica HTTP; cliente não consegue diferenciar criação, atualização ou deleção apenas pelo status. |


---

## 🏗️ Arquitetura

O projeto adota uma arquitetura de quatro camadas com separação rigorosa de responsabilidades:

**Camada de Apresentação:** Controller (REST) + DTOs (Request/Response) + Envelopes

**Camada de Aplicação:** Service (Regras de Negócio) + Mapper

**Camada de Persistência:** Repository (Spring Data JPA) + Entidades JPA



## 🔒 Blindagem da Entidade

As entidades JPA nunca são expostas diretamente nas respostas da API. Toda comunicação externa ocorre via DTOs:

Cliente → ProdutoRequestDTO → Mapper → Produto (Entidade) → Repository

Resposta (ProdutoResponseDTO + ApiSucesso) ← Mapper ← Produto (Entidade) Salvo


---

## 📜 Decisões Arquiteturais (ADR)

Esta implementação segue a **ADR-001** documentada no projeto. As principais decisões foram:

| Decisão | Benefício |
|---------|-----------|
| Uso de DTOs para entrada/saída | Contrato estável, desacoplado do modelo de persistência |
| Camada Mapper dedicada | Centralização da lógica de conversão |
| Envelopes `ApiSucesso` e `ApiErro` | Padronização das respostas para o frontend |
| Status HTTP semânticos (`201 Created` com `Location`) | Melhor usabilidade da API |
| Separação Controller → Service → Repository | Manutenibilidade e testabilidade |


📄 **Documento completo da ADR:** [ADR-001](./docs/adr-001-dto-mapper-http.md)

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|-------------|
| Java | 21 LTS | Linguagem base |
| Spring Boot | 3.4+ | Framework principal |
| Spring Data JPA | 3.4+ | Persistência e repositórios |
| Spring Web | 3.4+ | APIs REST |
| H2 Database | (última) | Banco em memória (desenvolvimento) |
| Lombok | 1.18.30+ | Redução de boilerplate |
| Bean Validation (Jakarta) | 3.0+ | Validação de dados |

---

## ⚙️ Pré-requisitos

- Java 21 (JDK 21 ou superior)
- Maven 3.9+
- Git (opcional, para clonar o repositório)
- Postman ou cURL (para testar a API)

---

## 🚀 Configuração e Execução

3. Executar o projeto
bash
mvn spring-boot:run

4. Acessar o console H2 (em desenvolvimento)

Abra no navegador: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (deixe em branco)

📍 Endpoints da API
Base URL: http://localhost:8080/api/produtos

Método	Endpoint	Descrição	Status HTTP	Header Location
- POST	/	Criar um novo produto	201 Created	/api/produtos/{id}
- GET	/	Listar todos os produtos	200 OK	-
- GET	/{id}	Buscar produto por ID	200 OK	-
- PUT	/{id}	Atualizar produto existente	200 OK	-
- DELETE	/{id}	Remover produto	204 No Content	-

## 📦 Exemplos de Requisição e Resposta

**POST /api/produtos - Criar Produto**

Requisição:
```json
{
    "nome": "Teclado Mecânico RGB",
    "preco": 350.00
}
```

Resposta (201 Created):
```json
{
    "mensagem": "Produto criado com sucesso",
    "dados": {
        "id": 1,
        "nome": "Teclado Mecânico RGB",
        "preco": 350.00
    },
    "timestamp": "2025-04-07T14:30:00"
}
```

Header Location: `Location: http://localhost:8080/api/produtos/1`

---

**GET /api/produtos - Listar Todos**

Resposta (200 OK):
```json
{
    "mensagem": "Lista de produtos recuperada",
    "dados": [
        {
            "id": 1,
            "nome": "Teclado Mecânico RGB",
            "preco": 350.00
        },
        {
            "id": 2,
            "nome": "Mouse Wireless Pro",
            "preco": 220.00
        }
    ],
    "timestamp": "2025-04-07T14:31:00"
}
```

---

**GET /api/produtos/1 - Buscar por ID**

Resposta (200 OK):
```json
{
    "mensagem": "Produto encontrado",
    "dados": {
        "id": 1,
        "nome": "Teclado Mecânico RGB",
        "preco": 350.00
    },
    "timestamp": "2025-04-07T14:32:00"
}
```

Erro (404 Not Found):
```json
{
    "erro": "Produto não encontrado",
    "status": 404,
    "detalhes": ["Produto não encontrado com o ID: 999"],
    "timestamp": "2025-04-07T14:33:00"
}
```

---

**PUT /api/produtos/1 - Atualizar Produto**

Requisição:
```json
{
    "nome": "Teclado Mecânico Customizado",
    "preco": 450.00
}
```

Resposta (200 OK):
```json
{
    "mensagem": "Produto atualizado com sucesso",
    "dados": {
        "id": 1,
        "nome": "Teclado Mecânico Customizado",
        "preco": 450.00
    },
    "timestamp": "2025-04-07T14:34:00"
}
```

---

**DELETE /api/produtos/1 - Remover Produto**

Resposta (204 No Content): (sem corpo na resposta)


## 🧪 Testando com Postman

Para importar a collection no Postman:

1. Abra o Postman
2. Clique em Import → Raw text
3. Cole o JSON abaixo e clique em Continue → Import

**Collection JSON:**
```json
{
    "info": {
        "name": "API Produtos - Arquitetura em Camadas",
        "description": "Collection para testes do CRUD de Produtos com blindagem por DTO e Mapper.",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "item": [
        {
            "name": "POST - Criar Produto",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Content-Type",
                        "value": "application/json"
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": "{\n    \"nome\": \"Cadeira Ergonômica Pro\",\n    \"preco\": 1250.00\n}"
                },
                "url": "http://localhost:8080/api/produtos"
            }
        },
        {
            "name": "GET - Listar Todos",
            "request": {
                "method": "GET",
                "url": "http://localhost:8080/api/produtos"
            }
        },
        {
            "name": "GET - Buscar por ID",
            "request": {
                "method": "GET",
                "url": "http://localhost:8080/api/produtos/1"
            }
        },
        {
            "name": "PUT - Atualizar Produto",
            "request": {
                "method": "PUT",
                "header": [
                    {
                        "key": "Content-Type",
                        "value": "application/json"
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": "{\n    \"nome\": \"Cadeira Ergonômica Ultra\",\n    \"preco\": 1400.00\n}"
                },
                "url": "http://localhost:8080/api/produtos/1"
            }
        },
        {
            "name": "DELETE - Remover Produto",
            "request": {
                "method": "DELETE",
                "url": "http://localhost:8080/api/produtos/2"
            }
        }
    ]
}
```

## 📁 Estrutura do Projeto

src/
  main/
    java/com/projeto/arquitetura/
      controller/
        ProdutoController.java (Endpoints REST)
      service/
        ProdutoService.java (Regras de negócio)
      repository/
        ProdutoRepository.java (Acesso a dados)
      model/
        Produto.java (Entidades JPA)
      dto/
        request/
          ProdutoRequestDTO.java
        response/
          ProdutoResponseDTO.java
          ApiSucesso.java
          ApiErro.java
      mapper/
        ProdutoMapper.java (Conversores Entidade ↔ DTO)
      config/
        DataLoader.java (Configurações e dados iniciais)
    resources/
      application.properties
  test/ (Testes unitários e integração)


---


📊 Validações Implementadas
Campo	Validação	Mensagem de erro
nome	@NotBlank	"O nome do produto é obrigatório"
preco	@Positive	"O preço deve ser um valor positivo"

🔄 DataLoader (Dados Iniciais)

Ao iniciar a aplicação, o DataLoader executa automaticamente:

Insere produtos de exemplo
Testa o ciclo completo do CRUD
Deixa o banco populado para testes manuais
Produtos carregados automaticamente:
Monitor UltraWide 34' - R$ 2800,00
Headset Gamer 7.1 - R$ 450,00

📝 Licença
Este projeto está licenciado sob os termos da licença MIT.

👥 Autores
Equipe de Arquitetura - Decisões arquiteturais e implementação

📚 Referências

ADR-001 - Documento de Decisão Arquitetural
Spring Boot Documentation
REST API Best Practices

## Referências
- Ebook interno: "[API Rest, DTO, Mapper e Http Response] - Arquitetura de Sistemas com Spring Boot 3.4+"
- [Spring Boot Reference – REST Repositories](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-mvc)
- [Jakarta Bean Validation](https://beanvalidation.org/)

*Última atualização: Abril/2025*

Este `README.md` está pronto para ser adicionado à raiz do projeto. Ele documenta desde a arquitetura até exemplos práticos de uso, incluindo a referência à ADR criada anteriormente.
