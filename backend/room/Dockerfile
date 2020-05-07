FROM openjdk:11-slim
RUN addgroup -S room && adduser -S room -G room
USER room:room
COPY target/*.jar /home/room/room.jar
ENTRYPOINT ["java","-jar","/home/room/room.jar"]