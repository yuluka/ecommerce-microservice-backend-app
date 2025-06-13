
FROM openjdk:11
ARG PROJECT_VERSION=0.1.0
RUN mkdir -p /home/app
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE dev
COPY target/api-gateway-v${PROJECT_VERSION}.jar api-gateway.jar
COPY e2e-tests /home/app/e2e-tests
RUN [ -d /home/app/e2e-tests ] && ls -l /home/app/e2e-tests || echo "⚠️  e2e-tests no existe"
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "api-gateway.jar"]


