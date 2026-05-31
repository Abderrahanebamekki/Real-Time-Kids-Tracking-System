# ========================
# Stage 1: Builder
# ========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy everything from repo root
COPY . .

# Build the geofencing service
RUN mvn clean package -DskipTests

# ========================
# Stage 2: Runner
# ========================
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

COPY --from=builder /app/geofencing-service/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]