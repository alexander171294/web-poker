rm -rf target
mvn clean package
docker build -t wearelantian.com/web-poker/room-poker .
docker push wearlantian.com/web-poker/room-poker