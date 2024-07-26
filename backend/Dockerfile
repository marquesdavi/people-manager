# Dockerfile para o backend
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY compose.yaml .
COPY src/main/resources/application.properties src/main/resources/application.properties

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]
