FROM amazoncorretto:17

WORKDIR /app
COPY target/text-matcher-0.0.1-SNAPSHOT.jar /app/text-matcher-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/text-matcher-0.0.1-SNAPSHOT.jar"]