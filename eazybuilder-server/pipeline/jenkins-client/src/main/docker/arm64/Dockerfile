FROM arm64v8/openjdk:8
RUN mkdir -p /opt/ci/resources
WORKDIR /opt/ci
VOLUME ["/opt/ci/resources"]
COPY config/  /opt/ci/config/
ADD jenkins-client*.jar /opt/ci/jenkins-client.jar

ENV JAVA_OPTS="-Xmx1g"
CMD java $JAVA_OPTS -jar jenkins-client.jar
