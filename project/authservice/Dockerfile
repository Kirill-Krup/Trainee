FROM openjdk:21-jdk-slim
WORKDIR /authService
COPY target/*.jar authservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authservice.jar"]