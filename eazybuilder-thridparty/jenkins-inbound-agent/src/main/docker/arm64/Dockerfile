FROM jenkins/inbound-agent:4.7-1

USER root
RUN sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list
RUN apt-get update 
RUN apt-get install -y wget ansible sshpass ant 

#install maven
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
RUN tar -xvf apache-maven-3.5.4-bin.tar.gz && rm -f apache-maven-3.5.4-bin.tar.gz &&  mv apache-maven-3.5.4 /usr/share/maven
COPY settings.xml /usr/share/maven/conf/settings.xml
ENV PATH="/usr/share/maven/bin:${PATH}"

#install nodejs
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && apt-get install -y nodejs && npm config set registry https://registry.npm.taobao.org && \ 
  npm install -g nrm && npm install -g npm-cli-login && npm install -g yarn && nrm add iaf http://10.129.130.198:4873/ && npm config set cache "/usr/share/maven-repo/npm-cache"

#instal gradle
RUN cd /opt && wget https://downloads.gradle-dn.com/distributions/gradle-6.8.3-bin.zip && unzip gradle-6.8.3-bin.zip -d /opt/gradle && rm -f /opt/gradle-6.8.3-bin.zip
ENV PATH="/opt/gradle/gradle-6.8.3/bin:${PATH}"

#install sonarscanner
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.0.2311.zip && unzip sonar-scanner-cli-4.6.0.2311.zip -d /usr/share/sonar-scanner
ENV PATH="/usr/share/sonar-scanner/sonar-scanner-4.6.0.2311/bin:${PATH}"

#install subversion
RUN apt-get install -y subversion
#install zh-CN lang pack
RUN apt-get install -y locales && localedef -c -f UTF-8 -i zh_CN zh_CN.utf8

#copy ci files
COPY ansible.cfg /etc/ansible/ansible.cfg
COPY push-war.yml /opt/push-war.yml
ENV ANSIBLE_HOST_KEY_CHECKING="False"

#注意，这些jar是eazybuilder-server\pipeline中如下工程编译之后的jar
#buildfile-decorator,legacy-ant-adapter,report-collector,sql-scanner
#也可以扩展自己的增强插件
RUN mkdir -p /opt/ci-tool/lib
COPY ci-tool/lib/*.jar  /opt/ci-tool/lib/

ENV LANG="zh_CN.utf-8"
ENV LC_ALL="zh_CN.utf-8"

RUN cat /proc/version
RUN echo “dash dash/sh boolean false” | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive dpkg-reconfigure dash

#注意，如下是安装华为obs工具，与集成jdk11,需要提前将文件，放到镜像所在的目录，如果不需要，可以注释掉
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxx -e=http://xxxxx
ADD .git-credentials /root/
RUN git config --global credential.helper store
COPY settings.xml /usr/share/maven/conf/settings.xml
ADD jdk-11.0.2 /usr/java/jdk/jdk-11.0.2
RUN chmod 777 /usr/java/jdk/jdk-11.0.2
