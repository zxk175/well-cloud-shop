#!/bin/bash

#docker-compose down --remove-orphans

gradle task clear

gradle task docker

docker-compose -f dc-app.yml up -d