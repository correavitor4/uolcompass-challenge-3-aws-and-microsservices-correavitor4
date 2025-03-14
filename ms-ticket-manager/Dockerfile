# Etapa 1: Construção da aplicação
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY ms-ticket-manager/mvnw .
COPY ms-ticket-manager/.mvn .mvn
COPY ms-ticket-manager/pom.xml .
COPY ms-ticket-manager/src src

# Concede permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Compila a aplicação e gera o JAR
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagem final com JRE para execução
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do serviço
EXPOSE 8081

# Comando de inicialização
CMD ["java", "-jar", "app.jar"]
