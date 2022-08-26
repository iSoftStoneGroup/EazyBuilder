FROM openjdk:18-ea-jdk-alpine
RUN mkdir -p /opt/ci/resources
WORKDIR /opt/ci
VOLUME ["/opt/ci/resources"]
RUN apk add --no-cache git
RUN apk add --update ttf-dejavu fontconfig
COPY config/  /opt/ci/config/
ADD jenkins-client*.jar /opt/ci/jenkins-client.jar


ENV JAVA_OPTS="-Xmx1g "
CMD java $JAVA_OPTS -jar jenkins-client.jar
