FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o arquivo jar
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]