#  平台后端采用springboot架构
#   application.yml所在目录为：
- pipeline/jenkins-client/config/application.yml

## 配置文件经过脱敏，如数据库用户密码等信息，需要用户自行配置

### 第16~18行，配置数据库
```yaml
    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8
    username: xxx
    password: xxxx
```	
### 第29~34行，配置邮箱
```yaml
    host: smtp.xxxx.com
    port: 25
    default-encoding: UTF-8
    username: d-fesci
    password: "xxxx"
    from: xxxx@xxxx.com
```
### 第57~60行，配置钉钉（如果不需要，可以不配置，但是参数不能删除，可以为空）	
```yaml
  dingtalk:
    url: https://oapi.dingtalk.com/robot/send?access_token=xxxxx
    secret: xxxx
    accessToken: xxxx
```	
### 第61~63行，配置钉钉（如果不需要，可以不配置，但是参数不能删除，可以为空）
```yaml
  harbor:
    url: registry.iss-devops.cn
    password: Password123!	harbor
```	
	
### 第67行，配置k8s yaml文件存放地，平台可以自动生成k8syaml文件，支持obs与本地存储，如果是本地存储，就需要配置此参数（如果不需要，可以不配置，但是参数不能删除，可以为空）
```yaml
 k8s-folder: D:/data/k8s-yaml

### 第69~70行， 配置redis，平台会将部分信息缓存到redis，redis是必须的配置
```yaml
   redis:
    address: ${redis_addr:redis://redis-plat:6379}
```	
### 第71~78行， 配置文件存储的路径，如果选择local,则不需要再配置hw-obs节点了
```yaml
  storage:
    type: local
```	
#### obs配置	
```yaml
    type: hw-obs
    hw-obs:
      endpointUrl: http://xxxx
      ak: xxxx
      sk: xxxx
      bucketName: obs	
```
### 第80~87行，配置ldap,如果enable选false，后面的ldap信息就不需要配置了
```yaml
ldap:
  enable: true
  url: "ldap://xxxxx:389"
  base: "DC=xxxx,DC=com"
  userDn: "xxx@xxxx.com"
  userPwd: "xxxx"
  referral: ignore
  domainName: "%s@xxxx"
```  
  
### 第89~93行，配置jenkins,  jenkins需要提前安装一些插件，可以参照thridparty\jenkins-base中的dockerfile去安装插件
```yaml
jenkins:
  url: http://jenkins.iss-devops.cn/jenkins/
  user: xx
  password: xxx
  home: /var/jenkins_home  
```
### 第95~96行，配置mavne中lib的存储地址，平台支持maven本地仓库管理（如果不需要，可以不配置，但是参数不能删除，可以为空）  
```yaml
maven:
  local_repo: /usr/share/maven-repo  
```  
### 第99~108行，配置sonarqube,平台集成了sonarqube做代码质量扫描，需要配置sonarqube的地址，以及部分sonarqube api接口，如果不需要集成代码质量扫描，可以不配置  
```yaml
sonar:
  url: http://sonarqube.iss-devops.cn/sonarqube/
  user: admin
  password: admin123
  查询sonarqube支持的语言
  languagesListUrl: http://sonarqube.iss-devops.cn/api/languages/list
  查询语言对应的规则集
  qualityprofilesSearchUrl: http://sonarqube.iss-devops.cn/api/qualityprofiles/search
  为指定项目设置默认规则集
  qualityprofilesAddProjectUrl: http://sonarqube.iss-devops.cn/api/qualityprofiles/add_project  
  
### 第110~111行，配置docker远程地址，平台使用了mavne docker plugin,制作镜像，需要配置docker远程地址，如果不使用docker，可以不配置
```yaml
build:
  docker-host: xxxxxx:2375  
```
### 第127-141行，配置mq信息，平台是从devops中拆出来的单独的开源工具，在与其他工具做交互时，使用的是rabbitmq,此配置项可以不配置，可以在pipeline/jenkins-client/pom.xml中删除mq的startter,但是这些配置项不能删除，  
```yaml
message:
  生产者 需求状态
  ciStatusExchange: ci.status.exchange
  dtpReport: ci.ATSReceive.devqueue
  ciGuardExchange: ci.guard.exchange
  broadcastExchange: ci.datasync.exchange
  topicExchange: devops.plat.exchange
  fanoutExchange: ci.pipeline.exchange
  queue: ci.pipeline.queue
  routingKey: ci.*
  confirmCallbackBean: issRabbitmqConfirmCallbackService
  updateProject: ci.project.update
  returnCallbackBean:
  issNotifyFanoutExchange: iss.notify.exchange
  dtpexchange: devops.dtp.testTopic
```
### 第142-161行，此配置项，集成了我司自己的统一登录平台，可以不配置，参数留空
```yaml
统一登录portal
portal:
  used: true
  统一门户网站的登录地址
  loginUrl: http://upms-web.iss-devops.cn/#/login?tenantCode=MTAxNzgxNTE2MzIwNzY4
  验签接口url
  credentialsUrl: ${credentialsUrl:http://upms-web.iss-devops.cn/system/cookie}
  获取用户相关信息
  getUserInfoUrl: ${getUserInfoUrl:http://upms-web.iss-devops.cn/system/user/getInfo}
  获取用户所在的项目组（upms维护的群组信息）
  getProjectGroupUrl: ${getProjectGroupUrl:http://upms-web.iss-devops.cn/system/group/user/list/user}
  获取子系统下当前用户所对应的菜单信息--前端请求，需要使用域名
  getMenusForCurrentUser: ${getMenusForCurrentUser:http://upms-web.iss-devops.cn/system/resource/resourceWithMenu}
  获取用户角色信息
  getRolesById: http://upms-web.iss-devops.cn/system/openapi/v1/role/list
  upmsTokenRefresh: false  
upms:
  gateway:
    url: "http://upms-web.iss-devops.cn/system/openapi/v1/"
    token: "Bearer xxxx"  
``` 
### 第162-173行，此配置项，集成了redmine，目前这块代码还在整理中，后续会考虑开源，可以不配置，参数留空
```yaml
redmine:
  根据当前用户查询项目组
  getProjectsUrl: http://demand-management.iss-devops.cn/demand-management/project/getProjectsByUserName
  根据项目组查询sprints
  getSprintsUrl: http://demand-management.iss-devops.cn//demand-management/api/sprints/getSprintsByProjectId
  根据sprint 查询对应的需求
  getIssuesUrl: http://demand-management.iss-devops.cn/demand-management/api/issuesTable/getIssuesBySprintId
  根据issuesId查询明细
  getIssuesDetailUrl: http://demand-management.iss-devops.cn/demand-management/api/issuesTable/getIssueById
  根据sprintsid查询看板信息
  getSprintsById: http://demand-management.iss-devops.cn/demand-management/api/sprints/ 
  ```
 
### 第174-185行， 配置平台默认的代码质量阈值，需要与sonarqube配合使用，如果不需要，可以参数留空
```yaml
门禁默认值
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
```  
  
### 第187-194行，配置harbor信息，平台集成了harbor，用来管理镜像，如果不需要，可以参数留空
```yaml
harbor:
  url: https://registry.iss-devops.cn
  username: xxxx
  password: xxxx
  addUserDefaultPwd: xxxx
  project:
    ## 默认设定为20GB
    storageLimit: 21474836480  
    ```

### 第201-203行，配置gitlab api接口地址，平台集成了gitlab，可以执行创建mr等操作，目前这块代码还在整理中，后续会考虑开源，可以不配置，参数留空
```yaml
gitlabApi:
  base_url: http://gitlab-api.iss-devops.cn	
  ```
