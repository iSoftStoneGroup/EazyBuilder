FROM nginx:1.22

RUN apt update && apt-get install unzip
ADD eazybuilder-web*.jar /tmp/eazybuilder-web.jar
RUN mkdir -p /usr/share/nginx/html && unzip /tmp/eazybuilder-web.jar -d /usr/share/nginx/html/