
FROM openjdk:11
ARG PROJECT_VERSION=0.1.0
RUN mkdir -p /home/app
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE dev
COPY target/service-discovery-v${PROJECT_VERSION}.jar service-discovery.jar
COPY e2e-tests /home/app/e2e-tests
RUN [ -d /home/app/e2e-tests ] && ls -l /home/app/e2e-tests || echo "⚠️  e2e-tests no existe"
EXPOSE 8761
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "service-discovery.jar"]


