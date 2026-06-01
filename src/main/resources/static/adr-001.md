# ADR-001: Arquitetura de Persistência e Camadas com Spring Boot

---

## METADADOS

| Campo | Valor |
|-------|-------|
| **Data** | 15 de Janeiro de 2024 |
| **Status** | Aceito |
| **Autores** | Equipe de Arquitetura |
| **Versão** | 1.0 |
| **Última Revisão** | 15 de Janeiro de 2024 |
| **Decisores** | Arquitetos de Software, Tech Lead |
| **Impacto** | Alto - Afeta toda camada de persistência |

---

## 1. CONTEXTO

### 1.1 Problema
O projeto necessita de uma API RESTful para operações CRUD de produtos. Os desafios incluem:

- **Alto acoplamento** entre lógica de negócio e acesso a dados
- **Código repetitivo** em operações de banco (SQL boilerplate)
- **Dificuldade em testar** camadas isoladamente
- **Baixa produtividade** em mudanças de esquema de banco
- **Risco de SQL injection** em queries manuais

### 1.2 Cenário
- Aplicação: API REST para gerenciamento de produtos
- Tecnologia base: Spring Boot 3.4+, Java 21
- Operações: CRUD completo (Create, Read, Update, Delete)
- Ambiente: Desenvolvimento (H2) → Produção (PostgreSQL/MySQL)

### 1.3 Restrições
- Prazo curto para entrega do MVP
- Equipe com conhecimento intermediário em JPA
- Necessário suporte a queries customizadas
- Deve permitir testes automatizados

---

## 2. DECISÃO

### 2.1 Escolha Arquitetural
**Adotar Spring Data JPA com Hibernate em arquitetura de 4 camadas:**
Camada 1: Controller (REST API)
↓
Camada 2: Service (Regras de negócio)
↓
Camada 3: Repository (Acesso a dados)
↓
Camada 4: Database (H2/PostgreSQL)

text

### 2.2 Componentes Selecionados

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| ORM        | Hibernate | 6.x (embutido no Spring Boot) |
| Abstração  | Spring Data JPA | 3.4+ |
| DB H2      | H2 Database | 2.2+ |
| Mapeamento | Jakarta Persistence (JPA 3.1) | - |

### 2.3 Estrutura de Pacotes
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

---

## 3. IMPLEMENTAÇÃO DETALHADA

### 3.1 Camada de Model (Entidade JPA)


Justificativas:

@Entity + @Table mapeamento explícito para segurança

GenerationType.IDENTITY usa auto-incremento do banco (melhor performance para insert)

Lombok reduz boilerplate em ~70%

###  3.2 Camada Repository

Justificativas:

JpaRepository fornece CRUD pronto (findAll, save, deleteById, etc.)

Query methods eliminam SQL para 80% dos casos

JPQL mantém portabilidade entre bancos

Native queries disponíveis para otimização pontual

###  3.3 Camada Service

Justificativas:

@Transactional(readOnly = true) no nível da classe otimiza leituras

@Transactional em métodos de escrita garante atomicidade

Injeção por construtor facilita testes e imutabilidade

Validação centralizada no service (não no controller)

### 3.4 Camada Controller

Justificativas:

Prefixo /api para versionamento futuro

Retornos HTTP apropriados (201 Created, 204 No Content)

@Valid para validação de bean validation

### 3.5 Configuração (application.yml)
yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: 
  
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop  # create-drop para dev, validate para prod
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
  
  h2:
    console:
      enabled: true
      path: /h2-console

logging:
  level:
    org.hibernate:
      SQL: DEBUG
      type.descriptor.sql.BasicBinder: TRACE
    org.springframework.web: DEBUG

### 3.6 DataLoader (População Inicial)

java
@Component
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final ProductRepository repository;
    
    public DataLoader(ProductRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void run(String... args) {
        log.info("=== INICIANDO DATA LOADER ===");
        
        // CREATE
        List<Product> products = Arrays.asList(
            new Product(null, "Smartphone iPhone 15", 5999.99),
            new Product(null, "Notebook Dell XPS", 12999.99),
            new Product(null, "Mouse Logitech MX", 399.99),
            new Product(null, "Teclado Mecânico", 499.99),
            new Product(null, "Monitor 4K 32", 2499.99)
        );
        repository.saveAll(products);
        log.info("✓ CREATE - {} produtos salvos", products.size());
        
        // READ
        List<Product> allProducts = repository.findAll();
        log.info("✓ READ - Total no banco: {}", allProducts.size());
        
        // UPDATE
        if (allProducts.size() > 0) {
            Product toUpdate = allProducts.get(0);
            String oldName = toUpdate.getName();
            toUpdate.setName(oldName + " (Ultra)");
            repository.save(toUpdate);
            log.info("✓ UPDATE - Produto ID {}: '{}' → '{}'", 
                toUpdate.getId(), oldName, toUpdate.getName());
        }
        
        // DELETE
        if (allProducts.size() > 2) {
            Product toDelete = allProducts.get(2);
            repository.delete(toDelete);
            log.info("✓ DELETE - Removido: {} (ID: {})", 
                toDelete.getName(), toDelete.getId());
        }
        
        // Query customizada
        List<Product> cheap = repository.findByPriceLessThanEqual(1000.0);
        log.info("📊 Produtos com preço ≤ R$1000: {}", cheap.size());
        
        log.info("=== DATA LOADER FINALIZADO ===");
    }
}

### 4. Postman

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
      "name": "Buscar por ID",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/products/1"
      }
    },
    {
      "name": "Criar Produto",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"name\": \"Produto Novo\", \"price\": 199.99}"
        },
        "url": "http://localhost:8080/api/products"
      }
    },
    {
      "name": "Atualizar Produto",
      "request": {
        "method": "PUT",
        "url": "http://localhost:8080/api/products/1",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\"name\": \"Produto Atualizado\", \"price\": 299.99}"
        }
      }
    },
    {
      "name": "Deletar Produto",
      "request": {
        "method": "DELETE",
        "url": "http://localhost:8080/api/products/2"
      }
    },
    {
      "name": "Buscar por Preço",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/products/search/price?maxPrice=500.0"
      }
    }
  ]
}

### 5.GUIAS DE IMPLEMENTAÇÃO
####5.1 Como Adicionar uma Nova Entidade

Criar a entidade JPA:

java
@Entity
@Table(name = "tb_category")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
Criar o Repository:

java
public interface CategoryRepository extends JpaRepository<Category, Long> {}
Criar o Service:

java
@Service
public class CategoryService {
    private final CategoryRepository repository;
    // ... métodos CRUD
}
Criar o Controller:

java
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;
    // ... endpoints
}

####5.2 Migração de H2 para PostgreSQL (Produção)

Adicionar dependência no pom.xml:

xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
Atualizar application-prod.yml:

yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: secret
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
Executar:


###6. REFERÊNCIAS
Documentação Oficial
Spring Data JPA Reference

Hibernate ORM Documentation

Baeldung JPA Series

Livros Recomendados
"Java Persistence with Hibernate" - Christian Bauer

"Spring Boot in Action" - Craig Walls

Links do Projeto
E-book Base: "JPA e Acesso a dados - Arquitetura de Sistemas com Spring Boot 3.4+"

Repositório: (adicionar link do GitHub)

Wiki do Projeto: (adicionar link)

### 7. APROVAÇÕES
Nome	Papel	Data	Assinatura
Arquiteto de Software	Arquitetura	15/01/2024	Aprovado ✅
Tech Lead	Implementação	15/01/2024	Aprovado ✅
Product Owner	Negócio	15/01/2024	Aprovado ✅
8. HISTÓRICO DE REVISÕES
Versão	Data	Autor	Mudanças
1.0	15/01/2024	Equipe Arquitetura	Documento inicial
1.1	(futuro)	-	Adicionar seção de troubleshooting
Fim do Documento


Este arquivo `.md` contém uma ADR completa e profissional, pronta para ser salva e utilizada em repositórios de código, documentações técnicas ou sistemas de gerenciamento de decisões arquiteturais como GitHub, GitLab, Confluence, Notion, etc.
