#!/bin/bash
echo Building docker image with tag alexander171294/web-poker-room:$1
rm -rf target
mvn clean package
docker build -t alexander171294/web-poker-room .
docker push alexander171294/web-poker-room
docker tag alexander171294/web-poker-room alexander171294/web-poker-room:$1
docker push alexander171294/web-poker-room:$1
docker rm room-poker
docker run --name room-poker --env ORCHESTRATOR_IP="192.168.1.189" -p 8081:8081 -it alexander171294/web-poker-room