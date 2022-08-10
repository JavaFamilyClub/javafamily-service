# Docker image for pmrs-xa
# VERSION 0.0.1
# Author: Jack Li
# base image using java8
FROM openjdk:8-jre-alpine
#FROM java:8
# author
#MAINTAINER mlogcn <lishuai@mlogcn.com>

LABEL maintainer="mlogcn lishuai@mlogcn.com"
LABEL author="Jack Li"

# temp folder. link locale:/var/lib/docker to container:/tmp
#VOLUME /tmp

# add jar to container and renaming to app.jar
ARG JAR_FILE
ADD ./${JAR_FILE} app.jar

ENV JAVA_OPTS="\
   "

ENV ACTIVE_PROFILE prod

# optonal env
ENV CONTEXT_PATH "/xxl-job-admin"
ENV PORT 8080
ENV DS_INIT_MODE never

# required env
ENV MYSQL_URL ""
ENV MYSQL_USERNAME ""
ENV MYSQL_PWD ""

# run command
ENTRYPOINT java ${JAVA_OPTS} \
   -Djava.security.egd=file:/dev/./urandom \
   -jar /app.jar --spring.profiles.active=${ACTIVE_PROFILE}
