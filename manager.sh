#!/usr/bin/env bash
set -e
RCol='\x1B[0m'    # Text Reset
#--------------------------------------------------------------------------------------------------------------------------------------------------
# ALL COLORS
#--------------------------------------------------------------------------------------------------------------------------------------------------
# Regular             Bold                  Underline             High Intensity        BoldHigh Intens       Background          High Intensity Backgrounds
Bla='\x1B[0;30m';     BBla='\x1B[1;30m';    UBla='\x1B[4;30m';    IBla='\x1B[0;90m';    BIBla='\x1B[1;90m';   On_Bla='\x1B[40m';    On_IBla='\x1B[0;100m';
Red='\x1B[0;31m';     BRed='\x1B[1;31m';    URed='\x1B[4;31m';    IRed='\x1B[0;91m';    BIRed='\x1B[1;91m';   On_Red='\x1B[41m';    On_IRed='\x1B[0;101m';
Gre='\x1B[0;32m';     BGre='\x1B[1;32m';    UGre='\x1B[4;32m';    IGre='\x1B[0;92m';    BIGre='\x1B[1;92m';   On_Gre='\x1B[42m';    On_IGre='\x1B[0;102m';
Yel='\x1B[0;33m';     BYel='\x1B[1;33m';    UYel='\x1B[4;33m';    IYel='\x1B[0;93m';    BIYel='\x1B[1;93m';   On_Yel='\x1B[43m';    On_IYel='\x1B[0;103m';
Blu='\x1B[0;34m';     BBlu='\x1B[1;34m';    UBlu='\x1B[4;34m';    IBlu='\x1B[0;94m';    BIBlu='\x1B[1;94m';   On_Blu='\x1B[44m';    On_IBlu='\x1B[0;104m';
Pur='\x1B[0;35m';     BPur='\x1B[1;35m';    UPur='\x1B[4;35m';    IPur='\x1B[0;95m';    BIPur='\x1B[1;95m';   On_Pur='\x1B[45m';    On_IPur='\x1B[0;105m';
Cya='\x1B[0;36m';     BCya='\x1B[1;36m';    UCya='\x1B[4;36m';    ICya='\x1B[0;96m';    BICya='\x1B[1;96m';   On_Cya='\x1B[46m';    On_ICya='\x1B[0;106m';
Whi='\x1B[0;37m';     BWhi='\x1B[1;37m';    UWhi='\x1B[4;37m';    IWhi='\x1B[0;97m';    BIWhi='\x1B[1;97m';   On_Whi='\x1B[47m';    On_IWhi='\x1B[0;107m';
#--------------------------------------------------------------------------------------------------------------------------------------------------
# Code
#--------------------------------------------------------------------------------------------------------------------------------------------------
ENV_TYPES=("generic" "clustering" "sharding")

function echoCurrentEnv(){
  echo -e ">>>>>> ${BBlu}CURRENT_ENV = ${BIGre}$(cat CURRENT_ENV)${RCol}"
}

function prune(){
    docker system prune -f 1> /dev/null
    docker volume prune -f 1> /dev/null
}

function stopAll(){
    docker-compose -f compose-$(cat CURRENT_ENV).yml down 2> /dev/null
}

function buildAll(){
    ./gradlew clean build buildDocker
}

function upAll(){
    echoCurrentEnv
    docker-compose -f compose-$(cat CURRENT_ENV).yml up -d
}

function openCtop(){
    ctop
}
function printUsage(){
  echo -e "${BBlu}./manager.sh env generic|clustering|sharding    ${Yel}#${BYel}change${Yel} env${RCol}"
  echo -e "${BBlu}./manager.sh reset                              ${Yel}#${BYel}stop all${Yel} containers and ${BYel}reset${Yel} env to generic${RCol}"
  echo -e "${BBlu}./manager.sh stop                               ${Yel}#${BYel}stop all${Yel} containers${RCol}"
  echo -e "${BBlu}./manager.sh rebuild-start                      ${Yel}#${BYel}rebuild and restart all${Yel} containers${RCol}"
  echo -e "${BBlu}./manager.sh rebuild                            ${Yel}#${BYel}rebuild all${Yel} containers${RCol}"
  echo -e "${BBlu}./manager.sh start                              ${Yel}#${BYel}start all${Yel} containers${RCol}"
  echo -e "${BBlu}./manager.sh status                             ${Yel}#${BYel}show all${Yel} containers status${RCol}"
  echo ''
  echo -e "${BBlu}./manager.sh endpoints                          ${Yel}#${BYel}show endpoints${RCol}"
  echo ''
  echoCurrentEnv
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
      stopAll
      echoCurrentEnv
  fi
}

function showEndpoints(){
  cat /Users/marcosarruda/prog-apps/workspaces/workspace-data/akka-examples/generic-local-node/src/main/kotlin/marcos/akka/controller/LocalNodeController.kt
}

if [ $# -gt 1 ] && [ "$1" != "" ] && [ "$1" = "env" ] && [ "$2" != "" ]; then
    changeEnv $2
elif [ "$1" = "reset" ]; then
    stopAll
    prune
    echo 'generic' > CURRENT_ENV
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
    openCtop
elif [ "$1" = "rebuild" ]; then
    stopAll
    buildAll
    prune
elif [ "$1" = "start" ]; then
    prune
    upAll
    openCtop
elif [ "$1" = "status" ]; then
    prune
    echoCurrentEnv
    dockerps
elif [ "$1" = "endpoints" ]; then
    prune
    showEndpoints
fi
