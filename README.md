# [JPA e Acesso a dados] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JPA](https://img.shields.io/badge/JPA-Hibernate-orange.svg)](https://hibernate.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

API RESTful para gerenciamento de produtos, desenvolvida com Spring Boot 3.4+ e Java 21. O projeto demonstra uma arquitetura de camadas bem definida utilizando Spring Data JPA para persistência de dados.

### 🎯 Objetivo

Fornecer uma referência técnica para construção de APIs CRUD robustas, com foco em:
- **Baixo acoplamento** entre camadas
- **Código limpo** e manutenível
- **Testabilidade** facilitada
- **Produtividade** com Spring Data JPA

## 🏗️ Arquitetura

### Camadas do Sistema
- Controller (REST API) ↓
- Service (Regras de negócio) ↓
- Repository (Acesso a dados) ↓
- Database (H2/PostgreSQL)



### Estrutura de Pacotes

br.com.projeto/

controller/
    ProductController.java
  
  service/
    ProductService.java
  
  repository/
    ProductRepository.java
  
  model/
    Product.java
  
  config/
    DataLoader.java



## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-------------|
| Java | 21 | Linguagem de programação |
| Spring Boot | 3.4+ | Framework principal |
| Spring Data JPA | 3.4+ | Abstração para persistência |
| Hibernate | 6.x | ORM (Object-Relational Mapping) |
| H2 Database | 2.2+ | Banco em memória (desenvolvimento) |
| Lombok | - | Redução de código boilerplate |
| Maven | - | Gerenciador de dependências |



## 📦 Funcionalidades

### Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-------------|
| GET | `/api/products` | Lista todos os produtos |
| GET | `/api/products/{id}` | Busca produto por ID |
| GET | `/api/products/search/price?maxPrice={valor}` | Busca produtos por preço máximo |
| POST | `/api/products` | Cria um novo produto |
| PUT | `/api/products/{id}` | Atualiza um produto existente |
| DELETE | `/api/products/{id}` | Remove um produto |


## 📦 Exemplos de Requisição e Resposta

**POST /api/products**

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
    "mensagem": "Produto criado com sucesso"
}
```


---

**GET /api/products**

Resposta (200 OK):
```json
{
    "mensagem": "Lista de produtos recuperada"
}
```

---

**GET /api/products/{id}**

Resposta (200 OK):
```json
{
    "mensagem": "Produto encontrado"
}
```

Erro (404 Not Found):
```json
{
    "erro": "Produto não encontrado",
    "status": 404
}
```

---

**PUT /api/products/{id}**

Requisição:
```json
{
"name": "Produto Atualizado",
"price": 299.99
}
```

Resposta (200 OK):
```json
{
    "mensagem": "Produto atualizado com sucesso"
}
```

**DELETE /api/products/{id}**
```bash
   Response (204 No Content) - sem corpo
```


## Como Executar:

### Pré-requisitos

- JDK 21 ou superior
- Maven 3.6+
- Git

### Passos para execução

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/product-api.git

# Entre no diretório
cd product-api

# Execute a aplicação
./mvnw spring-boot:run
```

Acessos:

API: http://localhost:8080/api/products

H2 Console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (deixe em branco)


##DataLoader (Dados Iniciais)
O sistema carrega automaticamente 5 produtos de exemplo ao iniciar:

java
- Smartphone iPhone 15 (Ultra) - R$ 5.999,99
- Notebook Dell XPS - R$ 12.999,99
- Mouse Logitech MX - R$ 399,99
- Teclado Mecânico - R$ 499,99
- Monitor 4K 32 - R$ 2.499,99

🧪 Testes com Postman
Importe a collection abaixo no Postman:

```json
{
    "info": {
        "name": "Products API - CRUD Completo",
        "description": "Collection para testes da API de Produtos com DTO, Mapper e respostas padronizadas",
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
                    "raw": "{\n    \"name\": \"Teclado Mecânico RGB\",\n    \"price\": 350.00\n}"
                },
                "url": "http://localhost:8080/api/products"
            }
        },
        {
            "name": "GET - Listar Todos",
            "request": {
                "method": "GET",
                "url": "http://localhost:8080/api/products"
            }
        },
        {
            "name": "GET - Buscar por ID",
            "request": {
                "method": "GET",
                "url": "http://localhost:8080/api/products/1"
            }
        },
        {
            "name": "GET - Buscar por Preço",
            "request": {
                "method": "GET",
                "url": "http://localhost:8080/api/products/search/price?maxPrice=500.0"
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
                    "raw": "{\n    \"name\": \"Teclado Mecânico Customizado\",\n    \"price\": 450.00\n}"
                },
                "url": "http://localhost:8080/api/products/1"
            }
        },
        {
            "name": "DELETE - Remover Produto",
            "request": {
                "method": "DELETE",
                "url": "http://localhost:8080/api/products/2"
            }
        }
    ]
}
```

📚 Documentação da Arquitetura
A arquitetura completa está documentada no arquivo ADR-001 (Architecture Decision Record), que inclui:

✅ Justificativas das decisões técnicas
✅ Alternativas consideradas
✅ Consequências positivas e negativas
✅ Padrões de implementação


📖 Referências

Spring Data JPA Documentation
Hibernate ORM Documentation
Spring Boot Reference Guide
Baeldung JPA Series

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
