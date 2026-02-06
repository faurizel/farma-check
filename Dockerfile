# =============================================================
# ESTÁGIO 1: Build (Compilação)
# Usamos uma imagem com Maven para gerar o arquivo .jar
# =============================================================
FROM maven:3.9.6-amazoncorretto-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de dependências
COPY pom.xml .

# (Opcional) Baixa as dependências para o cache do Docker
# Isso faz com que builds futuros sejam muito mais rápidos
RUN mvn dependency:go-offline

# Copia o código fonte do projeto
COPY src ./src

# Compila o projeto e gera o JAR (pulando testes para agilizar o build do docker)
RUN mvn clean package -DskipTests

# =============================================================
# ESTÁGIO 2: Run (Execução)
# Usamos uma imagem leve apenas com o Java (sem Maven) para rodar
# =============================================================
FROM amazoncorretto:21

WORKDIR /app

# Copia apenas o JAR gerado no estágio anterior (build)
# O nome do jar pode variar, o asterisco (*) garante que ele pegue o gerado
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8090

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]