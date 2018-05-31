#!/usr/bin/env bash

docker-compose down
./gradlew clean build buildDocker
docker system prune -f
docker volume prune -f
docker-compose up -d