FROM registry.iss-devops.cn/devops/jenkins-inbound-agent
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
RUN /obsutil/obsutil config -i=xxx -k=xxx -e=http://xxxx

#如下是配置git免登录信息，如果不需要，可以注释掉
ADD .git-credentials /root/
RUN git config --global credential.helper store

#如下是配置全局的maven，如果不需要，可以注释掉
COPY settings.xml /usr/share/maven/conf/settings.xml

#如下是配置jdk11的目录，需要提前将安装包放入打包目录中，如果不需要，可以注释掉
#ADD openjdk-11.0.2_linux-x64_bin.tar.gz /usr/java/jdk/jdk-11.0.2
#RUN chmod 777 /usr/java/jdk/jdk-11.0.2
#ENV JAVA_HOME="/usr/java/jdk/jdk-11.0.2/"