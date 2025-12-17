# Multi-stage build for optimized production image
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM tomcat:9-jdk17-temurin-jammy

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR file from build stage
COPY --from=build /app/target/Hankyo.war /usr/local/tomcat/webapps/ROOT.war

# Set environment variables (will be overridden by docker-compose or runtime)
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=hankyo
ENV DB_USER=postgres
ENV DB_PASSWORD=password
ENV DB_DRIVER=org.postgresql.Driver
ENV DB_MAX_POOL_SIZE=20
ENV DB_MIN_IDLE=10

# Google Cloud AI Platform
ENV GOOGLE_PROJECT_ID=""
ENV GOOGLE_LOCATION=""
ENV GOOGLE_APPLICATION_CREDENTIALS=""

# Cloudinary Configuration
ENV CLOUDINARY_CLOUD_NAME=""
ENV CLOUDINARY_API_KEY=""
ENV CLOUDINARY_API_SECRET=""

# PayOS Configuration
ENV PAYOS_CLIENT_ID=""
ENV PAYOS_API_KEY=""
ENV PAYOS_CHECKSUM_KEY=""

# Email Configuration
ENV MAIL_HOST=smtp.gmail.com
ENV MAIL_PORT=587
ENV MAIL_USERNAME=""
ENV MAIL_PASSWORD=""

# Application Configuration
ENV APP_ENV=production
ENV APP_PORT=8080

# Create directory for logs
RUN mkdir -p /usr/local/tomcat/logs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]