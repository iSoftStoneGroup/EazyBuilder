FROM registry.eazybuilder-devops.cn/devops/jenkins-inbound-agent
RUN cat /proc/version
RUN echo “dash dash/sh boolean false” | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive dpkg-reconfigure dash

RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxxx -e=http://xxxxx

ADD .git-credentials /root/
RUN git config --global credential.helper store
COPY settings.xml /usr/share/maven/conf/settings.xml
#ADD openjdk-11.0.2_linux-x64_bin.tar.gz /usr/java/jdk/jdk-11.0.2
#RUN chmod 777 /usr/java/jdk/jdk-11.0.2
#ENV JAVA_HOME="/usr/java/jdk/jdk-11.0.2/"