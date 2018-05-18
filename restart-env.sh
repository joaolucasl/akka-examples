#!/usr/bin/env bash

dirs=$(ls -1 -d */)
for i in $dirs
do
  cd $i
  pwd
  #./gradlew clean build -x test
  ./gradlew clean build
  cd ..
done
wait
