FROM openjdk:21-jdk-slim
WORKDIR /userService
COPY target/*.jar userService.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "userService.jar"]