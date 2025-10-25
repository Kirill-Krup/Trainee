FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*order*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
