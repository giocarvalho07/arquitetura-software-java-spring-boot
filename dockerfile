# Estágio de execução: utiliza uma imagem leve do JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Define um diretório de trabalho temporário
VOLUME /tmp

# Copia o arquivo .jar gerado pelo Maven para dentro do container como 'app.jar'
COPY target/*.jar app.jar

# Expor a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para executar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "/app.jar"]