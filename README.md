# [Cache e Performance] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Cache](https://img.shields.io/badge/Spring-Cache-orange.svg)](https://docs.spring.io/spring-framework/reference/integration/cache.html)

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Problema Identificado](#problema-identificado)
- [Solução Implementada](#solução-implementada)
- [Stack Tecnológico](#stack-tecnológico)
- [Estratégia de Cache](#estratégia-de-cache)
- [Configuração Implementada](#configuração-implementada)
- [Arquitetura da Solução](#arquitetura-da-solução)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Validação e Testes](#validação-e-testes)
- [Consequências](#consequências)
- [Referências](#referências)

## Sobre o Projeto

Sistema de gerenciamento de produtos que implementa uma camada de cache utilizando a abstração de cache do Spring Framework para melhorar a performance em operações de leitura frequentes.

## Problema Identificado

O sistema apresentava problemas de performance em operações de leitura frequentes, especialmente no método de busca por ID:

| Problema | Descrição |
|----------|-------------|
| **Latência elevada** | Consultas ao banco de dados demorando aproximadamente 2 segundos (simulando operações pesadas) |
| **Sobrecarga do banco de dados** | Múltiplas chamadas para o mesmo dado em curto intervalo |
| **Experiência do usuário degradada** | Tempo de resposta lento para operações de leitura |

## Stack Tecnológico

| Tecnologia | Versão | Finalidade |
|------------|--------|-------------|
| Spring Boot | 3.x | Framework principal |
| Spring Data JPA | 3.x | Persistência de dados |
| Spring Cache Abstraction | 3.x | Camada de cache |
| H2/PostgreSQL | - | Banco de dados |
| Maven | - | Gerenciador de dependências |

## Solução Implementada

Implementar uma camada de cache utilizando a abstração de cache do Spring Framework com as seguintes características:

- **Cache Provider**: Simple Cache Provider (default do Spring Boot)
- **Cache Name**: "products"
- **Cache Strategy**: Cache-Aside (Lazy Loading)
- **TTL**: Indefinido (controlado via evicção manual)

#### @Cacheable

```java
@Cacheable(value = "products", key = "#id")
public Product buscarPorId(Long id)
```

- Aplica cache no método de busca por ID
- Primeira chamada executa o método e armazena o resultado
- Chamadas subsequentes retornam diretamente do cache

#### @CacheEvict

```java
@CacheEvict(value = "products", key = "#id")
public Product atualizar(Long id, Product productAtualizado)
```
- Remove a entrada do cache quando o produto é atualizado
- Garante consistência dos dados


### 3. Arquitetura da Solução

**APPLICATION LAYER**

- **Controller Layer**
    - `ProductController`

- **Service Layer**
    - `ProductService`
        - `@Cacheable(value = "products", key = "#id")`
        - `@CacheEvict(value = "products", key = "#id")`

- **Cache Layer**
    - `CacheConfig` (`@EnableCaching`)
    - `Cache Manager` (`SimpleCacheManager`)

- **Data Layer**
    - `ProductRepository` (JPA)
    - `Database` (H2/PostgreSQL)


## 4. Configuração Implementada
### CacheConfig.java:

```java
@Configuration
@EnableCaching
public class CacheConfig {
// Habilita o suporte a cache na aplicação
}
```


### ProductService.java:

```java
@Service
public class ProductService {
@Cacheable(value = "products", key = "#id")
public Product buscarPorId(Long id) {
// Simulação de operação lenta (2 segundos)
Thread.sleep(2000);
return repository.findById(id);
}

    @CacheEvict(value = "products", key = "#id")
    public Product atualizar(Long id, Product product) {
        // Atualiza e remove do cache
    }
    
    @CacheEvict(value = "products", key = "#id")
    public void deletar(Long id) {
        // Deleta e remove do cache
    }
}
```


# Consequências

## Positivas

| Aspecto | Detalhe |
|---------|---------|
| **Performance** | Primeira chamada: ~2000ms → Chamadas seguintes: ~15ms (melhoria de ~99%) |
| **Banco de dados** | Redução de consultas repetitivas e menor uso de recursos |
| **Baixo acoplamento** | Implementação via anotações, sem poluir o código de negócio |
| **Flexibilidade** | Fácil troca para cache distribuído (Redis, Hazelcast) |

## Negativas

| Aspecto | Detalhe |
|---------|---------|
| **Consistência** | Risco de dados desatualizados (mitigado com `@CacheEvict` em operações de escrita) |
| **Memória** | Objetos em cache ocupam heap (para produção: usar TTL e políticas de evicção) |
| **Clusters** | Simple Cache não compartilha entre nós (para ambiente distribuído: Redis ou Hazelcast) |


## Endpoints Disponíveis

| Método | Endpoint | Cache |
|--------|----------|-------|
| GET | `/api/products` | Não |
| GET | `/api/products/{id}` | ✅ Sim |
| GET | `/api/products/search?maxPrice={price}` | Não |
| POST | `/api/products` | ❌ Evict |
| PUT | `/api/products/{id}` | ❌ Evict |
| DELETE | `/api/products/{id}` | ❌ Evict |


## Validação e Testes

### Teste de Performance Implementado

```java
@Test
void deveValidarPerformanceDoCache() {
// Primeira chamada: ~2000ms
long tempoPrimeira = medirExecucao(() -> buscarPorId(id));

    // Segunda chamada: ~15ms
    long tempoSegunda = medirExecucao(() -> buscarPorId(id));
    
    assertTrue(tempoSegunda < (tempoPrimeira / 10));
}
```

### Resultados dos Testes

```text
>>> PERFORMANCE TEST <<<
Tempo 1ª chamada (sem cache): 2005ms
Tempo 2ª chamada (com cache): 15ms
✅ Cache funcionando corretamente

>>> CACHE EVICT TEST (UPDATE) <<<
Tempo 1ª chamada (sem cache): 2003ms
Tempo após update (cache limpo): 2001ms
✅ Cache limpo após atualização
```


## Referências

- [Spring Cache Abstraction](https://docs.spring.io/spring-framework/reference/integration/cache.html)
- [ADR Template (Michael Nygard)](https://github.com/joelparkerhenderson/architecture-decision-record)
- [Spring Boot Caching Best Practices](https://spring.io/guides/gs/caching/)

---

| Item | Detalhe |
|------|---------|
| **Aprovado por** | Equipe de Arquitetura |
| **Implementado por** | Time de Desenvolvimento |
| **Data de Vigência** | Imediata |
| **Revisão** | 06 meses |