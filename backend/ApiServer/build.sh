rm -rf target
mvn clean package
docker build -t docker.tandilserver.com/web-poker/api-server .
docker push docker.tandilserver.com/web-poker/api-server
docker rm apiServer
# echo -n "smtp.gmail.com"|docker secret create mail.smtpserver -
docker run --name apiServer --env DB_URL="jdbc:mysql://192.168.1.189:3307/poker?autoReconnect=true&useSSL=false" -p 8083:8083 -it docker.tandilserver.com/web-poker/api-server:latest
