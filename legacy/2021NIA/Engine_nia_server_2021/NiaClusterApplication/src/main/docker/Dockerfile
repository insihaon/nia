# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="rca-cluster"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8095 available to the world outside this container
EXPOSE 8095

# The application's jar file
ARG JAR_FILE=/target/com.nia.rca.cluster.preprocessor-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD com.nia.rca.cluster.preprocessor-0.0.1-SNAPSHOT.jar cluster.jar
#COPY --from=builder ${JAR_FILE} topas.jar

# Run the jar file
ENTRYPOINT ["java","-Djasypt.encryptor.password=ktRca12#$","-jar","/cluster.jar","--spring.profiles.active=dev"]
