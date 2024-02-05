FROM eclipse-temurin:21-jdk-alpine
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
