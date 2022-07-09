FROM arm64v8/openjdk:8
RUN mkdir -p /opt/ci/resources
WORKDIR /opt/ci
VOLUME ["/opt/ci/resources"]
COPY config/  /opt/ci/config/
ADD eazybuilder*.jar /opt/ci/eazybuilder.jar

ENV JAVA_OPTS="-Xmx1g"
CMD java $JAVA_OPTS -jar eazybuilder.jar
