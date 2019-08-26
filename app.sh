#!/bin/bash

# 验证.env是否有效
#docker-compose -f app.yml com.zxk175.well.config
# 删除容器
#docker-compose -f app.yml down --remove-orphans

#gradle task clear

gradle :well-monitor:well-zipkin:build -x test

gradle :well-base:well-config:build -x test

gradle :well-base:well-gateway:build -x test

gradle :well-provider:well-provider-user:build -x test

docker-compose -f app.yml up -d --build
