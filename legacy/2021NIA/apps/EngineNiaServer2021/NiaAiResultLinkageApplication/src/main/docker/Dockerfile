# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="rca-topasAlLinkage"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8096 available to the world outside this container
EXPOSE 8097

# The application's jar file
ARG JAR_FILE=/target/com.nia.ai.performance.result-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD com.nia.ai.performance.result-0.0.1-SNAPSHOT.jar topasAlLinkage.jar
#COPY --from=builder ${JAR_FILE} topas.jar

# Run the jar file
ENTRYPOINT ["java","-Djasypt.encryptor.password=ktRca12#$","-jar","/engine.jar","--spring.profiles.active=dev"]
