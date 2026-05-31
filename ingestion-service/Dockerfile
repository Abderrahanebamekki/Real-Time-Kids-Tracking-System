FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy ONLY geofencing-service content correctly
COPY geofencing-service/.mvn/ .mvn/
COPY geofencing-service/mvnw ./
COPY geofencing-service/pom.xml ./
COPY geofencing-service/src/ src/

# Copy proto modules (same level as geofencing-service)
COPY family-proto/ family-proto/
COPY device-proto/ device-proto/

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

RUN cd family-proto && ../mvnw clean install -DskipTests
RUN cd device-proto && ../mvnw clean install -DskipTests

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]