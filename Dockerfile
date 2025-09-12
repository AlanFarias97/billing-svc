# --- Fase build ---
    FROM maven:3.9-eclipse-temurin-21 AS build
    WORKDIR /build
    COPY pom.xml .
    RUN mvn -q -e -DskipTests dependency:go-offline
    COPY src ./src
    RUN mvn -q -DskipTests package
    
    # --- Fase run ---
    FROM eclipse-temurin:21-jre
    WORKDIR /app
    COPY --from=build /build/target/*.jar app.jar
    ENTRYPOINT ["java","-XX:+UseZGC","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
    