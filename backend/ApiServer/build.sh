#!/bin/bash
echo Building docker image with tag alexander171294/web-poker-api:$1
rm -rf target
mvn clean package
docker build -t alexander171294/web-poker-api .
docker push alexander171294/web-poker-api
docker tag alexander171294/web-poker-api alexander171294/web-poker-api:$1
docker push alexander171294/web-poker-api:$1
docker rm apiServer
# echo -n "smtp.gmail.com"|docker secret create mail.smtpserver -
docker run --name apiServer --env DB_URL="jdbc:mysql://192.168.1.189:3307/poker?autoReconnect=true&useSSL=false" -p 8083:8083 -it alexander171294/web-poker-api:latest
