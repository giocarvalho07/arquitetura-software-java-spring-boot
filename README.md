# [Docker] - Arquitetura de Sistemas com Spring Boot 3.4+

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-28.0+-blue.svg)](https://www.docker.com/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2.0+-brightgreen.svg)](https://docs.docker.com/compose/)

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Problemas Identificados](#problemas-identificados)
- [Solução Implementada](#solução-implementada)
- [Componentes Criados](#componentes-criados)
- [Estrutura do Dockerfile](#estrutura-do-dockerfile)
- [Estrutura do Docker Compose](#estrutura-do-docker-compose)
- [Comandos Docker](#comandos-docker)
- [Acessos e Testes](#acessos-e-testes)
- [Alternativas Consideradas](#alternativas-consideradas)
- [Consequências](#consequências)
- [Referências](#referências)

## Sobre o Projeto

API REST para gerenciamento de produtos, desenvolvida com Spring Boot 3.4.0 e Java 21. O sistema implementa containerização com Docker para garantir ambiente consistente entre desenvolvimento, teste e produção, eliminando o clássico problema "funciona na minha máquina".

## Problemas Identificados

| Problema | Descrição |
|----------|-----------|
| **Ambientes inconsistentes** | Aplicação funcionava na máquina do desenvolvedor mas falhava em produção |
| **Dependências conflitantes** | Múltiplos projetos exigindo diferentes versões do Java/banco de dados |
| **Onboarding demorado** | Novos desenvolvedores precisavam instalar/configurar várias ferramentas |
| **Isolamento inadequado** | Serviços compartilhando o mesmo ambiente causando conflitos |

## Solução Implementada

Implementar containerização com Docker utilizando Dockerfile multi-estágio para build e execução da aplicação Spring Boot, com docker-compose para orquestração e gerenciamento de serviços.

## Componentes Criados

| Componente | Descrição |
|------------|-----------|
| `Dockerfile` | Define a imagem da aplicação com build multi-estágio |
| `docker-compose.yml` | Orquestra a aplicação e banco de dados |

## Estrutura do Dockerfile

```dockerfile
# Estágio de execução: utiliza uma imagem leve do JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Define um diretório de trabalho temporário
VOLUME /tmp

# Copia o arquivo .jar gerado pelo Maven para dentro do container
COPY target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

# Explicação das Instruções Dockerfile

| Instrução | Descrição |
|-----------|-----------|
| `FROM eclipse-temurin:21-jdk-alpine` | Base com Java 21 e Alpine Linux (imagem leve ~200MB) |
| `VOLUME /tmp` | Diretório temporário para o Spring Boot (uploads, logs, etc) |
| `COPY target/*.jar app.jar` | Copia o JAR gerado pelo Maven para dentro do container |
| `EXPOSE 8080` | Documenta que a aplicação usa a porta 8080 |
| `ENTRYPOINT ["java", "-jar", "/app.jar"]` | Comando executado ao iniciar o container |


## Estrutura do Docker-compose

```yml
version: '3.8'

services:
  app:
    build: .
    container_name: product-api
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:mem:productdb
      SPRING_DATASOURCE_DRIVER_CLASS_NAME: org.h2.Driver
      SPRING_JPA_DATABASE_PLATFORM: org.hibernate.dialect.H2Dialect
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_H2_CONSOLE_ENABLED: "true"
```

# Explicação das Configurações docker-compose.yml

| Configuração | Descrição |
|--------------|-----------|
| `version: '3.8'` | Define a versão do formato do Docker Compose (3.8 é a mais recente estável) |
| `services:` | Inicia a definição dos serviços que serão executados |
| `app:` | Nome do serviço (pode ser qualquer nome descritivo) |
| `build: .` | Indica que a imagem deve ser construída a partir do Dockerfile localizado no diretório atual |
| `container_name: product-api` | Define um nome fixo para o container (ao invés de um nome gerado automaticamente) |
| `ports: - "8080:8080"` | Mapeia a porta 8080 do host (computador local) para a porta 8080 do container (formato: "PORTA_HOST:PORTA_CONTAINER") |
| `environment:` | Define variáveis de ambiente que serão injetadas no container, sobrescrevendo as configurações do application.properties |



##Comandos Docker

### Build e Execução

```bash
# Build da imagem a partir do Dockerfile
docker build -t product-api .

# Sobe os containers (modo interativo - logs visíveis no terminal)
docker-compose up

# Sobe os containers em background (modo detached)
docker-compose up -d

# Para todos os containers em execução
docker-compose down

# Para os containers e remove os volumes (limpa dados persistentes)
docker-compose down -v

# Reconstrói as imagens sem usar cache (útil para forçar rebuild completo)
docker-compose up --build
```

# Acessos e Testes

## Endpoints Disponíveis

| Recurso | URL | Descrição |
|---------|-----|-----------|
| API de Produtos | `http://localhost:8080/products` | Retorna a lista completa de produtos cadastrados |
| Produto Específico | `http://localhost:8080/products/{id}` | Busca um produto específico pelo seu ID |
| H2 Console | `http://localhost:8080/h2-console` | Interface web administrativa do banco de dados H2 |

## Credenciais H2 Console

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:productdb` |
| User Name | `sa` |
| Password | (deixar em branco) |

## Referências

| Recurso | Link |
|---------|------|
| Spring Boot with Docker | [spring.io/guides/gs/spring-boot-docker/](https://spring.io/guides/gs/spring-boot-docker/) |
| Docker Compose Documentation | [docs.docker.com/compose/](https://docs.docker.com/compose/) |
| Best Practices for Dockerfiles | [docs.docker.com/develop/develop-images/dockerfile_best-practices/](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/) |

---

**Responsáveis:** Time de Arquitetura

**Data:** 04/06/2026

**Versão:** 1.0