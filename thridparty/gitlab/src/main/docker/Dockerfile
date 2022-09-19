FROM gitlab/gitlab-ce:latest

ADD hooks/* /opt/gitlab/embedded/service/gitlab-shell/hooks
ADD gitlab.rb /etc/gitlab/
RUN chmod -R 777 /opt/gitlab/embedded/service/gitlab-shell/hooks/
RUN chmod -R 777 /etc/gitlab/

RUN mkdir /usr/local/java
# jdk可以前往 https://www.oracle.com/java/technologies/downloads/  下载
# 和copy一样，复制文件到指定目录，但是copy不能解压，add自动解压
ADD jdk-8u181-linux-x64.tar.gz /usr/local/java
# 重命名(不知道文件名可以现在宿主机解压后看一下)
RUN ln -s /usr/local/java/jdk1.8.0_181 /usr/local/java/jdk
# 设置环境变量
ENV JAVA_HOME /usr/local/java/jdk
ENV JRE_HOME ${JAVA_HOME}/jre
ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib
ENV PATH ${JAVA_HOME}/bin:$PATH