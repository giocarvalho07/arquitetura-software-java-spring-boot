# [Observabilidade e Monitoramento] - API de Produtos com Spring Boot 3.4+

Solução completa de observabilidade para a API de Produtos, incluindo métricas, tracing distribuído, health checks e cobertura de testes.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Prometheus](https://img.shields.io/badge/Prometheus-Monitoring-orange.svg)](https://prometheus.io/)
[![Jaeger](https://img.shields.io/badge/Jaeger-Tracing-blue.svg)](https://www.jaegertracing.io/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![JaCoCo](https://img.shields.io/badge/JaCoCo-Coverage-red.svg)](https://www.jacoco.org/)

---

## Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Stack Tecnológica](#-stack-tecnológica)
- [Arquitetura da Solução](#-arquitetura-da-solução)
- [Componentes da Stack](#-componentes-da-stack)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Endpoints da Aplicação](#-endpoints-da-aplicação)
- [Ferramentas de Monitoramento](#-ferramentas-de-monitoramento)
- [Health Checks](#-health-checks)
- [Prometheus Queries](#-prometheus-queries)
- [Testes e Cobertura](#-testes-e-cobertura)
- [Logs e Debugging](#-logs-e-debugging)
- [Docker Commands](#-docker-commands)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Consequências da Decisão](#-consequências-da-decisão)
- [ADR - Architecture Decision Record](#-adr---architecture-decision-record)
- [Referências](#-referências)

---

## Sobre o Projeto

A **API de Produtos** (api-arquitetura-example) agora conta com um sistema completo de observabilidade para monitoramento em produção. Esta solução foi implementada baseada na **ADR-008** e fornece visibilidade total sobre a aplicação.

### Principais características

✅ **Métricas de performance** em tempo real com Prometheus  
✅ **Tracing distribuído** para debugging com Jaeger  
✅ **Health checks** customizados para Database e Gateway  
✅ **Logs estruturados** com Logback  
✅ **Cobertura de testes** garantida (80% mínimo) com JaCoCo  
✅ **Containerização completa** com Docker Compose

---

## Stack Tecnológica

| Categoria | Tecnologia | Versão | Finalidade |
|-----------|------------|--------|------------|
| Linguagem | Java | 21 | Plataforma base |
| Framework | Spring Boot | 3.4.0 | Aplicação principal |
| Métricas | Spring Boot Actuator | - | Health checks e métricas base |
| Coleta Métricas | Prometheus | Latest | Armazenamento time-series |
| Tracing | Jaeger | Latest | Tracing distribuído |
| Cobertura | JaCoCo | 0.8.11 | Relatórios de cobertura |
| Containerização | Docker Compose | - | Orquestração dos serviços |
| Logs | Logback | - | Logs estruturados |
| Banco de Dados | H2 | (em memória) | Persistência |

---

## Arquitetura da Solução

A solução de observabilidade é composta por múltiplos serviços orquestrados via Docker Compose:


### Fluxo de Dados

| Origem | Destino | Tipo de Dado | Intervalo |
|--------|---------|--------------|-----------|
| Aplicação | Prometheus | Métricas HTTP/JVM | 15 segundos |
| Aplicação | Jaeger | Traces distribuídos | 100% sampling |
| Cliente | Aplicação | Requisições HTTP | - |
| Aplicação | Console | Logs estruturados | Contínuo |

---

## Componentes da Stack

### 1. Spring Boot Actuator

| Componente | Endpoint | Descrição |
|------------|----------|-----------|
| Health Check | `/actuator/health` | Status da aplicação |
| Métricas | `/actuator/metrics` | Métricas detalhadas |
| Prometheus | `/actuator/prometheus` | Métricas formato Prometheus |
| Info | `/actuator/info` | Informações da aplicação |

**Health checks customizados:**
- `CustomDbHealthIndicator` - Verifica conexão com banco H2
- `GatewayHealthIndicator` - Verifica disponibilidade do gateway

### 2. Prometheus

| Característica | Configuração |
|----------------|---------------|
| Coleta de métricas | Intervalo de 15 segundos |
| Armazenamento | Time-series database |
| Porta | 9090 |
| Arquivo config | `prometheus.yml` |

### 3. Jaeger

| Característica | Configuração |
|----------------|---------------|
| Tracing | Sampling 100% |
| Protocolos | OTLP e Zipkin |
| Porta UI | 16686 |
| Funcionalidade | Visualização de dependências |

### 4. JaCoCo

| Característica | Configuração |
|----------------|---------------|
| Cobertura mínima | 80% para linhas |
| Relatórios | HTML e XML |
| Integração | Pipeline Maven |

---

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)

---

## Instalação e Execução

### Clonar o repositório

```bash
git clone https://github.com/seu-usuario/api-arquitetura-example.git
cd api-arquitetura-example
```


## Docker Commands

### Comandos Básicos

#### Subir a Stack Completa

```bash
# Build e start de todos serviços
docker-compose up -d --build

# Subir serviços específicos
docker-compose up -d app
docker-compose up -d prometheus
docker-compose up -d jaeger
```

#### Descer a Stack
```bash
# Parar todos serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Parar serviços específicos
docker-compose stop app
docker-compose stop prometheus
```


#### Gerenciamento de Containers
```bash
# Listar containers rodando
docker-compose ps

# Ver logs de um serviço
docker-compose logs -f app
docker-compose logs -f prometheus
docker-compose logs -f jaeger

# Executar comando no container
docker exec -it produto-api sh
docker exec -it prometheus sh
```


## Acessos Locais

### Endpoints da Aplicação

| Serviço | URL | Descrição |
|---------|-----|-------------|
| API Produtos | http://localhost:8080/products | Endpoints CRUD |
| Health Check | http://localhost:8080/actuator/health | Status da aplicação |
| Métricas | http://localhost:8080/actuator/metrics | Métricas detalhadas |
| Prometheus | http://localhost:8080/actuator/prometheus | Métricas formato Prometheus |
| H2 Console | http://localhost:8080/h2-console | Banco de dados H2 |
| Info | http://localhost:8080/actuator/info | Informações da aplicação |
| Env | http://localhost:8080/actuator/env | Variáveis de ambiente |

### Ferramentas de Monitoramento

| Ferramenta | URL | Credenciais | Descrição |
|------------|-----|-------------|-------------|
| Prometheus | http://localhost:9090 | - | Interface de consulta de métricas |
| Jaeger UI | http://localhost:16686 | - | Visualização de traces distribuídos |
| JaCoCo Report | target/site/jacoco/index.html | - | Relatório de cobertura de testes |


## Configurações de Acesso

### H2 Database Console

| Parâmetro | Valor |
|-----------|-------|
| JDBC URL | `jdbc:h2:mem:delivery_db` |
| User Name | `sa` |
| Password | (deixar em branco) |

### Prometheus Targets

| Item | Valor |
|------|-------|
| URL | `http://localhost:9090/targets` |
| Job | `spring-boot-app` |
| Target | `app:8080` |
| Status | `UP` |

### Jaeger Service

| Item | Valor |
|------|-------|
| Service Name | `api-arquitetura-example` |
| Operations | `GET /products`, `POST /products`, `GET /products/{id}`, `PUT /products/{id}`, `DELETE /products/{id}` |


## Logs e Debugging

### Logback Configuration (logback-spring.xml)

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="com.api_arquitetura_example" level="DEBUG"/>
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

## Health Checks Implementados

### CustomDbHealthIndicator
```json
{
  "status": "UP",
  "details": {
    "banco": "H2 In-Memory",
    "schema": "delivery_db",
    "status": "Conectado"
  }
}
```

### GatewayHealthIndicator
```json
{
  "status": "UP",
  "details": {
    "url": "https://api.pagamentos.com",
    "latencia": "45ms"
  }
}
```


## Testes e Qualidade

### JaCoCo Configuração

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <configuration>
        <rules>
            <rule>
                <element>PACKAGE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</plugin>
```

## Execução de Testes

| Comando | Descrição |
|---------|-------------|
| `mvn test` | Testes unitários |
| `mvn verify` | Testes de integração |
| `mvn jacoco:report` | Gerar relatório de cobertura |
| `open target/site/jacoco/index.html` | Visualizar relatório |



## Prometheus Queries Essenciais

### Métricas de Requisições HTTP
```promql
# Total de requisições por segundo
rate(http_server_requests_seconds_count[1m])

# Requisições por endpoint
sum by (uri) (rate(http_server_requests_seconds_count[1m]))

# Requisições por status HTTP
sum by (status) (rate(http_server_requests_seconds_count[1m]))

# Taxa de erro (5xx)
sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) 
/ 
sum(rate(http_server_requests_seconds_count[5m]))
```


### Métricas de Performance
```promql
# Latência média por endpoint
rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])

# Percentil 95 de latência
histogram_quantile(0.95, sum by (le, uri) (rate(http_server_requests_seconds_bucket[1m])))

# Percentil 99 de latência
histogram_quantile(0.99, sum by (le, uri) (rate(http_server_requests_seconds_bucket[1m])))
```

### Métricas de Sistema
```promql
# Uso de memória heap (MB)
jvm_memory_used_bytes{area="heap"} / 1024 / 1024

# Threads ativas
jvm_threads_live_threads

# CPU usage
process_cpu_usage

# Uptime da aplicação (horas)
process_uptime_seconds / 3600
```


### Métricas de Banco de Dados
```promql
# Conexões ativas no pool
hikaricp_connections_active

# Conexões ociosas
hikaricp_connections_idle

# Tempo mínimo de conexão (ms)
hikaricp_connection_timeout_total
```


## Consequências da Decisão

### Positivas

| Benefício | Impacto |
|-----------|---------|
| ✅ Visibilidade completa da aplicação | Debugging facilitado |
| ✅ Métricas de performance em tempo real | Monitoramento proativo |
| ✅ Qualidade de código garantida | 80% de cobertura mínima |
| ✅ Tracing distribuído | Identificação rápida de gargalos |

### Negativas

| Desvantagem | Impacto |
|-------------|---------|
| ❌ Overhead de performance | 3-5% com tracing |
| ❌ Complexidade operacional | Mais serviços para gerenciar |
| ❌ Consumo extra de recursos | Memória/CPU adicional |

## ADR - Architecture Decision Record

Esta implementação é baseada na **ADR-008: Implementação de Observabilidade e Monitoramento para API de Produtos**.

### Decisões Tomadas

| Alternativa | Decisão | Justificativa |
|-------------|---------|----------------|
| ELK Stack vs Jaeger + Prometheus | Jaeger + Prometheus | Menos consumo de recursos |
| New Relic vs Prometheus | Prometheus | Open source, sem custos |
| JUnit vs JaCoCo | Ambos | JUnit para testes, JaCoCo para cobertura |

| Item | Detalhe |
|------|---------|
| **Aprovado por** | Time de Arquitetura |
| **Data de Implementação** | 09/06/2026 |
| **Versão** | 1.0 |
| **Status** | Aceito |

## Referências

- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Jaeger Documentation](https://www.jaegertracing.io/docs/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/)

