# BE/Dockerfile

FROM gradle:6.8-jdk8 AS build
WORKDIR /app

# Copy all BE files to /app
COPY . /app

# Run gradle bootJar to build the JAR
WORKDIR /app/app-ipms
RUN gradle bootJar

FROM openjdk:8-jre-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/app-ipms/build/libs/app-ipms.jar /app/app.jar
# COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]