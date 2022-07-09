FROM nginx:1.12

ADD eazybuilder-web*.jar /tmp/eazybuilder-web.jar
RUN mkdir -p /usr/share/nginx/html && unzip -d /usr/share/nginx/html /tmp/eazybuilder-web.jar