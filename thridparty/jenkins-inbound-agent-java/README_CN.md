
# jenkins inbound agent java镜像

- 此工程，是基于同级目录中jenkins-inbound-agent镜像，又安装了一些必须的插件
在jenkins-inbound-agent中制作了镜像，并且推送到registry.eazybuilder-devops.cn/devops/jenkins-inbound-agent后，在本工程中又依赖于此镜像去安装插件

如下：
FROM registry.eazybuilder-devops.cn/devops/jenkins-inbound-agent

这样做，是为了最大的保证基础镜像的稳定，本目录中的镜像，可以经常变动，但是jenkins-inbound-agent目录中的镜像几乎不需要改变

- 如果不使用k8s jenkins agent，就无需配置此镜像
- 适配java语言，前端开发语言
- 此镜像是基于jenkins-base的制作方式来制作的，目录文件描述，可以参考jenkins-base
- obs插件安装

如下命令，是安装华为obs插件，obsutil由于文件过大，并没有放入此目录，需要用户自行去华为官网下载，如果不需要，可以删掉这些obs安装的脚本
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxxx -e=http://xxxxx

- 多jdk支持

平台支持自定义构建参数，可以指定jdk，如果用户需要支持多jdk，可以直接将jdk打包进此镜像中，可参照如下命令，由于文件过大，jdk11的目录，并为上传上来，需要用户自行下载
ADD openjdk-11.0.2_linux-x64_bin.tar.gz /usr/java/jdk/jdk-11.0.2
RUN chmod 777 /usr/java/jdk/jdk-11.0.2
ENV JAVA_HOME="/usr/java/jdk/jdk-11.0.2/"