#!/bin/bash

# 验证.env是否有效
#docker-compose -f dc-base.yml config
# 删除容器
#docker-compose -f dc-base.yml down --remove-orphans

#gradle task clear

gradle :well-eureka-server:build -x test

docker-compose -f dc-base.yml up -d --build