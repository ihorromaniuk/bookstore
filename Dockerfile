FROM maven:3.9.9 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY checkstyle.xml .
COPY src ./src
COPY *.env .
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM openjdk:21
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/book-shop.jar .
# Set the command to run the application
CMD ["java", "-jar", "book-shop.jar"]
