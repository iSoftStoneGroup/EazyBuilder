FROM openjdk:8
RUN mkdir -p /opt/ci/resources
WORKDIR /opt/ci
COPY config/  /opt/ci/config/
ADD jenkins-client*.jar /opt/ci/jenkins-client.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar"]
CMD ["jenkins-client.jar"]