FROM openjdk:17-oracle
ENV SPRING_PROFILES_ACTIVE=prod
COPY auth-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]