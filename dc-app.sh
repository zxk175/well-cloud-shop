#!/bin/bash

# 验证.env是否有效
#docker-compose -f dc-app.yml config
# 删除容器
#docker-compose -f dc-app.yml down --remove-orphans

#gradle task clear

gradle :well-zuul-server:build -x test

gradle :well-boot-admin-server:build -x test

gradle :well-provider:well-provider-user:build -x test

docker-compose -f dc-app.yml up -d --build