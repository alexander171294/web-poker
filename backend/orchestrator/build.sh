rm -rf target
mvn clean package
docker build -t wearelantian.com/web-poker/orchestrator .
docker push wearlantian.com/web-poker/orchestrator