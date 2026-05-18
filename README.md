# [Testes unitários e Testes de Integração] - Arquitetura de Sistemas com Spring Boot 3.4+


# API Produtos - Estratégia de Testes com Spring Boot 3

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10+-blue.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5.7+-green.svg)](https://site.mockito.org/)


## Sobre o Projeto

API RESTful para gerenciamento de produtos, implementando uma estratégia robusta de testes automatizados baseada na pirâmide de testes com Spring Boot 3+.

**Endpoints disponíveis:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Listar todos os produtos |
| GET | `/api/produtos/{id}` | Buscar produto por ID |
| POST | `/api/produtos` | Criar novo produto |
| PUT | `/api/produtos/{id}` | Atualizar produto |
| DELETE | `/api/produtos/{id}` | Deletar produto |

## Estratégia de Testes

O projeto adota a **pirâmide de testes** com três níveis:

- Testes de Integração (topo)
- Testes Unitários (meio)
- Testes de Repositório (base)

| Nível | Foco | Tecnologia |
|-------|------|-------------|
| Testes Unitários | Service (lógica de negócio isolada) | JUnit 5 + Mockito |
| Testes de Integração | Controller → Service → Repository | MockMvc + @SpringBootTest |
| Testes de Repositório | Camada de persistência JPA | @DataJpaTest + H2 |

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|-------------|
| Java | 21 | Linguagem base |
| Spring Boot | 3.4+ | Framework principal |
| JUnit 5 | 5.10+ | Framework de testes |
| Mockito | 5.7+ | Mock para testes unitários |
| MockMvc | - | Simulação de requisições HTTP |
| H2 Database | - | Banco em memória para testes |
| AssertJ | - | Asserções fluentes |


## Estrutura dos Testes

src/test/java/
  com/api_arquitetura_example/
    controller/
      ProdutoControllerIT.java (Testes de integração)
    service/
      ProdutoServiceTest.java (Testes unitários)
    repository/
      ProdutoRepositoryTest.java (Testes de repositório)




## Testes Unitários

**ProdutoServiceTest** - Testa a lógica de negócio de forma isolada usando Mockito.

```java
@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deveSalvarProdutoComSucesso() {
        Produto produto = new Produto(null, "Monitor 4K", new BigDecimal("1800.00"));
        when(repository.save(any(Produto.class)))
                .thenReturn(new Produto(1L, "Monitor 4K", new BigDecimal("1800.00")));

        Produto salvo = service.salvar(produto);

        assertNotNull(salvo.getId());
        assertEquals("Monitor 4K", salvo.getNome());
        verify(repository, times(1)).save(produto);
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Produto produto = new Produto(1L, "Teclado", new BigDecimal("150.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        Produto encontrado = service.buscarPorId(1L);

        assertEquals("Teclado", encontrado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }
}
```


## Testes de Integração

**ProdutoControllerIT** - Testa o fluxo completo desde a requisição HTTP até a resposta.

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarListaDeProdutos() throws Exception {
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        Produto novoProduto = new Produto(null, "Mouse Gamer", new BigDecimal("150.00"));
        String json = objectMapper.writeValueAsString(novoProduto);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Mouse Gamer"));
    }
}
```

## Testes de Repositório

**ProdutoRepositoryTest** - Testa operações de banco de dados com @DataJpaTest.

```java
@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        produtoTeste = repository.save(new Produto(null, "Produto Teste", BigDecimal.valueOf(100.0)));
        produtoCaro = repository.save(new Produto(null, "Produto Caro", BigDecimal.valueOf(500.0)));
        produtoBarato = repository.save(new Produto(null, "Produto Barato", BigDecimal.valueOf(50.0)));
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = repository.findAll();
        assertThat(produtos).hasSize(3);
    }

    @Test
    void testSaveProduct() {
        Produto produto = new Produto(null, "Novo Produto", BigDecimal.valueOf(75.0));
        Produto saved = repository.save(produto);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNome()).isEqualTo("Novo Produto");
    }

    @Test
    void testDeleteById() {
        repository.deleteById(produtoTeste.getId());
        assertThat(repository.findById(produtoTeste.getId())).isEmpty();
        assertThat(repository.findAll()).hasSize(2);
    }
}
```



## Como Executar os Testes

Executar todos os testes
```bash
mvn test
Executar uma classe específica

bash
mvn test -Dtest=ProdutoServiceTest
Executar um método específico

bash
mvn test -Dtest=ProdutoServiceTest#deveSalvarProdutoComSucesso
Executar apenas testes unitários

bash
mvn test -Dtest=*Test
Executar apenas testes de integração

bash
mvn test -Dtest=*IT
```

## Dependências Maven

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Boot Starter Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- H2 Database (para testes) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Spring Boot Starter Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## ADR - Decisão Arquitetural

### Contexto

O sistema precisava garantir qualidade e confiabilidade através de testes automatizados. As principais motivações foram:

- Dependência exclusiva de testes manuais, tornando o sistema frágil
- Alterações em uma camada poderiam quebrar funcionalidades em outras
- Falta de validação automatizada da lógica de negócio
- Comunicação entre camadas não era testada de forma integrada

### Decisão

Adotar estratégia baseada na **pirâmide de testes** combinando testes unitários, de integração e de repositório.

### Alternativas Consideradas

| Alternativa | Motivo da rejeição |
|-------------|---------------------|
| Apenas testes manuais | Frágil, lento e propenso a erro humano |
| Apenas testes integrados | Lentos e não isolam falhas específicas |
| Apenas testes unitários | Não validam integração entre camadas |

### Comparativo

| Característica | Teste Unitário | Teste Integrado | Teste Repositório |
|----------------|----------------|------------------|--------------------|
| O que testa | Uma única lógica/método | Fluxo entre várias classes | Operações JPA |
| Dependências | Mocks | Contexto Spring real | Banco H2 |
| Velocidade | Muito rápido | Mais lento | Rápido |
| Complexidade | Baixa | Alta | Média |

### Referências

- Ebook interno: "[Testes unitários e Testes de Integração] - Arquitetura de Sistemas com Spring Boot 3.4+"
- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring MockMvc Guide](https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html)

---

*Data: 2025-04-07*
*Autor: Equipe de Arquitetura*
