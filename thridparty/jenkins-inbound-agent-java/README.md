# [中文说明](./README_CN.md)
# Jenkins inbound agent java image
- This project is based on the jenkins inbound agent image in the peer directory, and some necessary plug-ins are installed
After creating an image in the jenkins inbound agent and pushing it to registry.eazybuilder-devops.cn/devops/jenkins inbound agent, the project relies on this image to install plug-ins
As follows:
```shell
FROM registry.eazybuilder-devops.cn/devops/jenkins-inbound-agent
```
This is to ensure the stability of the basic image to the greatest extent. The images in this directory can change frequently, but the images in the jenkins inbound agent directory hardly need to change
-If you do not use the k8s jenkins agent, you do not need to configure this image
-Adapt to java language and front-end development language
-This image is created based on the production method of jenkins base. For the description of the directory file, refer to jenkins base
-OBS plug-in installation
The following command is used to install the Huawei OBS plug-in. The obsutil file is too large and has not been placed in this directory. Users need to go to the Huawei official website to download it. If not, you can delete the scripts for installing the OBS
```shell
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup. sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup. sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxxx -e= http://xxxxx
```
- Multi jdk support
The platform supports user-defined building parameters, and you can specify jdks. If you need to support multiple jdks, you can directly package the jdks into this image. You can refer to the following command. Because the file is too large, the directory of jdk11 is uploaded, and you need to download it by yourself
```shell
ADD openjdk-11.0.2_ linux-x64_ bin.tar. gz /usr/java/jdk/jdk-11.0.2
RUN chmod 777 /usr/java/jdk/jdk-11.0.2
ENV JAVA_ HOME="/usr/java/jdk/jdk-11.0.2/"
```
