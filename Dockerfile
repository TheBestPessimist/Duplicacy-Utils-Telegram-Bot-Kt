# Created with help from https://codefresh.io/docker-tutorial/java_docker_pipeline/

#FROM openjdk:12-alpine as builder
FROM gradle:6.0.1-jdk13

# Install the CA certificates for the app to be able to make calls to HTTPS endpoints
# Ref: https://github.com/drone/ca-certs
#RUN apk add --no-cache ca-certificates git

RUN apk add --no-cache htop

WORKDIR /app

# Copy, compile and make the fat jar
COPY . .
#RUN ./gradlew shadowJar --no-daemon
RUN gradle shadowJar --no-daemon

# The port is mandatory for the app
EXPOSE 13337:13337

# TODO ?!?!?! why does this fail with OOM, when on windows, -Xmx20m is enough to have my simple webserver running?
CMD ["java", "-server", "-Xmx20m", "-Xshare:off",  "-XshowSettings:vm", "-jar", "/app/build/libs/Duplicacy-Utils-Telegram-Bot.jar"]




## Create the user and group files that will be used in the running container to
## run the process as an unprivileged user
#RUN mkdir /user && \
#    echo 'nobody:x:65534:65534:nobody:/:' > /user/passwd && \
#    echo 'nobody:x:65534:' > /user/group
#
#
## Final stage: copy only the needed data
#FROM openjdk:13-alpine
#
## Copy the user and group files from the builder stage
#COPY --from=builder /user/group /user/passwd /etc/
#
## Copy the CA certificates for enabling HTTPS
#COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
#
## Copy the fat jar from the builder stage
#COPY --from=builder /app.exe ./app -- todo
#
## Copy any configuration files there may be
#COPY --from=builder /app/config/ ./config/
#
## The port used by the application
#EXPOSE 13337
#
## Perform any further action as an unprivileged user
#USER nobody:nobody
#
## The env variables needed for the app
#ENV LISTENING_PORT=13337
#ENV CERTIFICATE_PATH=/etc/letsencrypt/live/tbp.land/fullchain.pem
#
