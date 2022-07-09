FROM openjdk:8-jre-slim
RUN mkdir -p /opt/ci/resources
WORKDIR /opt/ci
VOLUME ["/opt/ci/resources"]
RUN apk add --no-cache git
RUN apk add --update ttf-dejavu fontconfig
COPY config/  /opt/ci/config/
ADD eazybuilder*.jar /opt/ci/eazybuilder.jar

ENV JAVA_OPTS="-Xmx1g"
CMD java $JAVA_OPTS -jar eazybuilder.jar
