rm -rf target
mvn clean package
docker build -t tandilserver.com/web-poker/room-poker .
docker push tandilserver.com/web-poker/room-poker
docker rm room-poker
docker run --name room-poker --env ORCHESTRATOR_IP="192.168.1.189" -p 8081:8081 -it tandilserver.com/web-poker/room-poker