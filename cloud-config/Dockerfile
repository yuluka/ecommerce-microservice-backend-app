FROM openjdk:11

# Parámetro de versión del jar
ARG PROJECT_VERSION=0.1.0

# Crear y usar directorio de trabajo
WORKDIR /home/app

# Copiar el jar directamente
ADD cloud-config/target/cloud-config-v${PROJECT_VERSION}.jar cloud-config.jar

# Exponer puerto usado por el microservicio
EXPOSE 9296

# Comando de arranque
ENTRYPOINT ["java", "-jar", "cloud-config.jar"]
