#!/bin/sh

docker stop telegram_bot_kt
docker rm telegram_bot_kt

docker build -t telegram_bot_kt --no-cache .

# this is used for production
docker run -dt --restart on-failure -p 13337:13337 -m80m --name telegram_bot_kt telegram_bot_kt

# this is used when testing as it attaches to the console
#docker run -p 13337:13337 -m80m --name telegram_bot_kt telegram_bot_kt
