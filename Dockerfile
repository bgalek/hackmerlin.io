FROM eclipse-temurin:20-jdk-alpine
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx180M", "-jar", "/app.jar"]
