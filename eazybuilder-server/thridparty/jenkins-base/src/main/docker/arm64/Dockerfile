FROM jenkins4eval/jenkins:2.240-alpine-arm64

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV JENKINS_OPTS="--argumentsRealm.passwd.admin=admin --argumentsRealm.roles.user=admin  --argumentsRealm.roles.admin=admin --prefix=/jenkins"
#use mirror of jenkins update center
ENV JENKINS_UC="https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates"
ENV JENKINS_UC_DOWNLOAD="https://mirrors.tuna.tsinghua.edu.cn/jenkins"
#install plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER root
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
RUN tar -xvf apache-maven-3.5.4-bin.tar.gz && rm -f apache-maven-3.5.4-bin.tar.gz &&  mv apache-maven-3.5.4 /usr/share/maven
COPY settings.xml /usr/share/maven/conf/settings.xml
COPY .git-credentials /root/.git-credentials
RUN wget https://mirrors.bfsu.edu.cn/apache//ant/binaries/apache-ant-1.10.8-bin.tar.gz
RUN tar -xvf apache-ant-1.10.8-bin.tar.gz && rm -f apache-ant-1.10.8-bin.tar.gz && mv apache-ant-1.10.8 /usr/share/ant

RUN apk add --no-cache ansible
RUN apk add --no-cache sshpass
RUN apk add --update --no-cache nodejs-current nodejs-npm

ENV PATH="/usr/share/ant/bin:/usr/share/maven/bin:${PATH}"

ENV ANSIBLE_HOST_KEY_CHECKING="False"
COPY ansible.cfg /etc/ansible/ansible.cfg
COPY push-war.yml /opt/push-war.yml

RUN npm config set registry https://registry.npm.taobao.org && npm install -g nrm && npm install -g npm-cli-login && nrm add iaf http://10.129.130.198:4873/ && npm config set cache "/usr/share/maven-repo/npm-cache"

RUN mkdir -p /opt/ci-tool/lib
RUN git config --global credential.helper store
COPY ci-tool/lib/*.jar  /opt/ci-tool/lib/
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy
ENV MAVEN_OPTS="-Xmx2g"
RUN apk add --no-cache subversion && apk upgrade sqlite