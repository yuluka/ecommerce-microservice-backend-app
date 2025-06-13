
FROM openjdk:11

ARG PROJECT_VERSION=0.1.0

RUN mkdir -p /home/app

WORKDIR /home/app

ENV SPRING_PROFILES_ACTIVE dev

COPY target/user-service-v${PROJECT_VERSION}.jar user-service.jar
COPY e2e-tests /home/app/e2e-tests

RUN [ -d /home/app/e2e-tests ] && ls -l /home/app/e2e-tests || echo "⚠️  e2e-tests no existe"

EXPOSE 8700

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "user-service.jar"]


