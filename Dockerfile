# Multi-stage Dockerfile for DevOps Atolyesi Application
# Stage 1: Build Stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds in CI/CD)
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jre-alpine

# Install necessary packages for security and debugging
RUN apk add --no-cache \
    curl \
    wget \
    tzdata \
    && rm -rf /var/cache/apk/*

# Set timezone
ENV TZ=Europe/Istanbul

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Environment variables for Spring Boot
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Labels for better image management
LABEL maintainer="serdarselcuk@gmail.com"
LABEL version="1.0"
LABEL description="DevOps Atolyesi Bitirme Projesi - Spring Boot Application"
LABEL project="DevOps-Atolyesi-Bitirme-Projesi" 