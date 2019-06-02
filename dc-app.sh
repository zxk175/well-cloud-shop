#!/bin/bash

# 验证.env是否有效
#docker-compose -f dc-app.yml config
# 删除容器
#docker-compose -f dc-app.yml down --remove-orphans

gradle task clear

gradle task :well-boot-admin-server:docker

gradle task :well-zuul-server:docker

gradle task :well-provider:well-provider-user:docker

docker-compose -f dc-app.yml up -d