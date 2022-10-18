FROM registry.eazybuilder-devops.cn/library/eazybuilder-nginx:1.12

ADD ci-console*.jar /tmp/ci-console.jar
RUN mkdir -p /usr/share/nginx/html && unzip -d /usr/share/nginx/html /tmp/ci-console.jar