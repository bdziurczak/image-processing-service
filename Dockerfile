FROM ubuntu:latest
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
LABEL authors="bdziurczak"

ENTRYPOINT ["top", "-b"]