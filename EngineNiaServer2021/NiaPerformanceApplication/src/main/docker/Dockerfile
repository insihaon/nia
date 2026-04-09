# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="mba-ap"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8096 available to the world outside this container
EXPOSE 8096

# The application's jar file
ARG JAR_FILE=/target/com.nia.ai.per.ap-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD com.nia.ai.per.ap-0.0.1-SNAPSHOT.jar mbaAp.jar
#COPY --from=builder ${JAR_FILE} topas.jar

# Run the jar file
ENTRYPOINT ["java","-Djasypt.encryptor.password=ktRca12#$","-jar","/mbaAp.jar","--spring.profiles.active=dev"]
