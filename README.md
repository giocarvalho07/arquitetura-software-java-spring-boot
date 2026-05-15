[JPA e Acesso a dados] - Arquitetura de Sistemas com Spring Boot 3.4+


Este ebook documenta a implementação de uma API RESTful utilizando o ecossistema Spring, focando na separação de responsabilidades e na persistência de dados eficientes.

Objetivo
O objetivo é fornecer uma referência técnica para a construção de uma API CRUD (Create, Read, Update, Delete) robusta. A solução visa resolver o problema de acoplamento entre a lógica de negócio e o acesso a dados, utilizando o padrão de camadas para garantir que o sistema seja fácil de testar, manter e escalar.

Contexto e Fundamentação
Conceitos-Chave
Spring Data JPA: Abstração sobre o Hibernate que elimina a necessidade de escrever queries SQL manuais para operações comuns, utilizando o conceito de Repositórios.
H2 & Drivers Nativos: Utilização de banco de dados em memória (H2) para agilidade em desenvolvimento e testes, com suporte pronto para ambientes produtivos (MySQL/PostgreSQL).
Injeção de Dependência: Uso do @Autowired ou construtores para desacoplar a criação de objetos da sua utilização.

Arquitetura de Camadas

Diagrama do Projeto
O fluxo de dados segue o padrão Controller -> Service -> Repository -> Database.

Exemplo Prático
Implementação completa baseada em Java 21.
Configurações no Spring Initializr
Para este projeto, utilize as seguintes definições no start.spring.io:
Project: Maven
Language: Java
Spring Boot: 3.x.x (versão estável mais recente)
Java: 21
Packaging: Jar
Dependências Necessárias:
Spring Web: Para criar os endpoints REST e usar o Tomcat embutido.
Spring Data JPA: Para persistência de dados e uso do Hibernate.
H2 Database: Banco de dados em memória para testes rápidos (não precisa instalar nada na máquina).
Lombok (Opcional, mas recomendado): Para reduzir o código boilerplate (getters/setters).
