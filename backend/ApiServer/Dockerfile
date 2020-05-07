FROM openjdk:11-slim
RUN addgroup -S apiServer && adduser -S apiServer -G apiServer
USER apiServer:apiServer
COPY target/*.jar /home/apiServer/apiServer.jar
ENTRYPOINT ["java","-jar","/home/apiServer/apiServer.jar"]