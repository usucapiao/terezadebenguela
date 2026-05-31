# =============================================================
# Estágio 1 — Build (Maven + JDK 25)
# =============================================================
FROM maven:3.9.16-eclipse-temurin-25-alpine AS build

WORKDIR /app

# Copia o descritor de dependências primeiro para aproveitar cache de camadas
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B -q

# Copia o código-fonte e gera o JAR
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B -q

# =============================================================
# Estágio 2 — Runtime (JRE 25 mínimo)
# =============================================================
FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app

# Usuário não-root para segurança
RUN addgroup -S teresa && adduser -S teresa -G teresa

# Copia apenas o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Diretório de uploads persistido via volume
RUN mkdir -p /app/uploads && chown teresa:teresa /app/uploads

USER teresa

EXPOSE 8080

VOLUME ["/app/uploads"]

ENTRYPOINT ["java", "-jar", "app.jar"]
