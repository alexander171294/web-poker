ng build --prod
docker build -t tandilserver.com/web-poker/client .
docker push tandilserver.com/web-poker/client
docker rm poker-client
docker run --name poker-client -p 80:80 -it tandilserver.com/web-poker/client
