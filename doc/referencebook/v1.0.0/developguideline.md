 

# **EazyBuilder开发手册**



#### 源码安装

**依赖环境**

| 序号  | 名称  | 版本号  |
| ------------ | ------------ | ------------ |
| 1  | Java  | openjdk 1.8.0_282  |
| 2  | node.js  | v14.16.1  |
| 3  |  npm | 6.14.12  |
| 4  | yarn  |  1.22.10 |
| 5  |  Maven |  3.5.4 |
| 6  | SonarScanner  | 4.6.0.2311  |
| 7  |  Git  |  2.20.1 |
| 8  |  Jenkins  |  2.335 |



**1. 配置数据库**

支持mysql数据库/本地h2数据库，需要先创建数据库，应用启动后，会自动在数据库中创建表
例如：
- mysql配置：
```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8
    username: xxxx
    password: xxxxx
```
- h2配置：
```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    #h2
    driverClassName: org.h2.Driver
    url: jdbc:h2:file:./h2/ci-client;AUTO_SERVER=TRUE;
    username: xxxx
    password: xxxxx
```

**2. 配置jenkins**

```yaml
jenkins:
  url: http://jenkins:8080/jenkins/
  user: xxxxx
  password: xxxxxx
  home: /var/jenkins_home
```


3. **启动后端应用**

运行：com.eazybuilder.ci.Application.java


4. **启动前端应用**

- 安装nginx,更改nginx.conf

```json
server {
    listen       80;
    server_name  localhost;
    location /ci {
        proxy_pass   http://localhost:8080/ci/;
    }
    location / {
	    #前端代码目录
        root   E:/eazybuilder/eazybuilder-web/src/main/resources;
    }
}

```

- 启动nginx


5. **访问应用**

访问http://localhost/console/index.html 即可

## 概念

**构建过程**

流水线的执行步骤。

**事件设定**

设置提交代码/合并代码等代码仓库活动对应的构建过程。

**DevOps门禁**

设置编码规范阈值，如：单元测试覆盖率，代码bug个数。


## 快速开始

**工程结构说明**

- common 公共常量/实体/dto
- support 公共工具
- pipeline/buildfile-decorator pom/build.xml装饰器，用于自动添加maven扩展插件(例如jacoco、surefire、docker)
- pipeline/legacy-ant-adapter ant项目转maven项目适配器(自动查询依赖生成pom.xml)
- pipeline/jenkins-client 核心后台应用
- pipeline/pipeline-console 前端页面
- pipeline/report-generator 收集/整理各类报告/指标数据
- thridparty 依赖的第三方中间件/数据库 的docker镜像构建脚本
- deploy 平台自动部署脚本

**配置文件说明**
```yaml
server:
  port: 8080
  servlet :
   context-path: /ci
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    #h2
    #        driverClassName: org.h2.Driver
    #        url: jdbc:h2:file:./h2/ci-client;AUTO_SERVER=TRUE;
    #mysql
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8
    username: root
    password: xxxx
    maxActive: 50
    timeBetweenEvictionRunsMillis: 120000
    testWhileIdle: true
    validationQuery: select 1
  jpa:
    hibernate:
      #update|none
      ddl-auto: update
    show-sql: false
  mail:
    host: smtp.xxxx.com
    port: 25
    default-encoding: UTF-8
    username: xxx
    password: "xxx"
    from: xxxx@xxxx.com
    subject: "(DevOps平台)CI构建报告"
    protocol: smtp
    properties:
      mail:
        smtp:
          timeout: 30000
          connectiontimeout: 10000
  freemarker:
    template-loader-path: file:config/template/
    cache: false
    charset: UTF-8
    suffix: .ftl
  http:
    multipart:
      maxFileSize: 32MB
      maxRequestSize: 32MB
base:
  url: http://xxxx

ci:
  report:
    enable: true
  dingtalk:
    url: https://oapi.dingtalk.com/robot/send?access_token=xxxx
    secret: xxx
    accessToken: xxxx
  harbor:
    url: registry.eazybuilder-devops.cn
    password: xxxx
  scm:
    user: xxxx
    password: xxxx
  k8s-folder: D:/data/k8s-yaml
  hosts: http://xxxx/
  redis: 
   address: ${redis_addr:redis://redis-plat.eazybuilder-devops.cn:16379}
  storage:
    typs: local
#    type: obs
#    hw-obs:
#      endpointUrl: http://xxxxx
#      ak: xxx
#      sk: xxx
#      bucketName: obs

ldap:
  enable: true
  url: "ldap://xxxxxx:389"
  base: "DC=xxx,DC=com"
  userDn: "xxx@xxx.com"
  userPwd: "xxxx"
  referral: ignore
  domainName: "%s@eazybuilder.com"

jenkins:
  url: http://xxxxx/jenkins/
  user: xxxxx
  password: xxxxx
  home: /var/jenkins_home

maven:
  local_repo: /usr/share/maven-repo
#    local_repo: e:/repository

sonar:
  url: http://xxxxx/sonarqube/
  user: admin
  password: xxxxx

build:
  docker-host: xxxxxx:2375

tool:
  git: http://ci-tool:8080/eazybuilder-ci/ci-tool.git

qtp:
  agent:
    url: http://ci-qtp-agent:28880/qtp-agent/api/run

selenium:
  agent:
    url: http://ci-selenium-agent:28881/selenium-agent/api/run

appscan:
  agent:
    url: http://ci-appscan-agent:28882/appscan-agent/api/run
message:
    #生产者 需求状态
    ciStatusExchange: ci.status.exchange
    dtpReport: ci.ATSReceive.devqueue
    ciGuardExchange: ci.guard.exchange
    broadcastExchange: ci.datasync.exchange
    topicExchange: devops.plat.exchange
    fanoutExchange: ci.pipeline.exchange
    queue: ci.pipeline.queue
    routingKey: ci.*
    confirmCallbackBean: eazybuilderRabbitmqConfirmCallbackService
    updateProject: ci.project.update
    returnCallbackBean:
    eazybuilderNotifyFanoutExchange: eazybuilder.notify.exchange
    dtpexchange: devops.dtp.testTopic
#统一登录portal
portal:
      used: false
upms:
  gateway:
    url: 
    token: "Bearer xxxxxx"
redmine:
  #根据当前用户查询项目组
  getProjectsUrl: 
  #根据项目组查询sprints
  getSprintsUrl: 
  #根据sprint 查询对应的需求
  getIssuesUrl: 
  #根据issuesId查询明细
  getIssuesDetailUrl: 
  #根据sprintsid查询看板信息
  getSprintsById: 
#门禁默认值  
guard:
  bug_blocker_name: 默认BUG阻断阈值
  bug_blocker_name_thresholdMin: 30
  bug_blocker_name_thresholdMax: 100

  vulner_blocker_name: 默认安全漏洞阻断阈值
  vulner_blocker_name_thresholdMin: 30
  vulner_blocker_name_thresholdMax: 100
    
  code_smell_blocker_name: 默认编码规范阻断阈值
  code_smell_blocker_name_thresholdMin: 30
  codeSmellBlockerNameThresholdMax: 100

harbor:
    url: https://registry.xxxxxx
    username: xxxx
    password: xxxx
    addUserDefaultPwd: xxxxx
    project:
      ## 默认设定为20GB
      storageLimit: 21474836480
logging:
  level:
    com.alibaba.nacos.client.naming: warn
```


**开发调试**
- 修改jenkins-client/config/application.yaml
- 运行jenkins-client/src/main/java/Application.java
- 安装Nginx，nginx.conf示例配置如下：
```json
  server {      
    listen       80;      
    server_name  xxxxxxxx;      
    location /ci {       
    proxy_pass   http://localhost:8080/ci/;      
        }      
     location / {          
	 root   E://eazybuilder-web/src/main/resources;     
     }  
  }
```

**自定义构建机**

- thridparty模块包含jenkins打包脚本，可调整dockerfile，制作专用构建镜像
- thridparty模块包含jenkins-inbound-agent打包脚本，支持kubernetes弹性调度

