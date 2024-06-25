# Use an official OpenJDK runtime as a parent image
FROM openjdk:22-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application (replace 'mvnw' with 'mvn' if you use Maven Wrapper)
RUN ./mvnw package -DskipTests

# Second stage: Run the application
FROM openjdk:22-jdk-slim

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar maven.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "maven.jar"]