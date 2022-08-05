
# jenkins inbound镜像

- 此工程，是基于官方jenkins/inbound-agent镜像，又安装了一些必须的插件

- 如果不使用k8s jenkins agent，就无需配置此镜像
- obs插件安装

如下命令，是安装华为obs插件，obsutil由于文件过大，并没有放入此目录，需要用户自行去华为官网下载，如果不需要，可以删掉这些obs安装的脚本
```shell
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxxx -e=http://xxxxx
```
- 多jdk支持

平台支持自定义构建参数，可以指定jdk，如果用户需要支持多jdk，可以直接将jdk打包进此镜像中，可参照如下命令，由于文件过大，jdk11的目录，并为上传上来，需要用户自行下载
```shell
ADD openjdk-11.0.2_linux-x64_bin.tar.gz /usr/java/jdk/jdk-11.0.2
RUN chmod 777 /usr/java/jdk/jdk-11.0.2
ENV JAVA_HOME="/usr/java/jdk/jdk-11.0.2/"
```
