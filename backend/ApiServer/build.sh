rm -rf target
mvn clean package
docker build -t wearelantian.com/web-poker/api-server .
docker push wearlantian.com/web-poker/api-server