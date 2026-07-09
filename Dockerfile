FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

ARG SERVICE_NAME

RUN mvn clean package -pl ${SERVICE_NAME} -am -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

ARG SERVICE_NAME

COPY --from=builder /app/${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]