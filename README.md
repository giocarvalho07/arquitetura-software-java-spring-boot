# [JWT e Autenticação] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/JWT-JSON%20Web%20Token-orange.svg)](https://jwt.io/)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0+-green.svg)](https://spring.io/projects/spring-security)

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Arquitetura Proposta](#arquitetura-proposta)
- [Componentes Implementados](#componentes-implementados)
- [Fluxo de Autenticação](#fluxo-de-autenticação)
- [Estrutura do Token JWT](#estrutura-do-token-jwt)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Dependências Maven](#dependências-maven)
- [Como Executar](#como-executar)
- [Testando a API](#testando-a-api)
- [Decisões Arquiteturais](#decisões-arquiteturais)

## Sobre o Projeto

API RESTful com autenticação JWT (JSON Web Token) para gerenciamento de produtos, implementando segurança stateless com Spring Security.

**Recursos implementados:**
- Cadastro e autenticação de usuários
- Geração de tokens JWT
- Proteção de endpoints com validação de token
- Senhas criptografadas com BCrypt

## Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/api/auth/register` | Cadastro de novo usuário | Público |
| POST | `/api/auth/login` | Login e obtenção do token JWT | Público |

### Produtos (Protegidos)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/api/products` | Criar novo produto | Autenticado |
| GET | `/api/products` | Listar todos os produtos | Autenticado |
| GET | `/api/products/{id}` | Buscar produto por ID | Autenticado |
| PUT | `/api/products/{id}` | Atualizar produto | Autenticado |
| DELETE | `/api/products/{id}` | Deletar produto | Autenticado |

## Arquitetura Proposta

Cliente → POST /api/auth/login → Controller → Service → Geração JWT

↓ (Bearer Token)

Cliente → GET /api/products (Header: Authorization) → Filtro JWT → Validação → Controller


## Componentes Implementados

### AuthController

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.registerUser(registerRequest);
        return ResponseEntity.ok(authResponse);
    }
}
```

### ProductController com Proteção

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) { ... }
    
    @GetMapping
    public ResponseEntity<List<Product>> findAll() { ... }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) { ... }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { ... }
}
```

## Fluxo de Autenticação

1. Usuário envia credenciais para `/api/auth/login`
2. Servidor valida credenciais e gera token JWT assinado
3. Token é retornado no `AuthResponse`
4. Cliente armazena token (localStorage/sessionStorage)
5. Cliente envia token no header: `Authorization: Bearer <token>`
6. Servidor valida token antes de processar requisições protegidas


#Estrutura do Token JWT

```json
{
"header": {
"alg": "HS256",
"typ": "JWT"
},
"payload": {
"sub": "user@email.com",
"roles": ["ROLE_USER"],
"iat": 1712500000,
"exp": 1712503600
},
"signature": "assinatura_digital"
}
```

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|-------------|
| Java | 21 | Linguagem base |
| Spring Boot | 3.4+ | Framework principal |
| Spring Security | 6.0+ | Segurança e autenticação |
| JWT (JJWT) | 0.11.5 | Geração e validação de tokens |
| Spring Data JPA | 3.4+ | Persistência de dados |
| BCrypt | - | Criptografia de senhas |
| H2 Database | - | Banco em memória |


##Dependências Maven


```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

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
```


#Como Executar
Pré-requisitos:
- Java 21
- Maven 3.9+

### Clone o repositório
git clone https://github.com/seu-usuario/api-jwt-spring.git

### Entre no diretório
cd api-jwt-spring

### Execute a aplicação
mvn spring-boot:run

Testando a API
1. Registrar um novo usuário 

```bash
POST /api/auth/register
   Content-Type: application/json

{
"username": "usuario",
"email": "usuario@email.com",
"password": "senha123",
"role": "USER"
}
```

2. Fazer login
```bash
   POST /api/auth/login
   Content-Type: application/json

{
"email": "usuario@email.com",
"password": "senha123"
}
```

Resposta:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "usuario@email.com",
    "roles": ["ROLE_USER"]
}
```

3. Acessar endpoint protegido
```bash
GET /api/products
Authorization: Bearer <token_jwt>
```



## Decisões Arquiteturais

### Alternativas Consideradas

| Alternativa | Motivo da rejeição |
|-------------|---------------------|
| Basic Authentication | Envio de credenciais a cada requisição, menos seguro |
| Session-based (JSESSIONID) | Stateful, não escala horizontalmente facilmente |
| OAuth 2.0 | Overkill para APIs internas/simples |
| API Key | Sem diferenciação por usuário e roles |

### Comparativo

| Característica | JWT | Session | Basic Auth |
|----------------|-----|---------|-------------|
| Stateful/Stateless | Stateless | Stateful | Stateless |
| Escalabilidade | Alta | Baixa | Alta |
| Revogação | Difícil | Fácil | Difícil |
| Custo por requisição | Médio | Baixo | Médio |
| Segurança | Alta | Média | Baixa |

### Consequências

**Positivas**

- **Stateless**: API não mantém sessão em memória, facilitando escalabilidade horizontal
- **Segurança**: Senhas criptografadas e tokens assinados digitalmente
- **Interoperabilidade**: JWT é padrão amplamente suportado por diferentes tecnologias
- **Performance**: Validação de token sem consulta a banco de dados
- **Flexibilidade**: Possibilidade de incluir claims personalizadas (roles, permissões)

**Negativas**

- **Tamanho do token**: Pode crescer com muitas informações adicionais
- **Revogação**: Tokens são válidos até expiração natural (não há revogação imediata)
- **Armazenamento no cliente**: Frontend precisa gerenciar o token com segurança
- **Complexidade inicial**: Configuração do Spring Security com JWT é mais elaborada

### Referências

- [JSON Web Tokens (JWT) Specification (RFC 7519)](https://tools.ietf.org/html/rfc7519)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [JJWT Library](https://github.com/jwtk/jjwt)
- [OWASP JWT Security Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)

---

*Data: 2025-06-01*
*Autor: Equipe de Arquitetura*