# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
# Give permission and build the jar
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy only the built jar from the first stage
COPY --from=build /app/build/libs/*.jar samvada_app.jar

# Standard Port for Spring Boot
EXPOSE 8080

# The Entrypoint
ENTRYPOINT ["java", "-jar", "samvada_app.jar"]