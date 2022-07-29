FROM sonarqube:7.1-arm64

ADD plugins/*.jar /opt/sonarqube/extensions/plugins/
COPY run.sh /opt/sonarqube/bin/run.sh
USER root
RUN chmod +x /opt/sonarqube/bin/run.sh && chown -R sonarqube:sonarqube /opt/sonarqube/bin
USER sonarqube