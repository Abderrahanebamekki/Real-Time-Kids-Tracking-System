FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

COPY family-proto/ family-proto/
COPY device-proto/ device-proto/

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src/ src/

RUN cd ./../family-proto && ../mvnw clean install -DskipTests
RUN cd ./../device-proto && ../mvnw clean install -DskipTests

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]