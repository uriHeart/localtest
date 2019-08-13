FROM adoptopenjdk/openjdk11:latest

ARG APP_PROFILES
ARG XMS
ARG XMX

ENV APP_ROOT_NAME argo
ENV APP_NAME argo-collector

ENV APP_ROOT_DIR /argo/service/${APP_ROOT_NAME}
ENV APP_DIR ${APP_ROOT_DIR}/${APP_NAME}

ENV SPRING_PROFILES_ACTIVE $APP_PROFILES
ENV JAVA_XMS $XMS
ENV JAVA_XMX $XMX

#setup
RUN java -version
RUN $JAVA_HOME/bin/javac -version
RUN gradle --version

#move file
COPY . ${APP_ROOT_DIR}

#build app
WORKDIR ${APP_DIR}
RUN gradle clean build

EXPOSE 8080

ENTRYPOINT java \
-server \
-Xms${JAVA_XMS} -Xmx${JAVA_XMX} \
-Dsun.net.inetaddr.ttl=0 \
-Djava.security.egd=file:/dev/./urandom \
-Dfile.encoding=UTF-8 \
-Duser.timezone=GMT+09:00 \
-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
-jar build/libs/app.jar