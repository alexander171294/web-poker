FROM openjdk:11-slim
RUN addgroup -S orchestrator && adduser -S orchestrator -G orchestrator
USER orchestrator:orchestrator
COPY target/*.jar /home/orchestrator/orchestrator.jar
ENTRYPOINT ["java","-jar","/home/orchestrator/orchestrator.jar"]