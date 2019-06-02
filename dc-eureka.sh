#!/bin/bash

docker-compose down --remove-orphans

gradle task clear

cd ./well-eureka-server/

gradle task docker

cd ../

docker-compose -f dc-eureka.yml up -d