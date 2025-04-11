FROM ibm-semeru-runtimes:open-17-jdk-focal
WORKDIR /app
COPY ./build/libs/notificationprocessor-0.0.1-SNAPSHOT.jar notification-processor.jar

ENV DB_URL="r2dbc:mysql://localhost:3306/assessment?sslMode=DISABLED"
ENV DB_USERNAME="root"
ENV DB_PASSWORD="sql1233"
ENV KAFKA_BROKER_URL="localhost:8090"

ENV USER_META_DATA_ENDPOINT="https://mockserver.free.beeceptor.com/api/users/"
ENV API_TIMEOUT="10"
ENV RETRY_ATTEMPT="3"
ENV RETRY_DELAY="5"

CMD ["java","-jar","notification-processor.jar"]
EXPOSE 8080