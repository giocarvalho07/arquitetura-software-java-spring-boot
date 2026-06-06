# Estágio 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Runtime (Execução)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia o jar gerado com o nome correto do seu pom.xml
COPY --from=build /app/target/api-arquitetura-example-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]