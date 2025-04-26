FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
# Copy the POM file first
COPY ./pom.xml ./
# Copy the src directory
COPY ./src ./src
# Build the application
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"] 