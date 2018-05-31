#!/usr/bin/env bash

function prune(){
    docker system prune -f
    docker volume prune -f
}

function stopAll(){
    docker-compose down
}

function buildAll(){
    ./gradlew clean build buildDocker
}

function upAll(){
    docker-compose up -d
}

function dockerps(){
    docker ps -a
}

if [ "$1" = "stop" ]; then
    stopAll
    exit 0
elif [ "$1" = "rebuild-start" ]; then
    stopAll
    buildAll
    prune
    upAll
elif [ "$1" = "rebuild" ]; then
    stopAll
    buildAll
    prune
elif [ "$1" = "start" ]; then
    prune
    upAll
elif [ "$1" = "status" ]; then
    dockerps
    prune
fi


