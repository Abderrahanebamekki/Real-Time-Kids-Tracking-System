FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY family-proto/ family-proto/
COPY device-proto/ device-proto/

COPY pom.xml .
COPY src/ src/

RUN cd family-proto && mvn install -q && cd ..
RUN cd device-proto && mvn install -q && cd ..

RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]