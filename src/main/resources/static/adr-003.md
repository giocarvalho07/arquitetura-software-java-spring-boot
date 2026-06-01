# ADR-003: Estratégia de Testes com Unitários e Integração no Spring Boot 3


## Contexto

O sistema precisa garantir qualidade e confiabilidade através de testes automatizados. Foi identificado que:

1. O sistema dependia exclusivamente de testes manuais, tornando-o frágil e propenso a regressões.
2. Alterações em uma camada poderiam quebrar funcionalidades em outras sem detecção prévia.
3. Não havia validação automatizada da lógica de negócio (Services) de forma isolada.
4. A comunicação entre Controller, Service e Repository não era testada de forma integrada.
5. A falta de testes dificultava refatorações e evoluções do código com segurança.

## Decisão

Adotar uma estratégia de testes baseada na **pirâmide de testes**, combinando:

**Testes Unitários:**
- Foco exclusivo na camada Service
- Isolamento total usando Mockito para simular o Repository
- Execução rápida (centenas por segundo)
- Validação da lógica de negócio e regras

**Testes de Integração:**
- Foco no fluxo Controller → Service → Repository
- Uso do contexto Spring com `@SpringBootTest`
- Simulação de requisições HTTP via MockMvc
- Banco de dados H2 em memória para testes

**Componentes obrigatórios:**
- `@ExtendWith(MockitoExtension.class)` para testes unitários
- `@Mock` e `@InjectMocks` para injeção de dependências simuladas
- `@SpringBootTest` + `@AutoConfigureMockMvc` para testes integrados
- `verify()` para validação de interações com mocks

## Estrutura de Testes

```java
//Testes Unitários (ProdutoServiceTest):

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock private ProdutoRepository repository;
    @InjectMocks private ProdutoService service;
}

//Testes de Integração (ProdutoControllerIT):

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIT {
    @Autowired private MockMvc mockMvc;
}
```

## Consequências
**Positivas**

- Confiança no código: regressões são detectadas automaticamente
- Documentação viva: testes descrevem o comportamento esperado
- Refatoração segura: mudanças podem ser feitas com baixo risco
- Isolamento de falhas: teste unitário aponta exatamente onde está o erro
- Velocidade: testes unitários rodam em milissegundos

**Negativas**

- Tempo inicial: escrever testes demanda esforço adicional no desenvolvimento
- Manutenção: testes precisam ser atualizados quando o código muda
- Curva de aprendizado: equipe precisa aprender Mockito e MockMvc
- Testes integrados mais lentos: exigem subir o contexto Spring

## Alternativas Consideradas

| Alternativa | Motivo da rejeição |
|-------------|---------------------|
| Apenas testes manuais | Frágil, lento e propenso a erro humano |
| Apenas testes integrados | Lentos e não isolam falhas específicas |
| Apenas testes unitários | Não validam integração entre camadas |
| TestContainers com banco real | Overhead desnecessário para a maioria dos cenários |

## Comparativo

| Característica | Teste Unitário | Teste Integrado |
|----------------|----------------|------------------|
| O que testa | Uma única lógica/método | Fluxo entre várias classes |
| Dependências | Mocks (simulações) | Contexto Spring real |
| Velocidade | Muito rápido | Mais lento |
| Complexidade | Baixa | Alta |
| Custo de manutenção | Baixo | Alto |

## Dependências Maven Necessárias

```bash
xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Compliance com Spring Boot 3.4+

A solução utiliza recursos compatíveis com as versões 3.4+ do Spring Boot:
- JUnit 5
- Mockito integrado ao spring-boot-starter-test
- MockMvc para testes de controller
- @SpringBootTest com suporte a slices de contexto

Referências

- Ebook interno: "[Testes unitários e Testes de Integração] - Arquitetura de Sistemas com Spring Boot 3.4+"
- Spring Boot Testing Documentation
- Mockito Documentation

*Data da ADR: 2025-04-07*
Autor: Equipe de Arquitetura