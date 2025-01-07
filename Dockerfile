FROM bellsoft/liberica-openjdk-alpine:23
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx180M", "-Xss256k", "-jar", "/app.jar"]
