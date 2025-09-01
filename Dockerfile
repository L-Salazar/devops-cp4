# ===== Etapa de build =====
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia apenas o pom para aproveitar cache de dependências
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Agora copia o restante do código
COPY src ./src

# Build do artefato (sem testes)
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn -q -B clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# ===== Etapa de runtime =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instala curl para o healthcheck do docker-compose
RUN apk add --no-cache curl

# Cria usuário/grupo não-root
RUN addgroup -S spring && adduser -S spring -G spring

# Copia o jar do build e ajusta ownership
COPY --from=builder --chown=spring:spring /app/target/*.jar /app/app.jar

# Troca para o usuário sem privilégios
USER spring

# Porta da aplicação
EXPOSE 8081

# (Opcional) Variáveis úteis
# ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
# ENV SERVER_PORT=8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
