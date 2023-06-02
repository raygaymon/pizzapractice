#
# First stage
#

FROM maven:3.8.3-openjdk-17 AS build

COPY src /tryingworkshop16/src
COPY pom.xml /tryingworkshop16

ARG REDISHOST
ARG REDISPORT
ARG REDISUSER
ARG REDISPASSWORD

RUN mvn -f /tryingworkshop16/pom.xml clean package

#
# second stage
#

FROM openjdk:17-oracle

ARG REDISHOST
ARG REDISPORT
ARG REDISUSER
ARG REDISPASSWORD

COPY --from=build /tryingworkshop16/target/tryingworkshop16-0.0.1-SNAPSHOT.jar /tryingworkshop16.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/tryingworkshop16.jar"]