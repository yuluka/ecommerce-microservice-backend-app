FROM openjdk:11

# Parámetro de versión del jar
ARG PROJECT_VERSION=0.1.0

# Crear y usar directorio de trabajo
WORKDIR /home/app

ENV SPRING_PROFILES_ACTIVE=dev

# Copiar el jar directamente
ADD target/*.jar app.jar

# Exponer puerto usado por el microservicio
EXPOSE 8888

# Comando de arranque
# ENTRYPOINT ["java", "-jar", "cloud-config.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]
