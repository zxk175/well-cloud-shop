#!/bin/bash

gradle task clear

gradle task docker

docker-compose up -d