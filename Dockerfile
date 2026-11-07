# Stage 1: Build JAR using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build package
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal Runtime Environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root system user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built JAR from stage 1
COPY --from=builder /app/target/*.jar app.jar

# Set permissions and switch user
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "app.jar"]
