FROM openjdk:11-slim
RUN addgroup -S apiServer && adduser -S apiServer -G apiServer
USER apiServer:apiServer
COPY target/*.jar /home/apiServer/apiServer.jar

ENV SIGNUP_CHIPS=2500
ENV MAX_BAD_LOGINS=5

ENV SERVER_PORT="8083"
ENV LOGGING_LEVEL="WARN"
ENV FLYWAY="true"
# TODO: change this to docker secret:
ENV SMTP="smtp.gmail.com"
ENV EMAIL="web.poker@tandilserver.com"
ENV EMAIL_PASSWORD=""
ENV DB_URL="jdbc:mysql://127.0.0.1:3307/poker?autoReconnect=true"
ENV DB_USERNAME="root"
ENV DB_PASSWORD="321654987"


ENTRYPOINT ["java","-jar","/home/apiServer/apiServer.jar",
            "-Dar.com.tandilweb.ApiServer.users.initialChips=${SIGNUP_CHIPS}",
            "-Dar.com.tandilweb.ApiServer.users.maxBadLogins=${MAX_BAD_LOGINS}",
            "-Dspring.flyway.enabled=${FLYWAY}",
            "-Dserver.port=${SERVER_PORT}",
            "-Dlogging.level.ar.com.tandilweb=${LOGGING_LEVEL}",
            "-Dmail.smtpserver=${SMTP}",
            "-Dmail.email=${EMAIL}",
            "-Dmail.password=${EMAIL_PASSWORD}",
            "-Dspring.datasource.url=${DB_URL}",
            "-Dspring.datasource.username=${DB_USERNAME}",
            "-Dspring.datasource.password=${DB_PASSWORD}"]