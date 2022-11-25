FROM openjdk:8
# 时区调整
ENV  TIME_ZONE Asia/Shanghai
ENV APP_HOME=/opt/eazybuilder-notify
ENV APP_NAME=eazybuilder-notify
ENV JAVA_OPTS="-Xmx1g"


RUN echo "${TIME_ZONE}" > /etc/timezone && \
      ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
RUN mkdir -p /opt/eazybuilder-notify

WORKDIR ${APP_HOME}

ADD startApp.sh ${APP_HOME}/startApp.sh
ADD ${APP_NAME}*.jar ${APP_HOME}/${APP_NAME}.jar
ADD config/*  ${APP_HOME}/config/


CMD sh ${APP_HOME}/startApp.sh