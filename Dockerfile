FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN groupadd spring && useradd -r -g spring spring
USER spring:spring
ENTRYPOINT ["java","-jar","/app.jar"]