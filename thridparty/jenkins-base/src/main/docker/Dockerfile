FROM jenkins/jenkins:2.346.1-lts-centos7

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV JENKINS_OPTS="--argumentsRealm.passwd.admin=admin --argumentsRealm.roles.user=admin  --argumentsRealm.roles.admin=admin --prefix=/jenkins"

#install plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER root
RUN sed -e 's|^mirrorlist=|#mirrorlist=|g' \
         -e 's|^#baseurl=http://mirror.centos.org|baseurl=https://mirrors.tuna.tsinghua.edu.cn|g' \
         -i.bak \
         /etc/yum.repos.d/CentOS-*.repo && yum makecache
RUN yum install -y epel-release && yum install -y wget ansible sshpass


#install maven
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz --no-check-certificate
RUN tar -xvf apache-maven-3.5.4-bin.tar.gz && rm -f apache-maven-3.5.4-bin.tar.gz &&  mv apache-maven-3.5.4 /usr/share/maven
COPY settings.xml /usr/share/maven/conf/settings.xml
ENV PATH="/usr/share/maven/bin:${PATH}"

#install gradle
RUN cd /opt && wget https://downloads.gradle-dn.com/distributions/gradle-6.8.3-bin.zip --no-check-certificate && unzip gradle-6.8.3-bin.zip -d /opt/gradle && rm -f /opt/gradle-6.8.3-bin.zip
ENV PATH="/opt/gradle/gradle-6.8.3/bin:${PATH}"


#install sonar-scanner
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.0.2311.zip --no-check-certificate && unzip sonar-scanner-cli-4.6.0.2311.zip -d /usr/share/sonar-scanner
ENV PATH="/usr/share/sonar-scanner/sonar-scanner-4.6.0.2311/bin:${PATH}"

#install apache ant
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/ant/binaries/apache-ant-1.10.12-bin.tar.gz --no-check-certificate && \
    tar -xvf apache-ant-1.10.12-bin.tar.gz && rm -f apache-ant-1.10.12-bin.tar.gz &&  mv apache-ant-1.10.12 /usr/share/ant
ENV PATH="/usr/share/ant/bin:$PATH"

#install nodejs
RUN curl -sL https://rpm.nodesource.com/setup_14.x | bash - && yum install -y nodejs && npm config set registry https://registry.npm.taobao.org && npm install -g nrm && npm install -g npm-cli-login && npm install -g yarn && npm config set cache "/usr/share/maven-repo/npm-cache"


#install cloud plugin
RUN /usr/local/bin/install-plugins.sh kubernetes docker-plugin

RUN yum install epel-release -y && yum install dnf -y

#RUN yum install glibc-all-langpacks -y

#install subversion
RUN yum install -y subversion

#install zh-CN lang pack
#RUN yum install -y langpacks-zh_CN && echo 'LANG="zh_CN.UTF-8"' > /etc/locale.conf

#ansible config files
ENV ANSIBLE_HOST_KEY_CHECKING="False"
COPY ansible.cfg /etc/ansible/ansible.cfg
COPY push-war.yml /opt/push-war.yml

RUN mkdir -p /opt/ci-tool/lib
COPY ci-tool/lib/*.jar  /opt/ci-tool/lib/
COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/executors.groovy
ENV MAVEN_OPTS="-Xmx2g"
ENV LANG="zh_CN.UTF-8"
ENV LC_ALL="zh_CN.UTF-8"

#安装离线插件 http request
COPY  http_request /var/jenkins_home/plugins
COPY  http_request.jpi /var/jenkins_home/plugins
