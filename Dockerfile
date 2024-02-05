FROM eclipse-temurin:21-jdk-alpine
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx180M", "-jar", "-noverify", "/app.jar"]
