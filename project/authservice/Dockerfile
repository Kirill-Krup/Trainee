FROM eclipse-temurin:21
WORKDIR /app
COPY target/*auth*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
