FROM openjdk:11-slim
RUN groupadd orchestrator && useradd -m -g orchestrator orchestrator
USER orchestrator:orchestrator
RUN mkdir /home/orchestrator/logs
COPY target/*.jar /home/orchestrator/orchestrator.jar

ENV SERVER_PORT="8082"
ENV LOGGING_LEVEL="WARN"
ENV CORE_POOL_SIZE="4"
ENV MAX_POOL_SIZE="10"
ENV ORCHESTRATOR_LISTEN_PORT="8045"

# TODO: MOVE TO DOCKER SECRET
ENV DB_URL="jdbc:mysql://127.0.0.1:3307/poker?autoReconnect=true"
ENV DB_USERNAME="root"
ENV DB_PASSWORD="321654987"

ENTRYPOINT ["java","-jar",\
            "-Dserver.port=${SERVER_PORT}",\
            "-Dlogging.level.ar.com.tandilweb=${LOGGING_LEVEL}",\
            "-Dact.orchestrator.corePoolSize=${CORE_POOL_SIZE}",\
            "-Dact.orchestrator.maxPoolSize=${MAX_POOL_SIZE}",\
            "-Dact.orchestrator.listenPort=${ORCHESTRATOR_LISTEN_PORT}",\
            "-Dspring.datasource.url=${DB_URL}",\
            "-Dspring.datasource.username=${DB_USERNAME}",\
            "-Dspring.datasource.password=${DB_PASSWORD}",\
            "/home/orchestrator/orchestrator.jar"]