
# 第三方插件

EazyBuilder整合了jenkins，sonarqube等插件，本目录包含了制作插件镜像的Dockerfile。使用Dockerfile重新创建镜像，不是必要操作。可以直接在EazyBuilder配置文件中，配置已有的插件访问地址。

# 目录说明

## x86支持
- 平台默认使用x86架构的镜像。

## 注意
- 文件中包含的访问地址，密码等敏感信息，已经做了特殊处理，在重新制作镜像前，首先应该详细阅读目录中的README文件，修改脚本，将访问地址，密码设置为真实有效的信息。

## Jenkins说明
- 平台自动生成jenkins pipeline脚本，然后调用jenkins接口，运行流水线。本目录中包含三个jenkins相关镜像，具体作用如下：
### jenkins-base

- 基础镜像，基于官方jenkins/jenkins镜像，安装了一些必备的插件，并且安装了一些流水线依耐的工具：maven,gradle,sonar-scanner,nodejs,kubernetes docker-plugin,subversion,ansible.

### jenkins-inbound-agent

- 基础镜像，基于官方jenkins/inbound-agent，为kubernetes专用，与jenkins-base类似，安装了一些必备的插件与流水线依耐的工具。

### jenkins-inbound-agent-java  jenkins-inbound-agent-net

- 基于jenkins-inbound-agent镜像，做了一些定制化的特性改造，需要用户根据自己实际情况，修改dockerfile，制作自己的镜像

## 具体详情，可以参照每个目录中的README文件



