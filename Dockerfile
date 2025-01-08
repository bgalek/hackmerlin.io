FROM bellsoft/liberica-openjdk-alpine:21
COPY ./build/libs/merlin.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Xmx180M", "-Xss256k", "-jar", "/app.jar"]
