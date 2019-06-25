#!/bin/bash

# 验证.env是否有效
#docker-compose -f base.yml config
# 删除容器
#docker-compose -f base.yml down --remove-orphans

#gradle task clear

gradle :well-base-server:well-eureka:build -x test

docker-compose -f base.yml up -d --build