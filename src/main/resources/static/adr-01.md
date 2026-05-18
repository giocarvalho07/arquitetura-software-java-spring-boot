# ADR-001: Adoção de Padrão Arquitetural com DTO, Mapper e Respostas HTTP Semânticas no Spring Boot 3

## Status

**Aceita** (Implementada a partir da versão 1.0 do sistema)

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

## Compliance com Spring Boot 3.4+

A solução utiliza recursos compatíveis com as versões 3.4+ do Spring Boot:

- `jakarta.validation` para validação com `@Valid`
- `ResponseEntity` para controle total da resposta HTTP
- `ServletUriComponentsBuilder` para construção do header `Location`
- `@Transactional(readOnly = true)` para otimização de consultas
- Suporte a Java 21 (LTS)

## Referências

- Ebook interno: "[API Rest, DTO, Mapper e Http Response] - Arquitetura de Sistemas com Spring Boot 3.4+"
- [Spring Boot Reference – REST Repositories](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-mvc)
- [Jakarta Bean Validation](https://beanvalidation.org/)

---

*Data da ADR: 2025-04-07*  
*Autor: Equipe de Arquitetura*