# Étape 1 : Construction du projet
# Utiliser une image de base avec Maven et JDK 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copier le fichier parent pom.xml, shared, et le pom.xml du microservice
COPY ../pom.xml ./parent-pom.xml
COPY ../shared ./shared
COPY pom.xml ./pom.xml

# Télécharger les dépendances Maven pour tout le projet (y compris parent et shared)
RUN --mount=type=cache,target=/root/.m2 mvn -f parent-pom.xml dependency:go-offline -B

# Copier le code source du microservice
COPY src ./src

# Construire le projet du microservice
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# Étape 2 : Exécution du projet
# Utiliser une image légère JRE pour exécuter l'application
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copier le JAR du projet depuis l'étape de build précédente
COPY --from=build /app/target/gateway-service-1.0-SNAPSHOT.jar /app/gateway-service.jar

# Exposer le port sur lequel l'application va tourner
EXPOSE 8080

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]
