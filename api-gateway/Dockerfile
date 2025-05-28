FROM openjdk:11

WORKDIR /home/app

ENV SPRING_PROFILES_ACTIVE=dev

# Copiar solo el JAR generado por Maven
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]
