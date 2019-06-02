#!/bin/bash

docker-compose down --remove-orphans

gradle task clear

gradle task :well-eureka-server:docker

docker-compose -f dc-eureka.yml up -d