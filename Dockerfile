FROM eclipse-temurin:21-ubi9-minimal
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-XX:MaxRAM=100m", "-Xss256k", "-XX:+UseSerialGC", "-jar", "/app.jar"]
