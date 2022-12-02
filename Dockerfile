FROM azul/zulu-openjdk-alpine:11

COPY ./dist/app.jar /app/app.jar
WORKDIR /app

EXPOSE 8080

ENTRYPOINT [ "java", "-Dport=8080", "-DisProd=true", "-jar", "app.jar" ]
