FROM eclipse-temurin:21-jdk-alpine
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-XX:MaxRAM=150m", "-Xss256k", "-XX:+UseSerialGC", "-jar", "/app.jar"]
