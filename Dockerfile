FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

COPY . .

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:21-jdk-alpine

COPY --from=builder /workspace/target/api-*.jar banking-api.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $BANKING_API_JAVA_OPTS -jar banking-api.jar"]