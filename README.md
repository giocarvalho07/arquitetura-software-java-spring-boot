# [Tratamento de Exceptions] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring](https://img.shields.io/badge/Spring-REST-green.svg)](https://spring.io/)

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Problemas Identificados](#problemas-identificados)
- [Solução Implementada](#solução-implementada)
- [Componentes Criados](#componentes-criados)
- [Estrutura do GlobalExceptionHandler](#estrutura-do-globalexceptionhandler)
- [Exceções Personalizadas](#exceções-personalizadas)
- [Uso nas Camadas](#uso-nas-camadas)
- [Testes de Validação](#testes-de-validação)
- [Alternativas Consideradas](#alternativas-consideradas)
- [Consequências](#consequências)
- [Referências](#referências)

## Sobre o Projeto

API REST para gerenciamento de produtos, desenvolvida com Spring Boot 3.4.0 e Java 21. O sistema implementa um mecanismo global de tratamento de exceções utilizando `@RestControllerAdvice` e `@ExceptionHandler`, centralizando toda a lógica de tratamento de erros em uma única classe.

## Problemas Identificados

| Problema | Descrição |
|----------|-------------|
| **Duplicação de código** | Cada Controller precisaria tratar exceções individualmente |
| **Respostas inconsistentes** | Diferentes endpoints retornam formatos de erro diferentes |
| **Dificuldade de manutenção** | Para adicionar um novo tipo de exceção, seria necessário alterar múltiplos controllers |
| **Baixa experiência do desenvolvedor** | Clientes da API recebem respostas de erro não padronizadas (stack traces ou mensagens genéricas) |

## Solução Implementada

Implementar um mecanismo global de tratamento de exceções utilizando `@RestControllerAdvice` combinado com `@ExceptionHandler`, centralizando toda a lógica de tratamento de erros em uma única classe.

## Componentes Criados

| Componente | Descrição |
|------------|-------------|
| `BusinessException` | Exceção personalizada para regras de negócio |
| `EntityNotFoundException` | Exceção personalizada para recursos não encontrados |
| `GlobalExceptionHandler` | Classe centralizada com `@RestControllerAdvice` para tratar todas as exceções |

## Estrutura do GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneric(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getMessage());
    }
}
```


### Exceções Personalizadas
```java
// BusinessException.java
public class BusinessException extends RuntimeException {
   public BusinessException(String message) {
      super(message);
   }
}

// EntityNotFoundException.java  
public class EntityNotFoundException extends RuntimeException {
   public EntityNotFoundException(String message) {
      super(message);
   }
}
```

### Uso na camada Service
```java
@Service
public class ProductService {
   public Product findById(Long id) {
      return repository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
   }

   public Product create(Product obj) {
      if (obj.getPrice() <= 0) {
         throw new BusinessException("Product price must be greater than zero");
      }
      return repository.save(obj);
   }
}
```

### Uso na camada Controller
```java
@RestController
@RequestMapping("/products")
public class ProductController {
   // Sem necessidade de try-catch - o GlobalExceptionHandler cuida de tudo
   @GetMapping("/{id}")
   public ResponseEntity<Product> getById(@PathVariable Long id) {
      return ResponseEntity.ok(service.findById(id));
   }
}
```


## Testes de Validação

Cenários testados com sucesso:

| Cenário | Status Esperado | Comportamento |
|---------|-----------------|---------------|
| Buscar produto inexistente | 404 | Mensagem: "Product not found with id: 999" |
| Criar produto com nome vazio | 422 | Mensagem: "Product name cannot be null or empty" |
| Criar produto com preço negativo | 422 | Mensagem: "Product price must be greater than zero" |
| Criar produto com nome duplicado | 422 | Mensagem: "Product with name 'X' already exists" |
| Erro inesperado no sistema | 500 | Mensagem: "Erro interno: [mensagem original]" |


## Alternativas Consideradas

### 1. Tratamento de exceções em cada Controller
```java
@GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
        Product obj = service.findById(id);
        return ResponseEntity.ok(obj);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro interno");
    }
}
```
Rejeitada porque: Gera muita duplicação de código, viola o princípio DRY e dificulta a manutenção.


### 2. Uso de ExceptionHandler no Controller base
```java
public abstract class BaseController {
   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
      return ResponseEntity.status(404).body(ex.getMessage());
   }
}
```
Rejeitada porque: Requer que todos os controllers estendam a classe base, acoplamento desnecessário e herança em vez de composição.

### 3. Apenas try-catch nos Services
Rejeitada porque: A responsabilidade de formatar respostas HTTP pertence à camada de apresentação (Controller), não ao Service.


### 4. Uso de @ControllerAdvice com ResponseEntityExceptionHandler estendido
```java
public abstract class BaseController {
   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
      return ResponseEntity.status(404).body(ex.getMessage());
   }
}
```
Rejeitada porque: A versão mais simples atende aos requisitos atuais sem complexidade desnecessária.


## Consequências

### Positivas

- **Centralização**: Todo tratamento de exceções em um único local
- **Consistência**: Respostas de erro padronizadas para toda a API
- **Manutenibilidade**: Novo tipo de exceção pode ser adicionado com apenas um novo método no handler
- **Código mais limpo**: Controllers e Services ficam focados apenas na lógica de negócio
- **Separação de preocupações**: Tratamento de erros isolado da lógica principal
- **Status HTTP apropriados**:
    - `404` para recursos não encontrados
    - `422` para violações de regras de negócio
    - `500` para erros inesperados

### Negativas

- **Indireção**: Adiciona uma camada extra que pode dificultar o debugging inicial
- **Curva de aprendizado**: Desenvolvedores novos precisam entender o mecanismo do ControllerAdvice
- **Overhead mínimo**: Pequena sobrecarga na cadeia de execução (negligenciável)

### Trade-offs

- **Simplicidade vs Detalhamento**: Optou-se por respostas simples (String) ao invés de objetos complexos com timestamp e path para manter o código mais simples
- **Especificidade vs Generalidade**: Tratamento específico para BusinessException e EntityNotFoundException, mas genérico para RuntimeException

## Referências

- [Spring Documentation - @RestControllerAdvice](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestControllerAdvice.html)
- [Spring Boot Error Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
- [ADR Template](https://adr.github.io/madr/)

## Decisão Final

**Aprovado.** A implementação do GlobalExceptionHandler com @RestControllerAdvice atende todos os requisitos do projeto atual, proporcionando:

- ✅ Código mais limpo e manutenível
- ✅ Respostas de erro consistentes
- ✅ Separação clara de responsabilidades
- ✅ Facilidade para adicionar novos tipos de exceção
- ✅ Melhor experiência para consumidores da API

---

*Data: 04 de Junho de 2026*
*Autor: Equipe de Arquitetura*