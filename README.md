# notification-processor

## 📦 Prerequisites

- Docker
- Java 17+
- Gradle above 8.0
- MySQL 8.0
- Internet access (for pulling Docker images and calling external APIs)


## STEP 1
Create table in MySQL
Command
create table user
(
    ID                         bigint                   not null
        primary key,
    email                      varchar(50)              not null,
    full_name                  varchar(100)             not null,
    address                    varchar(100) default '-' null,
    country                    varchar(100) default '-' null,
    notification_event_context json                     not null,
    doe                        datetime                 not null,
    constraint email
        unique (email)
);

## STEP 2

- Start the kafka service using the docker-compose file

## STEP 3

- Set all the required env in docker file like
  DB_URL (ensure the db url has r2dbc prefix)
  DB_USERNAME
  DB_PASSWORD
  KAFKA_BROKER_URL
  
  USER_META_DATA_ENDPOINT (For mocking external api :: use -> https://mockserver.free.beeceptor.com/api/users/930504453)
  API_TIMEOUT
  RETRY_ATTEMPT
  RETRY_DELAY


## STEP 4
- Build the docker images
Command (docker build -t image_tag Docker_file_path)

## STEP 5
- After successful building images, run the container service
Command (docker run -p host_port:container_port image_tag)


## STEP 6
- Produce message through the kafka-ui to topic notification-events on comsumer group notification-consumer

## Exposed API
GET /users
GET /users/{user_id}

