#!/bin/bash

# 删除docker容器
docker-compose down

gradle task clear

gradle task docker

docker-compose up -d