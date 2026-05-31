FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY family-proto/ family-proto/
COPY device-proto/ device-proto/

COPY pom.xml .
COPY src/ src/

RUN cd family-proto && mvn install -q && cd ..
RUN cd device-proto && mvn install -q && cd ..

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java","-jar","app.jar"]