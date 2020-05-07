FROM openjdk:11-slim
RUN groupadd roompoker && useradd -m -g roompoker roompoker
USER roompoker:roompoker
RUN mkdir /home/roompoker/dataSave
COPY target/*.jar /home/roompoker/room.jar

ENV SERVER_PORT="8081"
ENV CONFIG_FILE="/home/room/dataSave"
ENV LOGGING_LEVEL="WARN"
ENV ROOM_NAME="Probando Room"
ENV ACCESS_PASSWORD="389874125"
ENV SECURITY_TOKEN=""
ENV MAX_PLAYERS="10"
ENV DESCRIPTION="Testing server, description"
ENV MIN_CHIPS_FOR_ACCESS="250"
ENV SERVER_PUBLIC_IP="127.0.0.1"

# TODO: mover a secret
ENV RECOVERY_EMAIL="alexander171294@gmail.com"
ENV ORCHESTRATOR_LISTEN_PORT="8045"
ENV ORCHESTRATOR_IP="127.0.0.1"

ENTRYPOINT ["java","-jar",\
            "-Dserver.port=${SERVER_PORT}",\
            "-Dact.room.cfgFileSave=${CONFIG_FILE}",\
            "-Dlogging.level.ar.com.tandilweb=${LOGGING_LEVEL}",\
            "-Dact.room.RoomAuth.name=${ROOM_NAME}",\
            "-Dact.room.RoomAuth.accessPassword=${ACCESS_PASSWORD}",\
            "-Dact.room.RoomAuth.securityToken=${SECURITY_TOKEN}",\
            "-Dact.room.RoomAuth.maxPlayers=${MAX_PLAYERS}",\
            "-Dact.room.RoomAuth.description=${DESCRIPTION}",\
            "-Dact.room.RoomAuth.minCoinsForAccess=${MIN_CHIPS_FOR_ACCESS}",\
            "-Dact.room.RoomAuth.recoveryEmail=${RECOVERY_EMAIL}",\
            "-Dact.room.RoomAuth.serverPublicIP=${SERVER_PUBLIC_IP}",\
            "-Dact.room.orchestrator.remoteListenerPort=${ORCHESTRATOR_LISTEN_PORT}",\
            "-Dact.room.orchestrator.remoteAddr=${ORCHESTRATOR_IP}",\
            "/home/room/room.jar"]