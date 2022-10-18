# [中文说明](./README_CN.md)
# The platform backend adopts springboot architecture
# The directory where application.yml is located is:
- pipeline/jenkins-client/config/application.yml

## The configuration file is desensitized, such as database user password and other information, which needs to be configured by the user

### Lines 16~18, configure the database
````yaml
    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8
    username: xxx
    password: xxxx
````
### Lines 29~34, configure the mailbox
````yaml
    host: smtp.xxxx.com
    port: 25
    default-encoding: UTF-8
    username: d-fesci
    password: "xxxx"
    from: xxxx@xxxx.com
````
### Lines 57~60, configure Dingding (if you don't need it, you can not configure it, but the parameter cannot be deleted, it can be empty)
````yaml
  dingtalk:
    url: https://oapi.dingtalk.com/robot/send?access_token=xxxxx
    secret: xxxx
    accessToken: xxxx
````
### Lines 61~63, configure harbor (if you don't need it, you can not configure it, but the parameter cannot be deleted, it can be empty)
````yaml
  harbor:
    url: registry.eazybuilder-devops.cn
    password: Password123!
````

### Line 67, configure the k8s yaml file storage location, the platform can automatically generate k8syaml files, support obs and local storage, if it is local storage, you need to configure this parameter (if not, you can not configure it, but the parameter cannot be deleted , can be empty)
````yaml
 k8s-folder: D:/data/k8s-yaml
````

### Lines 69~70, configure redis, the platform will cache some information to redis, redis is a necessary configuration
````yaml
   redis:
    address: ${redis_addr:redis://redis-plat:6379}
````
### Lines 71~78, the path of the configuration file storage, if you choose local, you do not need to configure the hw-obs node again
````yaml
  storage:
    type: local
````
#### obs configuration
````yaml
    type: hw-obs
    hw-obs:
      endpointUrl: http://xxxx
      ak: xxxx
      sk: xxxx
      bucketName: obs
````
### Lines 80~87, configure ldap, if enable is false, the following ldap information does not need to be configured
````yaml
ldap:
  enable: true
  url: "ldap://xxxxx:389"
  base: "DC=xxxx,DC=com"
  userDn: "xxx@xxxx.com"
  userPwd: "xxxx"
  referral: ignore
  domainName: "%s@xxxx"
````
  
### Lines 89~93, configure jenkins, jenkins needs to install some plug-ins in advance, you can refer to the dockerfile in thridparty\jenkins-base to install the plug-ins
````yaml
Jenkins:
  url: http://jenkins.eazybuilder-devops.cn/jenkins/
  user: xx
  password: xxx
  home: /var/jenkins_home
````
### Lines 95~96, configure the storage address of lib in mavne, the platform supports maven local warehouse management (if not needed, you can not configure it, but the parameter cannot be deleted, it can be empty)
````yaml
maven:
  local_repo: /usr/share/maven-repo
````
### Lines 99~108, configure sonarqube, the platform integrates sonarqube for code quality scanning, you need to configure the address of sonarqube, and some sonarqube api interfaces, if you do not need to integrate code quality scanning, you can not configure
````yaml
sonar:
  url: http://sonarqube.eazybuilder-devops.cn/sonarqube/
  user: admin
  password: admin
  Query the languages ​​supported by sonarqube
  languagesListUrl: http://sonarqube.eazybuilder-devops.cn/api/languages/list
  The rule set corresponding to the query language
  qualityprofilesSearchUrl: http://sonarqube.eazybuilder-devops.cn/api/qualityprofiles/search
  Set the default ruleset for the specified project
  qualityprofilesAddProjectUrl: http://sonarqube.eazybuilder-devops.cn/api/qualityprofiles/add_project
````
### Lines 110~111, configure the docker remote address. The platform uses the mavne docker plugin. To make an image, you need to configure the docker remote address. If you do not use docker, you can not configure it.
````yaml
build:
  docker-host: xxxxxx:2375
````
### Lines 127-141, configure mq information. The platform is a separate open source tool that was removed from devops. When interacting with other tools, rabbitmq is used. This configuration item can be left unconfigured and can be configured in pipeline/ Delete mq's starter in jenkins-client/pom.xml, but these configuration items cannot be deleted,
````yaml
message:
  Producer Demand Status
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
  issNotifyFanoutExchange: eazybuilder.notify.exchange
  dtpexchange: devops.dtp.testTopic
````
### Lines 142-161, this configuration item integrates our own unified login platform, you can not configure it, and leave the parameters blank
````yaml
Unified login portal
portal:
  used: true
  Login address of the unified portal
  loginUrl: http://upms-web.eazybuilder-devops.cn/#/login?tenantCode=MTAxNzgxNTE2MzIwNzY4
  Signature verification interface url
  credentialsUrl: ${credentialsUrl:http://upms-web.eazybuilder-devops.cn/system/cookie}
  Get user related information
  getUserInfoUrl: ${getUserInfoUrl:http://upms-web.eazybuilder-devops.cn/system/user/getInfo}
  Get the project group the user is in (group information maintained by upms)
  getProjectGroupUrl: ${getProjectGroupUrl:http://upms-web.eazybuilder-devops.cn/system/group/user/list/user}
  Get the menu information corresponding to the current user under the subsystem--front-end request, you need to use the domain name
  getMenusForCurrentUser: ${getMenusForCurrentUser:http://upms-web.eazybuilder-devops.cn/system/resource/resourceWithMenu}
  Get user role information
  getRolesById: http://upms-web.eazybuilder-devops.cn/system/openapi/v1/role/list
  upmsTokenRefresh: false
upms:
  gateway:
    url: "http://upms-web.eazybuilder-devops.cn/system/openapi/v1/"
    token: "Bearer xxxx"
````
### Lines 162-173, this configuration item integrates redmine. At present, this code is still being sorted out. Open source will be considered in the future. It can be left blank.
````yaml
redmine:
  Query project group based on current user
  getProjectsUrl: http://demand-management.eazybuilder-devops.cn/demand-management/project/getProjectsByUserName
  Query sprints by project group
  getSprintsUrl: http://demand-management.eazybuilder-devops.cn//demand-management/api/sprints/getSprintsByProjectId
  Query the corresponding requirements according to the sprint
  getIssuesUrl: http://demand-management.eazybuilder-devops.cn/demand-management/api/issuesTable/getIssuesBySprintId
  Query details based on issuesId
  getIssuesDetailUrl: http://demand-management.eazybuilder-devops.cn/demand-management/api/issuesTable/getIssueById
  Query kanban information based on sprintsid
  getSprintsById: http://demand-management.eazybuilder-devops.cn/demand-management/api/sprints/
  ````
 
### Lines 174-185, configure the default code quality threshold of the platform, which needs to be used in conjunction with sonarqube, if not, the parameter can be left blank
````yaml
Access Control Default
guard:
  bug_blocker_name: Default bug blocking threshold
  bug_blocker_name_thresholdMin: 30
  bug_blocker_name_th
