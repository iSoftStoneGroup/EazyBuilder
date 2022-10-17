/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.21 : Database - nacos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`nacos` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `nacos`;

/*Table structure for table `config_info` */

DROP TABLE IF EXISTS `config_info`;

CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8_bin,
  `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/*Data for the table `config_info` */

insert  into `config_info`(`id`,`data_id`,`group_id`,`content`,`md5`,`gmt_create`,`gmt_modified`,`src_user`,`src_ip`,`app_name`,`tenant_id`,`c_desc`,`c_use`,`effect`,`type`,`c_schema`,`encrypted_data_key`) values 
(1,'cache.yml','DEFAULT_GROUP','ftc:\r\n  cache:\r\n    # CAFFEINE | REDIS\r\n    type: REDIS\r\n    # 全局配置【最终key: key-prefix + 缓存注解cacheNames/value + 注解key】\r\n    def:\r\n      # 过期时间【单位毫秒，默认一天】\r\n      time-to-live: 2000000\r\n      # 是否允许缓存null值\r\n      cache-null-values: false\r\n      # key的前缀\r\n      key-prefix: global_test\r\n      # 写入redis时，是否使用key前缀\r\n      use-key-prefix: true\r\n      # 最大缓存个数\r\n      max-Size: 50\r\n    # 特殊key配置【最终key: key-prefix + 缓存注解cacheNames/value + 注解key】\r\n    configs:\r\n      customInfoIdCache:\r\n        time-to-live: 2000000\r\n        cache-null-values: true\r\n        key-prefix: custom_info_id\r\n        use-key-prefix: true\r\n        max-Size: 10','b2065df9e3e426f7c35f14bd4685bc15','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','','devops',NULL,NULL,NULL,'yaml',NULL,''),
(2,'jenkins-client.yml','DEFAULT_GROUP','server:\n  port: 8080\n  servlet :\n   context-path: /ci\nspring:\n  servlet:\n    multipart:\n      maxFileSize: 50MB\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    #h2\n    #        driverClassName: org.h2.Driver\n    #        url: jdbc:h2:file:./h2/ci-client;AUTO_SERVER=TRUE;\n    #mysql\n    driverClassName: com.mysql.cj.jdbc.Driver\n    #外网：10.137.25.90:3306\n    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8\n    username: root\n    password: mysql_123\n    maxActive: 50\n    timeBetweenEvictionRunsMillis: 120000\n    testWhileIdle: true\n    validationQuery: select 1\n  jpa:\n    hibernate:\n      #update|none\n      ddl-auto: update\n    show-sql: false\n  mail:\n    host: smtp.eazybuilder.com\n    port: 25\n    default-encoding: UTF-8\n    username: eazybuilder\n    password: \"eazybuilder\"\n    from: ci@eazybuilder.com\n    subject: \"(DevOps平台)CI构建报告\"\n    protocol: smtp\n    properties:\n      mail:\n        smtp:\n          timeout: 30000\n          connectiontimeout: 10000\n  freemarker:\n    template-loader-path: file:config/template/\n    cache: false\n    charset: UTF-8\n    suffix: .ftl\n  http:\n    multipart:\n      maxFileSize: 32MB\n      maxRequestSize: 32MB\nbase:\n  url: http://es-web.eazybuilder.com\n\nci:\n  report:\n    enable: true\n  dingtalk:\n    url: https://oapi.dingtalk.com/robot/send?access_token=xxxx\n    secret: xxxx\n    accessToken: xxxx\n  harbor:\n    url: jinqiu8810\n    password: xxxx\n  scm:\n    user: develop1\n    password: xxxx\n  k8s-folder: \"\"\n  hosts: http://es-web.eazybuilder.com/\n  redis: \n   address: ${redis_addr:redis://redis-plat:6379}\n  storage:\n    type: local\nldap:\n  enable: true\n  url: \"ldap://0.0.0.0:389\"\n  base: \"DC=eazybuilder,DC=com\"\n  userDn: \"ci@eazybuilder.com\"\n  userPwd: \"xxxx\"\n  referral: ignore\n  domainName: \"%s@eazybuilder.com\"\n\njenkins:\n  url: http://jenkins.eazybuilder.com/jenkins/\n  user: admin\n  password: admin\n  home: /var/jenkins_home\n\nmaven:\n  local_repo: /usr/share/maven-repo\n#    local_repo: e:/repository\n\nsonar:\n  url: http://sonarqube.eazybuilder.com/sonarqube/\n  user: admin\n  password: admin123\n  #查询sonarqube支持的语言\n  languagesListUrl: http://sonarqube.eazybuilder.com/api/languages/list\n  #查询语言对应的规则集\n  qualityprofilesSearchUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/search\n  #为指定项目设置默认规则集\n  qualityprofilesAddProjectUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/add_project\n\nbuild:\n  docker-host: 0.0.0.0:2375\n\ntool:\n  git: \"\"\n\nqtp:\n  agent:\n    url: \"\"\n\nselenium:\n  agent:\n    url: \"\"\n\nappscan:\n  agent:\n    url: \"\"\nmessage:\n    #生产者 需求状态\n    ciStatusExchange: ci.status.exchange\n    dtpReport: ci.ATSReceive.devqueue\n    ciGuardExchange: ci.guard.exchange\n    broadcastExchange: ci.datasync.exchange\n    topicExchange: devops.plat.exchange\n    fanoutExchange: ci.pipeline.exchange\n    queue: ci.pipeline.queue\n    routingKey: ci.*\n    confirmCallbackBean: esRabbitmqConfirmCallbackService\n    updateProject: ci.project.update\n    returnCallbackBean:\n    issNotifyFanoutExchange: es.notify.exchange\n    dtpexchange: devops.dtp.testTopic\n#统一登录portal\nportal:\n      used: false\n      #统一门户网站的登录地址\n      loginUrl: \"\"\n      #验签接口url\n      credentialsUrl: \"\"\n      #获取用户相关信息\n      getUserInfoUrl: \"\"\n      #获取用户所在的项目组\n      getProjectGroupUrl: \"\" \n      #获取子系统下当前用户所对应的菜单信息--前端请求，需要使用域名\n      getMenusForCurrentUser: \"\"\n      #获取用户角色信息\n      getRolesById: \"\"\n      upmsTokenRefresh: false\nupms:\n  gateway:\n    url: \"\"\n    token: \"\"\nredmine:\n  #根据当前用户查询项目组\n  getProjectsUrl: \"\"\n  #根据项目组查询sprints\n  getSprintsUrl: \"\"\n  #根据sprint 查询对应的需求\n  getIssuesUrl: \"\"\n  #根据issuesId查询明细\n  getIssuesDetailUrl: \"\"\n  #根据sprintsid查询看板信息\n  getSprintsById: \"\"\n#门禁默认值  \nguard:\n  bug_blocker_name: 默认BUG阻断阈值\n  bug_blocker_name_thresholdMin: 30\n  bug_blocker_name_thresholdMax: 100\n\n  vulner_blocker_name: 默认安全漏洞阻断阈值\n  vulner_blocker_name_thresholdMin: 30\n  vulner_blocker_name_thresholdMax: 100\n    \n  code_smell_blocker_name: 默认编码规范阻断阈值\n  code_smell_blocker_name_thresholdMin: 30\n  codeSmellBlockerNameThresholdMax: 100\n\nharbor:\n    url: https://registry.eazybuilder.com\n    username: es\n    password: xxxx\n    addUserDefaultPwd: xxxx\n    project:\n      ## 默认设定为20GB\n      storageLimit: 21474836480\nlogging:\n  level:\n    com.alibaba.nacos.client.naming: warn\n    org.hibernate.type.descriptor.sql.BasicBinder: error\n    com.iss.dtg.ci.jenkins: warn\n\ngitlabApi:\n  base_url: \"\"\nmirror:\n  url: \"\"','3ff6f2a6f3f853cb87bac476a23799ec','2022-10-17 16:15:02','2022-10-17 16:32:47','nacos','10.10.102.73','','devops','','','','yaml','',''),
(3,'rabbitmq.yml','DEFAULT_GROUP','#Rabbitmq的配置\nspring:\n  rabbitmq:\n    host: rabbitmq\n    port: 5672\n    virtual-host: /\n    username: admin\n    password: admin123\n    #确认消息已发送到交换机exchange(开启手动确认消息)\n    publisher-confirm-type: correlated\n    #确认消息从exchange发送到queue\n    publisher-returns: true \n    # 设置手动确认(ack)\n    listener: \n      direct:\n        acknowledge-mode: manual    \n      simple: \n        acknowledge-mode: manual    \n        prefetch: 200  #一次预加载消息的数量','63bf94d6f154e2415d1cccf25a7745f0','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','','devops',NULL,NULL,NULL,'yaml',NULL,''),
(4,'redis-common.yml','DEFAULT_GROUP','spring:\r\n  redis:\r\n#    database: 0\r\n    host: redis-plat         # Redis服务器地址\r\n    port: 6379              # Redis服务器连接端口\r\n    password:               # Redis服务器连接密码（默认为空）\r\n    pool:\r\n      max-active: 200       # 连接池最大连接数（使用负值表示没有限制）\r\n      max-idle: 10          # 连接池中的最大空闲连接\r\n      max-wait: -1          # 连接池最大阻塞等待时间（使用负值表示没有限制）\r\n      min-idle: 0           # 连接池中的最小空闲连接\r\n    timeout: 10000           # 连接超时时间（毫秒）\r\n        # 关闭spring data 的redis仓库即可\r\n    repositories:\r\n      enabled: false\r\n\r\n','6287b4a078138bf2b7f146b5c3031446','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','','devops',NULL,NULL,NULL,'yaml',NULL,'');

/*Table structure for table `config_info_aggr` */

DROP TABLE IF EXISTS `config_info_aggr`;

CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

/*Data for the table `config_info_aggr` */

/*Table structure for table `config_info_beta` */

DROP TABLE IF EXISTS `config_info_beta`;

CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/*Data for the table `config_info_beta` */

/*Table structure for table `config_info_tag` */

DROP TABLE IF EXISTS `config_info_tag`;

CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/*Data for the table `config_info_tag` */

/*Table structure for table `config_tags_relation` */

DROP TABLE IF EXISTS `config_tags_relation`;

CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/*Data for the table `config_tags_relation` */

/*Table structure for table `group_capacity` */

DROP TABLE IF EXISTS `group_capacity`;

CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/*Data for the table `group_capacity` */

/*Table structure for table `his_config_info` */

DROP TABLE IF EXISTS `his_config_info`;

CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text COLLATE utf8_bin,
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

/*Data for the table `his_config_info` */

insert  into `his_config_info`(`id`,`nid`,`data_id`,`group_id`,`app_name`,`content`,`md5`,`gmt_create`,`gmt_modified`,`src_user`,`src_ip`,`op_type`,`tenant_id`,`encrypted_data_key`) values 
(0,1,'cache.yml','DEFAULT_GROUP','','ftc:\r\n  cache:\r\n    # CAFFEINE | REDIS\r\n    type: REDIS\r\n    # 全局配置【最终key: key-prefix + 缓存注解cacheNames/value + 注解key】\r\n    def:\r\n      # 过期时间【单位毫秒，默认一天】\r\n      time-to-live: 2000000\r\n      # 是否允许缓存null值\r\n      cache-null-values: false\r\n      # key的前缀\r\n      key-prefix: global_test\r\n      # 写入redis时，是否使用key前缀\r\n      use-key-prefix: true\r\n      # 最大缓存个数\r\n      max-Size: 50\r\n    # 特殊key配置【最终key: key-prefix + 缓存注解cacheNames/value + 注解key】\r\n    configs:\r\n      customInfoIdCache:\r\n        time-to-live: 2000000\r\n        cache-null-values: true\r\n        key-prefix: custom_info_id\r\n        use-key-prefix: true\r\n        max-Size: 10','b2065df9e3e426f7c35f14bd4685bc15','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','I','devops',''),
(0,2,'jenkins-client.yml','DEFAULT_GROUP','','server:\n  port: 8080\n  servlet :\n   context-path: /ci\nspring:\n  servlet:\n    multipart:\n      maxFileSize: 50MB\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    #h2\n    #        driverClassName: org.h2.Driver\n    #        url: jdbc:h2:file:./h2/ci-client;AUTO_SERVER=TRUE;\n    #mysql\n    driverClassName: com.mysql.cj.jdbc.Driver\n    #外网：10.137.25.90:3306\n    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8\n    username: root\n    password: mysql_123\n    maxActive: 50\n    timeBetweenEvictionRunsMillis: 120000\n    testWhileIdle: true\n    validationQuery: select 1\n  jpa:\n    hibernate:\n      #update|none\n      ddl-auto: update\n    show-sql: false\n  mail:\n    host: smtp.eazybuilder.com\n    port: 25\n    default-encoding: UTF-8\n    username: eazybuilder\n    password: \"eazybuilder\"\n    from: ci@eazybuilder.com\n    subject: \"(DevOps平台)CI构建报告\"\n    protocol: smtp\n    properties:\n      mail:\n        smtp:\n          timeout: 30000\n          connectiontimeout: 10000\n  freemarker:\n    template-loader-path: file:config/template/\n    cache: false\n    charset: UTF-8\n    suffix: .ftl\n  http:\n    multipart:\n      maxFileSize: 32MB\n      maxRequestSize: 32MB\nbase:\n  url: http://es-web.eazybuilder.com\n\nci:\n  report:\n    enable: true\n  dingtalk:\n    url: https://oapi.dingtalk.com/robot/send?access_token=xxxx\n    secret: xxxx\n    accessToken: xxxx\n  harbor:\n    url: registry.eazybuilder.com\n    password: xxxx\n  scm:\n    user: develop1\n    password: xxxx\n  k8s-folder: \"\"\n  hosts: http://es-web.eazybuilder.com/\n  redis: \n   address: ${redis_addr:redis://redis-plat:6379}\n  storage:\n    type: local\nldap:\n  enable: true\n  url: \"ldap://0.0.0.0:389\"\n  base: \"DC=eazybuilder,DC=com\"\n  userDn: \"ci@eazybuilder.com\"\n  userPwd: \"xxxx\"\n  referral: ignore\n  domainName: \"%s@eazybuilder.com\"\n\njenkins:\n  url: http://jenkins.eazybuilder.com/jenkins/\n  user: admin\n  password: admin\n  home: /var/jenkins_home\n\nmaven:\n  local_repo: /usr/share/maven-repo\n#    local_repo: e:/repository\n\nsonar:\n  url: http://sonarqube.eazybuilder.com/sonarqube/\n  user: admin\n  password: admin123\n  #查询sonarqube支持的语言\n  languagesListUrl: http://sonarqube.eazybuilder.com/api/languages/list\n  #查询语言对应的规则集\n  qualityprofilesSearchUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/search\n  #为指定项目设置默认规则集\n  qualityprofilesAddProjectUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/add_project\n\nbuild:\n  docker-host: 0.0.0.0:2375\n\ntool:\n  git: \"\"\n\nqtp:\n  agent:\n    url: \"\"\n\nselenium:\n  agent:\n    url: \"\"\n\nappscan:\n  agent:\n    url: \"\"\nmessage:\n    #生产者 需求状态\n    ciStatusExchange: ci.status.exchange\n    dtpReport: ci.ATSReceive.devqueue\n    ciGuardExchange: ci.guard.exchange\n    broadcastExchange: ci.datasync.exchange\n    topicExchange: devops.plat.exchange\n    fanoutExchange: ci.pipeline.exchange\n    queue: ci.pipeline.queue\n    routingKey: ci.*\n    confirmCallbackBean: esRabbitmqConfirmCallbackService\n    updateProject: ci.project.update\n    returnCallbackBean:\n    issNotifyFanoutExchange: es.notify.exchange\n    dtpexchange: devops.dtp.testTopic\n#统一登录portal\nportal:\n      used: false\n      #统一门户网站的登录地址\n      loginUrl: \"\"\n      #验签接口url\n      credentialsUrl: \"\"\n      #获取用户相关信息\n      getUserInfoUrl: \"\"\n      #获取用户所在的项目组\n      getProjectGroupUrl: \"\" \n      #获取子系统下当前用户所对应的菜单信息--前端请求，需要使用域名\n      getMenusForCurrentUser: \"\"\n      #获取用户角色信息\n      getRolesById: \"\"\n      upmsTokenRefresh: false\nupms:\n  gateway:\n    url: \"\"\n    token: \"\"\nredmine:\n  #根据当前用户查询项目组\n  getProjectsUrl: \"\"\n  #根据项目组查询sprints\n  getSprintsUrl: \"\"\n  #根据sprint 查询对应的需求\n  getIssuesUrl: \"\"\n  #根据issuesId查询明细\n  getIssuesDetailUrl: \"\"\n  #根据sprintsid查询看板信息\n  getSprintsById: \"\"\n#门禁默认值  \nguard:\n  bug_blocker_name: 默认BUG阻断阈值\n  bug_blocker_name_thresholdMin: 30\n  bug_blocker_name_thresholdMax: 100\n\n  vulner_blocker_name: 默认安全漏洞阻断阈值\n  vulner_blocker_name_thresholdMin: 30\n  vulner_blocker_name_thresholdMax: 100\n    \n  code_smell_blocker_name: 默认编码规范阻断阈值\n  code_smell_blocker_name_thresholdMin: 30\n  codeSmellBlockerNameThresholdMax: 100\n\nharbor:\n    url: https://registry.eazybuilder.com\n    username: es\n    password: xxxx\n    addUserDefaultPwd: xxxx\n    project:\n      ## 默认设定为20GB\n      storageLimit: 21474836480\nlogging:\n  level:\n    com.alibaba.nacos.client.naming: warn\n    org.hibernate.type.descriptor.sql.BasicBinder: error\n    com.iss.dtg.ci.jenkins: warn\n\ngitlabApi:\n  base_url: \"\"\nmirror:\n  url: \"\"','c78cb67d6760e33f146ed2631960271d','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','I','devops',''),
(0,3,'rabbitmq.yml','DEFAULT_GROUP','','#Rabbitmq的配置\nspring:\n  rabbitmq:\n    host: rabbitmq\n    port: 5672\n    virtual-host: /\n    username: admin\n    password: admin123\n    #确认消息已发送到交换机exchange(开启手动确认消息)\n    publisher-confirm-type: correlated\n    #确认消息从exchange发送到queue\n    publisher-returns: true \n    # 设置手动确认(ack)\n    listener: \n      direct:\n        acknowledge-mode: manual    \n      simple: \n        acknowledge-mode: manual    \n        prefetch: 200  #一次预加载消息的数量','63bf94d6f154e2415d1cccf25a7745f0','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','I','devops',''),
(0,4,'redis-common.yml','DEFAULT_GROUP','','spring:\r\n  redis:\r\n#    database: 0\r\n    host: redis-plat         # Redis服务器地址\r\n    port: 6379              # Redis服务器连接端口\r\n    password:               # Redis服务器连接密码（默认为空）\r\n    pool:\r\n      max-active: 200       # 连接池最大连接数（使用负值表示没有限制）\r\n      max-idle: 10          # 连接池中的最大空闲连接\r\n      max-wait: -1          # 连接池最大阻塞等待时间（使用负值表示没有限制）\r\n      min-idle: 0           # 连接池中的最小空闲连接\r\n    timeout: 10000           # 连接超时时间（毫秒）\r\n        # 关闭spring data 的redis仓库即可\r\n    repositories:\r\n      enabled: false\r\n\r\n','6287b4a078138bf2b7f146b5c3031446','2022-10-17 16:15:02','2022-10-17 16:15:02',NULL,'10.10.102.73','I','devops',''),
(2,5,'jenkins-client.yml','DEFAULT_GROUP','','server:\n  port: 8080\n  servlet :\n   context-path: /ci\nspring:\n  servlet:\n    multipart:\n      maxFileSize: 50MB\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    #h2\n    #        driverClassName: org.h2.Driver\n    #        url: jdbc:h2:file:./h2/ci-client;AUTO_SERVER=TRUE;\n    #mysql\n    driverClassName: com.mysql.cj.jdbc.Driver\n    #外网：10.137.25.90:3306\n    url: jdbc:mysql://mysql:3306/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8\n    username: root\n    password: mysql_123\n    maxActive: 50\n    timeBetweenEvictionRunsMillis: 120000\n    testWhileIdle: true\n    validationQuery: select 1\n  jpa:\n    hibernate:\n      #update|none\n      ddl-auto: update\n    show-sql: false\n  mail:\n    host: smtp.eazybuilder.com\n    port: 25\n    default-encoding: UTF-8\n    username: eazybuilder\n    password: \"eazybuilder\"\n    from: ci@eazybuilder.com\n    subject: \"(DevOps平台)CI构建报告\"\n    protocol: smtp\n    properties:\n      mail:\n        smtp:\n          timeout: 30000\n          connectiontimeout: 10000\n  freemarker:\n    template-loader-path: file:config/template/\n    cache: false\n    charset: UTF-8\n    suffix: .ftl\n  http:\n    multipart:\n      maxFileSize: 32MB\n      maxRequestSize: 32MB\nbase:\n  url: http://es-web.eazybuilder.com\n\nci:\n  report:\n    enable: true\n  dingtalk:\n    url: https://oapi.dingtalk.com/robot/send?access_token=xxxx\n    secret: xxxx\n    accessToken: xxxx\n  harbor:\n    url: registry.eazybuilder.com\n    password: xxxx\n  scm:\n    user: develop1\n    password: xxxx\n  k8s-folder: \"\"\n  hosts: http://es-web.eazybuilder.com/\n  redis: \n   address: ${redis_addr:redis://redis-plat:6379}\n  storage:\n    type: local\nldap:\n  enable: true\n  url: \"ldap://0.0.0.0:389\"\n  base: \"DC=eazybuilder,DC=com\"\n  userDn: \"ci@eazybuilder.com\"\n  userPwd: \"xxxx\"\n  referral: ignore\n  domainName: \"%s@eazybuilder.com\"\n\njenkins:\n  url: http://jenkins.eazybuilder.com/jenkins/\n  user: admin\n  password: admin\n  home: /var/jenkins_home\n\nmaven:\n  local_repo: /usr/share/maven-repo\n#    local_repo: e:/repository\n\nsonar:\n  url: http://sonarqube.eazybuilder.com/sonarqube/\n  user: admin\n  password: admin123\n  #查询sonarqube支持的语言\n  languagesListUrl: http://sonarqube.eazybuilder.com/api/languages/list\n  #查询语言对应的规则集\n  qualityprofilesSearchUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/search\n  #为指定项目设置默认规则集\n  qualityprofilesAddProjectUrl: http://sonarqube.eazybuilder.com/api/qualityprofiles/add_project\n\nbuild:\n  docker-host: 0.0.0.0:2375\n\ntool:\n  git: \"\"\n\nqtp:\n  agent:\n    url: \"\"\n\nselenium:\n  agent:\n    url: \"\"\n\nappscan:\n  agent:\n    url: \"\"\nmessage:\n    #生产者 需求状态\n    ciStatusExchange: ci.status.exchange\n    dtpReport: ci.ATSReceive.devqueue\n    ciGuardExchange: ci.guard.exchange\n    broadcastExchange: ci.datasync.exchange\n    topicExchange: devops.plat.exchange\n    fanoutExchange: ci.pipeline.exchange\n    queue: ci.pipeline.queue\n    routingKey: ci.*\n    confirmCallbackBean: esRabbitmqConfirmCallbackService\n    updateProject: ci.project.update\n    returnCallbackBean:\n    issNotifyFanoutExchange: es.notify.exchange\n    dtpexchange: devops.dtp.testTopic\n#统一登录portal\nportal:\n      used: false\n      #统一门户网站的登录地址\n      loginUrl: \"\"\n      #验签接口url\n      credentialsUrl: \"\"\n      #获取用户相关信息\n      getUserInfoUrl: \"\"\n      #获取用户所在的项目组\n      getProjectGroupUrl: \"\" \n      #获取子系统下当前用户所对应的菜单信息--前端请求，需要使用域名\n      getMenusForCurrentUser: \"\"\n      #获取用户角色信息\n      getRolesById: \"\"\n      upmsTokenRefresh: false\nupms:\n  gateway:\n    url: \"\"\n    token: \"\"\nredmine:\n  #根据当前用户查询项目组\n  getProjectsUrl: \"\"\n  #根据项目组查询sprints\n  getSprintsUrl: \"\"\n  #根据sprint 查询对应的需求\n  getIssuesUrl: \"\"\n  #根据issuesId查询明细\n  getIssuesDetailUrl: \"\"\n  #根据sprintsid查询看板信息\n  getSprintsById: \"\"\n#门禁默认值  \nguard:\n  bug_blocker_name: 默认BUG阻断阈值\n  bug_blocker_name_thresholdMin: 30\n  bug_blocker_name_thresholdMax: 100\n\n  vulner_blocker_name: 默认安全漏洞阻断阈值\n  vulner_blocker_name_thresholdMin: 30\n  vulner_blocker_name_thresholdMax: 100\n    \n  code_smell_blocker_name: 默认编码规范阻断阈值\n  code_smell_blocker_name_thresholdMin: 30\n  codeSmellBlockerNameThresholdMax: 100\n\nharbor:\n    url: https://registry.eazybuilder.com\n    username: es\n    password: xxxx\n    addUserDefaultPwd: xxxx\n    project:\n      ## 默认设定为20GB\n      storageLimit: 21474836480\nlogging:\n  level:\n    com.alibaba.nacos.client.naming: warn\n    org.hibernate.type.descriptor.sql.BasicBinder: error\n    com.iss.dtg.ci.jenkins: warn\n\ngitlabApi:\n  base_url: \"\"\nmirror:\n  url: \"\"','c78cb67d6760e33f146ed2631960271d','2022-10-17 16:32:47','2022-10-17 16:32:47','nacos','10.10.102.73','U','devops','');

/*Table structure for table `permissions` */

DROP TABLE IF EXISTS `permissions`;

CREATE TABLE `permissions` (
  `role` varchar(50) COLLATE utf8_bin NOT NULL,
  `resource` varchar(255) COLLATE utf8_bin NOT NULL,
  `action` varchar(8) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `permissions` */

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `role` varchar(50) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `roles` */

insert  into `roles`(`username`,`role`) values 
('nacos','ROLE_ADMIN');

/*Table structure for table `tenant_capacity` */

DROP TABLE IF EXISTS `tenant_capacity`;

CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

/*Data for the table `tenant_capacity` */

/*Table structure for table `tenant_info` */

DROP TABLE IF EXISTS `tenant_info`;

CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

/*Data for the table `tenant_info` */

insert  into `tenant_info`(`id`,`kp`,`tenant_id`,`tenant_name`,`tenant_desc`,`create_source`,`gmt_create`,`gmt_modified`) values 
(1,'1','devops','devops','devops','nacos',1665994492258,1665994492258);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(500) COLLATE utf8_bin NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `users` */

insert  into `users`(`username`,`password`,`enabled`) values 
('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
