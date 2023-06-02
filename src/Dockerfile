#
# First stage
#

FROM maven:3.8.3-openjdk-17 AS build

COPY src /home/app/src
COPY pom.xml /home/app

ARG REDISHOST
ARG REDISPORT
ARG REDISUSER
#this must match applicatio.properties knn
ARG REDISPASSWORD

RUN mvn -f /home/app/pom.xml clean package

#
# second stage
#

FROM openjdk:17-oracle

ARG REDISHOST
ARG REDISPORT
ARG REDISUSER
ARG REDISPASSWORD

#use mvn clean package to find the jar file needed to add here
COPY --from=build /home/app/target/tryingworkshop16-0.0.1-SNAPSHOT.jar /usr/local/lib/tryingworkshop16.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/tryingworkshop16.jar"]