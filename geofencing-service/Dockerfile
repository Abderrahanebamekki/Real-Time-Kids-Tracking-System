# ========================
# Stage 1: Builder
# ========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy proto modules
COPY family-proto/ family-proto/
COPY device-proto/ device-proto/

# Copy geofencing service files
COPY geofencing-service/.mvn/ .mvn/
COPY geofencing-service/mvnw ./
COPY geofencing-service/pom.xml ./
COPY geofencing-service/src/ src/

# Install proto modules into local Maven repo
RUN cd family-proto && ../mvnw install -q && cd ..
RUN cd device-proto && ../mvnw install -q && cd ..

# Build the geofencing service
RUN ./mvnw clean package -DskipTests -q

# ========================
# Stage 2: Runner
# ========================
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]