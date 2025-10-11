FROM openjdk:21-jdk-slim
WORKDIR /orderService
COPY target/*.jar orderService.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "orderService.jar"]