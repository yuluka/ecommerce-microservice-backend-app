FROM openjdk:11

ARG PROJECT_VERSION=0.1.0

WORKDIR /home/app

ENV SPRING_PROFILES_ACTIVE=dev

ADD target/*.jar app.jar

EXPOSE 8700

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]