FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN pwd
RUN ls -la
RUN find . -name pom.xml

RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

COPY --from=builder /app/identity-family/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]