#!/bin/bash

#docker-compose -f dc-app.yml down --remove-orphans

gradle task clear

gradle task :well-boot-admin-server:docker

gradle task :well-zuul-server:docker

gradle task :well-provider:well-provider-user:docker

docker-compose -f dc-app.yml up -d