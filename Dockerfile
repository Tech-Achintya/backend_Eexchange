# Stage 1: Build the Spring Boot application
# Uses a JDK 21 image from Eclipse Temurin for compilation.
FROM eclipse-temurin:21-jdk-focal AS builder

# Set the working directory inside the container for the build stage.
WORKDIR /app

# Copy the Maven wrapper and its directory.
# This allows you to use the Maven wrapper (mvnw) inside the container.
COPY mvnw .
COPY .mvn .mvn

# Copy the build configuration file.
# pom.xml: Main Maven build file.
COPY pom.xml .

# Copy the source code.
# The `src` directory contains your Java source files, resources, etc.
COPY src src

# Make the Maven wrapper script executable (useful for Linux/Mac environments).
RUN chmod +x mvnw

# Build the Spring Boot application into an executable JAR.
# `clean package` creates the executable JAR in the target/ directory.
# `-DskipTests` skips running tests during the Docker build, speeding it up.
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final, lightweight runtime image
# Uses a JRE 21 image, which is much smaller than a full JDK image.
FROM eclipse-temurin:21-jre-focal

# Set the working directory inside the container for the runtime stage.
WORKDIR /app

# Copy the executable JAR from the builder stage to the final image.
# The JAR is copied from `/app/target/backend-0.0.1-SNAPSHOT.jar` (based on your pom.xml)
# and renamed to `app.jar` in the current stage for simplicity.
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the default port on which your Spring Boot application will listen (8080).
EXPOSE 8080

# Define the command to run your application when the container starts.
# Spring Boot will respect the `PORT` environment variable if set by hosting platforms like Render.
ENTRYPOINT ["java", "-jar", "app.jar"]
