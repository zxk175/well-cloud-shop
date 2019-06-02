#!/bin/bash

#docker-compose -f dc-base.yml down --remove-orphans

gradle task clear

gradle task :well-eureka-server:docker

docker-compose -f dc-base.yml up -d