FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only Maven files first to leverage Docker cache
COPY pom.xml .
COPY .mvn/ .mvn
COPY mvnw .

# Use BuildKit cache for Maven repository
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:resolve-plugins dependency:go-offline -B

# Copy source code after dependencies cached
COPY src ./src

# Package the application, skip tests for faster build
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

#############################
# Runtime stage
#############################
FROM eclipse-temurin:21-jdk
RUN groupadd spring && useradd -g spring spring

WORKDIR /app
# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Switch to non-root user
USER spring:spring

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]