FROM azul/zulu-openjdk-alpine:17

# -- Create and enter into working directory

# Copy the fat jar into the docker container
COPY "./dist/app.jar" "/app/app.jar"

# Allow external access to the port that the web service will be running on inside the container
EXPOSE $PORT

#WORKDIR "/app"

ENTRYPOINT [ "java", "-Dport=8080", "-DisProd=true", "-jar", "/app/app.jar" ]
