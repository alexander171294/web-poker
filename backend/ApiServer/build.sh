rm -rf target
mvn clean package
docker build -t tandilserver.com/web-poker/api-server .
docker push tandilserver.com/web-poker/api-server
docker rm apiServer
docker run --name apiServer --env DB_URL="jdbc:mysql://192.168.1.189:3307/poker?autoReconnect=true&useSSL=false" -p 8083:8083 -it tandilserver.com/web-poker/api-server:latest