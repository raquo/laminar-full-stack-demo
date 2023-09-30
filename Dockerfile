FROM azul/zulu-openjdk-alpine:17

ARG SOURCE_JAR_PATH="./dist/app.jar"

ARG TARGET_JAR_PATH="/app/app.jar"

ARG PORT=8080

# -- Create and enter into working directory

# Copy the fat jar into the docker container
COPY ${SOURCE_JAR_PATH} ${TARGET_JAR_PATH}

# Allow external access to the port that the web service will be running on inside the container
EXPOSE $PORT

#WORKDIR "/app"

ENTRYPOINT [ "java", "-Dport=${PORT}", "-DisProd=true", "-jar", "${TARGET_JAR_PATH}" ]
