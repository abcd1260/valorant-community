# Build Stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
# Build the application, skipping tests to save time and avoid environment-specific failures
RUN gradle bootJar -x test --no-daemon

# Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
