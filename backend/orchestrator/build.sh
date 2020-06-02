#!/bin/bash
echo Building docker image with tag alexander171294/web-poker-orchestrator:$1
rm -rf target
mvn clean package
docker build -t alexander171294/web-poker-orchestrator .
docker push alexander171294/web-poker-orchestrator
docker tag alexander171294/web-poker-orchestrator alexander171294/web-poker-orchestrator:$1
docker push alexander171294/web-poker-orchestrator:$1
docker rm orchestrator
docker run --name orchestrator --env DB_URL="jdbc:mysql://192.168.1.189:3307/poker?autoReconnect=true&useSSL=false" -p 8082:8082 -p 8045:8045 -it alexander171294/web-poker-orchestrator