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
Controller (REST API)
↓
Service (Regras de negócio)
↓
Repository (Acesso a dados)
↓
Database (H2/PostgreSQL)

text

### Estrutura de Pacotes
br.com.projeto/
├── controller/
│ └── ProductController.java
├── service/
│ └── ProductService.java
├── repository/
│ └── ProductRepository.java
├── model/
│ └── Product.java
└── config/
└── DataLoader.java

text

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

## 🔧 Como Executar

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
Acessos
API: http://localhost:8080/api/products

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa

Password: (deixe em branco)

📝 Exemplos de Uso
Criar um produto
bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Notebook Dell", "price": 4500.00}'
Listar todos os produtos
bash
curl http://localhost:8080/api/products
Buscar produto por ID
bash
curl http://localhost:8080/api/products/1
Buscar produtos por preço máximo
bash
curl "http://localhost:8080/api/products/search/price?maxPrice=1000.00"
Atualizar produto
bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Notebook Dell Ultra", "price": 5200.00}'
Deletar produto
bash
curl -X DELETE http://localhost:8080/api/products/2
🗄️ Banco de Dados
Configuração (application.yml)
yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: ''
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    
  h2:
    console:
      enabled: true
      path: /h2-console
DataLoader (Dados Iniciais)
O sistema carrega automaticamente 5 produtos de exemplo ao iniciar:

java
- Smartphone iPhone 15 (Ultra) - R$ 5.999,99
- Notebook Dell XPS - R$ 12.999,99
- Mouse Logitech MX - R$ 399,99
- Teclado Mecânico - R$ 499,99
- Monitor 4K 32 - R$ 2.499,99
🧪 Testes com Postman
Importe a collection abaixo no Postman:

json
{
  "info": {
    "name": "Product API - CRUD Completo",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Listar Todos",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/products"
      }
    },
    {
      "name": "Criar Produto",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"name\": \"Monitor 4K\", \"price\": 1800.00}"
        },
        "url": "http://localhost:8080/api/products"
      }
    }
  ]
}
📚 Documentação da Arquitetura
A arquitetura completa está documentada no arquivo ADR-001 (Architecture Decision Record), que inclui:

✅ Justificativas das decisões técnicas

✅ Alternativas consideradas

✅ Consequências positivas e negativas

✅ Padrões de implementação

✅ Guias de migração e boas práticas

🔄 Migração para Produção (PostgreSQL)
Adicionar dependência:

xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
Configurar application-prod.yml:

yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: validate
Executar com profile de produção:

bash
java -jar app.jar --spring.profiles.active=prod
📊 Logs e Monitoramento
Logs SQL habilitados
yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
🤝 Como Contribuir
Faça um fork do projeto

Crie sua feature branch (git checkout -b feature/nova-feature)

Commit suas mudanças (git commit -m 'feat: nova feature')

Push para a branch (git push origin feature/nova-feature)

Abra um Pull Request

📖 Referências
Spring Data JPA Documentation

Hibernate ORM Documentation

Spring Boot Reference Guide

Baeldung JPA Series

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
