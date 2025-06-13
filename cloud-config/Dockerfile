
FROM openjdk:11
ARG PROJECT_VERSION=0.1.0
RUN mkdir -p /home/app
WORKDIR /home/app
COPY target/cloud-config-v${PROJECT_VERSION}.jar cloud-config.jar
COPY e2e-tests /home/app/e2e-tests
RUN [ -d /home/app/e2e-tests ] && ls -l /home/app/e2e-tests || echo "⚠️  e2e-tests no existe"
EXPOSE 9296
ENTRYPOINT ["java", "-jar", "cloud-config.jar"]


