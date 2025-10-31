# Use official Java runtime as base image
FROM amazoncorretto:17-alpine

# Create non-root user
RUN addgroup -g 1001 appgroup && adduser -D -u 1001 -G appgroup appuser

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Change ownership to appuser
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Set environment variables for production
ENV SPRING_PROFILES_ACTIVE=production

# Run the application
CMD ["java", "-jar", "target/relational-data-access-0.0.1-SNAPSHOT.jar"]