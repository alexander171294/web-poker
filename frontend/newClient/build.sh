#!/bin/bash
echo Building docker image with tag alexander171294/web-poker-client:$1
ng build --prod
docker build -t alexander171294/web-poker-client .
docker push alexander171294/web-poker-client
docker tag alexander171294/web-poker-client alexander171294/web-poker-client:$1
docker push alexander171294/web-poker-client:$1
docker rm poker-client
docker run --name poker-client -p 80:80 -it alexander171294/web-poker-client
