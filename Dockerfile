FROM gradle:6.0.1-jdk13 as builder

WORKDIR /app

# Copy, compile and make the fat jar
COPY . .
RUN gradle shadowJar --no-daemon

#
#
# Final stage: copy only the needed data
FROM openjdk:12-alpine

# Install the CA certificates for the app to be able to make calls to HTTPS endpoints
# Ref: https://github.com/drone/ca-certs
RUN apk add --no-cache ca-certificates git

# Define the user used in this instance. Prevent using root as even in a container it can be a security risk.
ENV APPLICATION_USER kotlin

# Add the user.
RUN adduser -D -g '' $APPLICATION_USER

# Marks this container to use the specified $APPLICATION_USER
USER $APPLICATION_USER

WORKDIR /app

# Copy the fat jar into the /app folder.
COPY --from=builder /app/build/libs/Duplicacy-Utils-Telegram-Bot.jar ./

# Copy the settings file into the /app folder.
COPY --from=builder /app/src/main/resources/configuration.properties ./

# The port is mandatory for the app
EXPOSE 13337

RUN ls -l

CMD ["java", "-server", "-XX:+ExitOnOutOfMemoryError", "-XX:+TieredCompilation", "-XX:TieredStopAtLevel=1", "-XshowSettings:vm", "-Xmx40m", "-jar", "Duplicacy-Utils-Telegram-Bot.jar"]
