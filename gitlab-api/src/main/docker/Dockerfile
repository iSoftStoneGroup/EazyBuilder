FROM adoptopenjdk/openjdk8:jre8u292-b10-alpine

# apk(类似yum)源设置，这里使用阿里云的
RUN echo -e "http://mirrors.aliyun.com/alpine/v3.4/main\n\
    http://mirrors.aliyun.com/alpine/v3.4/community" > /etc/apk/repositories

# 时区调整
ENV  TIME_ZONE Asia/Shanghai

ENV GITLAB_HOME=/opt/gitlab
ENV APP_NAME=gitlab-api


ADD startApp.sh ${GITLAB_HOME}/startApp.sh
ENV JAVA_OPTS="-Xms1024m -Xmx1024m -XX:+ExitOnOutOfMemoryError "
#ENV JAVA_OPTS="-Xms1024m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/dm/logs/heapdump.hprof"

VOLUME ["/opt/dm/logs"]

EXPOSE 8011

WORKDIR ${GITLAB_HOME}
#CMD sh ${TCM_HOME}/startApp.sh > /dev/null 2>&1
ADD ${APP_NAME}*.jar ${GITLAB_HOME}/${APP_NAME}.jar
ADD config/*  ${GITLAB_HOME}/config/

CMD sh ${GITLAB_HOME}/startApp.sh
