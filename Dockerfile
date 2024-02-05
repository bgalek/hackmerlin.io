FROM eclipse-temurin:21-ubi9-minimal
COPY ./backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx180M", "-Xss256k", "-XX:+UseSerialGC", "-jar", "/app.jar"]
