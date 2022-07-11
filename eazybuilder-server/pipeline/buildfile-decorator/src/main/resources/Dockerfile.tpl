FROM tomcat:latest

ENV APP_NAME="%s"

COPY %s.war /apache-tomcat-8.5.43/webapps/

RUN cd /apache-tomcat-8.5.43/webapps/ \
 && unzip ./$APP_NAME.war -d ./$APP_NAME \
 && rm -f ./$APP_NAME.war