# [CI-CD e Deploy] - Arquitetura de Sistemas com Spring Boot 3.4+

Uma API RESTful para gerenciamento de produtos com pipeline automatizado de integração e entrega contínua.

[![CI/CD Pipeline](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue.svg)](https://github.com/seu-usuario/api-arquitetura-example/actions/workflows/ci-cd.yml)
![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)

---

## Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Stack Tecnológica](#-stack-tecnológica)
- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Endpoints da API](#-endpoints-da-api)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Testes](#-testes)
- [Métricas e Monitoramento](#-métricas-e-monitoramento)
- [Próximos Passos](#-próximos-passos)
- [ADR - Architecture Decision Record](#-adr---architecture-decision-record)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## Sobre o Projeto

**Product API** é uma API RESTful desenvolvida em Spring Boot que realiza operações CRUD (Create, Read, Update, Delete) para gerenciamento de produtos. O projeto foi desenvolvido com foco em boas práticas de arquitetura, containerização e automação de pipeline CI/CD.

### Principais características

✅ Arquitetura em camadas (Controller → Service → Repository)  
✅ Containerização com Docker  
✅ Pipeline CI/CD automatizado com GitHub Actions  
✅ Banco de dados H2 em memória  
✅ Código limpo com Lombok  
✅ Pronto para deploy em nuvem

---

## Stack Tecnológica

| Categoria | Tecnologia | Versão |
|-----------|------------|--------|
| Linguagem | Java | 21 |
| Framework | Spring Boot | 3.4.0 |
| ORM | Spring Data JPA | - |
| Banco de Dados | H2 Database | (em memória) |
| Gerenciador de Dependências | Maven | - |
| Containerização | Docker | - |
| CI/CD | GitHub Actions | - |
| Code Generation | Lombok | - |

## Arquitetura

O projeto segue o padrão de arquitetura em camadas (Layered Architecture):

### Camadas da Aplicação

| Camada | Componente | Responsabilidade |
|--------|------------|------------------|
| **Cliente** | HTTP/REST | Requisições externas |
| **Controller** | ProductController | Endpoints e validação de entrada |
| **Service** | ProductService | Regras de negócio e orquestração |
| **Repository** | ProductRepository | Acesso e persistência de dados |
| **Database** | H2 | Armazenamento de dados |

### Fluxo de Dados
Cliente → Controller → Service → Repository → Database

### Fluxo da Pipeline CI/CD

| Fase | Ferramenta | Ações |
|------|------------|-------|
| 1. Push | GitHub | Dispara o pipeline automaticamente |
| 2. Build & Test | Maven | Compila código e executa testes |
| 3. Docker Build | Docker CLI | Cria a imagem da aplicação |
| 4. Deploy | Scripts | Publica em homologação |

### Funcionalidades

| Operação | Endpoint | Método | Descrição |
|----------|----------|--------|-----------|
| Listar todos | `/products` | GET | Retorna todos os produtos cadastrados |
| Buscar por ID | `/products/{id}` | GET | Retorna um produto específico |
| Criar produto | `/products` | POST | Cadastra um novo produto |
| Atualizar produto | `/products/{id}` | PUT | Altera os dados de um produto |
| Deletar produto | `/products/{id}` | DELETE | Remove um produto do sistema |


## H2 Database Console

Após iniciar a aplicação, acesse: `http://localhost:8080/h2-console`

| Parâmetro | Valor |
|-----------|-------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| User Name | `sa` |
| Password | (deixar em branco) |



## Jobs do Pipeline

| Job | Dependência | Descrição |
|-----|-------------|-----------|
| `build-and-test` | Nenhuma | Compila o código e executa testes unitários |
| `docker-build` | `build-and-test` | Cria a imagem Docker da aplicação |
| `deploy-homolog` | `docker-build` | Publica em ambiente de homologação (apenas branch `master`) |



## Triggers

| Evento | Branches |
|--------|----------|
| `push` | `main`, `master` |
| `pull_request` | `main`, `master` |


## CI/CD Pipeline

O projeto utiliza **GitHub Actions** para automação de build, testes e deploy.

**Arquivo de pipeline:** `.github/workflows/ci-cd.yml`

### Estrutura do Pipeline

```yaml
name: Java CI/CD with Docker

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]

jobs:
  build-and-test:  # Build e testes
  docker-build:    # Criação da imagem Docker
  deploy-homolog:  # Deploy em homologação
```

### Jobs do Pipeline

- build-and-test: Compila o código e executa os testes unitários
- docker-build: Cria a imagem Docker da aplicação
- deploy-homolog: Publica a aplicação em ambiente de homologação


## REFERÊNCIAS
- SPRING.IO https://spring.io/projects/spring-boot
- GitHub Actions https://docs.github.com/actions
- Docker Documentationer https://docs.docker.com 