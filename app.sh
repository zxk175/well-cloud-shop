#!/bin/bash

# 验证.env是否有效
#docker-compose -f app.yml config
# 删除容器
#docker-compose -f app.yml down --remove-orphans

#gradle task clear

#gradle :well-base-server:well-zuul:build -x test

gradle :well-base-server:well-gateway:build -x test

gradle :well-base-server:well-boot-admin:build -x test

gradle :well-provider:well-provider-user:build -x test

docker-compose -f app.yml up -d --build
