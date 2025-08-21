FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only POM files first to leverage Docker cache for dependencies
COPY pom.xml .
# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B
# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk
RUN groupadd spring && useradd -g spring spring

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java","-jar","app.jar"]
