FROM sonarqube

ENV SONARQUBE_JDBC_USERNAME=sonar
ENV SONARQUBE_JDBC_PASSWORD=sonar
ENV SONARQUBE_JDBC_URL=jdbc:mysql://ci-mysql:3306/sonar?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true
ENV JAVA_OPTS=-server

ADD plugins/*.jar /opt/sonarqube/extensions/plugins/
COPY run.sh /opt/sonarqube/bin/run.sh
RUN chmod +x /opt/sonarqube/bin/run.sh && chown -R sonarqube:sonarqube /opt/sonarqube/bin
