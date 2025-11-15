FROM eclipse-temurin:21
WORKDIR /app
COPY target/*payment*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
