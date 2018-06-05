#!/usr/bin/env bash

ENV_TYPES=("generic" "clustering" "sharding")

function echoCurrentEnv(){
  echo "CURRENT_ENV = $(cat CURRENT_ENV)"
}

function prune(){
    docker system prune -f 1> /dev/null
    docker volume prune -f 1> /dev/null
}

function stopAll(){
    docker-compose -f compose-$(cat CURRENT_ENV).yml down
}

function buildAll(){
    ./gradlew clean build buildDocker
}

function upAll(){
    echoCurrentEnv
    docker-compose -f compose-$(cat CURRENT_ENV).yml up -d
}

function printUsage(){
  echo './manager.sh env generic|clustering|sharding    #change env'
  echo './manager.sh reset                              #stop all containers and reset env to generic'
  echo './manager.sh stop                               #stop all containers'
  echo './manager.sh rebuild-start                      #rebuild and restart all containers'
  echo './manager.sh rebuild                            #rebuild all containers'
  echo './manager.sh start                              #start all containers'
  echo './manager.sh status                             #show all containers status'
  echo ''
  echo "CURRENT_ENV = $(cat CURRENT_ENV)"
}

function dockerps(){
    docker ps -a
}

function containsElement(){
    local e match="$1"
    shift
    for e; do [[ "$e" == "$match" ]] && return 0; done
    return 1
}

function changeEnv(){
  containsElement "$1" "${ENV_TYPES[@]}"
  if [ "$?" = "0" ];then
      echo "$1" > CURRENT_ENV
      echoCurrentEnv
      stopAll
  fi
}

if [ $# -gt 1 ] && [ "$1" != "" ] && [ "$1" = "env" ] && [ "$2" != "" ]; then
    changeEnv $2
elif [ "$1" = "reset" ]; then
    stopAll
    prune
    echo "generic" > CURRENT_ENV
    echoCurrentEnv
elif [ "$1" = "" ]; then
    printUsage
elif [ "$1" = "stop" ]; then
    stopAll
    prune
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
    prune
    echoCurrentEnv
    dockerps
fi
