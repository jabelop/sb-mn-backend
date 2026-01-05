# Use a base image that has OpenJDK 19 installed
FROM openjdk:17-ea-5-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file to the container
COPY combats/build/libs/combats-all.jar app.jar

# Specify the command to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
