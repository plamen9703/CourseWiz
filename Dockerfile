#FROM openjdk:17
#
#WORKDIR /app
#
#COPY . /app/
#
#RUN javac Main.java


## ---------- Stage 1: Build the JAR ----------
#FROM maven:3.9.6-eclipse-temurin-17 AS builder
#
## Set work directory
#WORKDIR /app
#
## Copy only pom.xml first for dependency caching
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY config.yml config.yml
#
## Copy source code and build
#COPY src ./src
#RUN mvn clean package
#
## ---------- Stage 2: Create lightweight runtime image ----------
#FROM eclipse-temurin:17-jre-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy the fat JAR from builder
#COPY --from=builder /app/target/*-shaded.jar app.jar
#
## Expose the application port (adjust to your Dropwizard config)
#EXPOSE 8080
#
## Command to run Dropwizard
#CMD ["java", "-jar", "app.jar", "server", "config.yml"]
# 1. Use an official lightweight JDK image
FROM eclipse-temurin:17-jre-alpine

# 2. Set working directory
WORKDIR /app

# 3. Copy the fat JAR from Maven target folder into the container
COPY target/CourseWiz-1.0-SNAPSHOT-shaded.jar CourseWiz-1.0-SNAPSHOT-shaded.jar

COPY config.yml /app/config.yml
# 4. Expose the application port (adjust if your Dropwizard runs on another port)
EXPOSE 8080 3000

# 5. Command to run your app
ENTRYPOINT ["java", "-jar", "CourseWiz-1.0-SNAPSHOT-shaded.jar", "server", "config.yml"]
