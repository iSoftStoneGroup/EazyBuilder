/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.21 : Database - ci
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ci` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ci`;

/*Table structure for table `ci_api_security` */

DROP TABLE IF EXISTS `ci_api_security`;

CREATE TABLE `ci_api_security` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `secret` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK58214it0tlxp8n4q02hsgh3ik` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_api_security` */

/*Table structure for table `ci_appscan_history` */

DROP TABLE IF EXISTS `ci_appscan_history`;

CREATE TABLE `ci_appscan_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `end_time` datetime(6) DEFAULT NULL,
  `execute_status` int DEFAULT NULL,
  `plan_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `plan_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remind_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `report_file_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `target_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `total_high` int DEFAULT NULL,
  `total_informational` int DEFAULT NULL,
  `total_low` int DEFAULT NULL,
  `total_medium` int DEFAULT NULL,
  `uid` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeu7vxbur0yxdb56t6dmf7iwfk` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_appscan_history` */

/*Table structure for table `ci_appscan_history_detail` */

DROP TABLE IF EXISTS `ci_appscan_history_detail`;

CREATE TABLE `ci_appscan_history_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `execute_order` int DEFAULT NULL,
  `history_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `script_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `total_high` int DEFAULT NULL,
  `total_informational` int DEFAULT NULL,
  `total_low` int DEFAULT NULL,
  `total_medium` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_appscan_history_detail` */

/*Table structure for table `ci_appscan_plan` */

DROP TABLE IF EXISTS `ci_appscan_plan`;

CREATE TABLE `ci_appscan_plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cron` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `execute_type` int DEFAULT NULL,
  `last_trigger` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `next_time` bigint DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_starting_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `timeout_minute` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4ns61ou1i7u69kgbbyysqqjxb` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_appscan_plan` */

/*Table structure for table `ci_appscan_plan_scripts` */

DROP TABLE IF EXISTS `ci_appscan_plan_scripts`;

CREATE TABLE `ci_appscan_plan_scripts` (
  `app_scan_plan_id` int NOT NULL,
  `scripts_id` int NOT NULL,
  KEY `FK8n5ae8ccyftaohrweljac0yx3` (`scripts_id`),
  KEY `FKd82mcqbq1bhvedaw4j1d23ylb` (`app_scan_plan_id`),
  CONSTRAINT `FK8n5ae8ccyftaohrweljac0yx3` FOREIGN KEY (`scripts_id`) REFERENCES `ci_appscan_script` (`id`),
  CONSTRAINT `FKd82mcqbq1bhvedaw4j1d23ylb` FOREIGN KEY (`app_scan_plan_id`) REFERENCES `ci_appscan_plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_appscan_plan_scripts` */

/*Table structure for table `ci_appscan_script` */

DROP TABLE IF EXISTS `ci_appscan_script`;

CREATE TABLE `ci_appscan_script` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `original_host` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `scm_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjn4jk99n80cbfv6uew9forsm9` (`name`),
  KEY `FKftkbqqwg77bo309y1qxmvwl5d` (`scm_id`),
  CONSTRAINT `FKftkbqqwg77bo309y1qxmvwl5d` FOREIGN KEY (`scm_id`) REFERENCES `ci_scm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_appscan_script` */

/*Table structure for table `ci_atm_script` */

DROP TABLE IF EXISTS `ci_atm_script`;

CREATE TABLE `ci_atm_script` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `script_type` int DEFAULT NULL,
  `scm_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKruhkiytgcus1oqluylh4kil14` (`name`),
  KEY `FKssr2dhw0n9cxd43d9u9mrsfqn` (`scm_id`),
  CONSTRAINT `FKssr2dhw0n9cxd43d9u9mrsfqn` FOREIGN KEY (`scm_id`) REFERENCES `ci_scm` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_atm_script` */

/*Table structure for table `ci_audit_profile` */

DROP TABLE IF EXISTS `ci_audit_profile`;

CREATE TABLE `ci_audit_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `owner_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_audit_profile` */

/*Table structure for table `ci_audit_profile_groups` */

DROP TABLE IF EXISTS `ci_audit_profile_groups`;

CREATE TABLE `ci_audit_profile_groups` (
  `audit_profile_id` int NOT NULL,
  `groups_id` int NOT NULL,
  KEY `FK5se0bor9hqk4h4vl6mny4a0fw` (`groups_id`),
  KEY `FKmfcit3ojho7qpxji5eno34jq3` (`audit_profile_id`),
  CONSTRAINT `FK5se0bor9hqk4h4vl6mny4a0fw` FOREIGN KEY (`groups_id`) REFERENCES `ci_project_group` (`id`),
  CONSTRAINT `FKmfcit3ojho7qpxji5eno34jq3` FOREIGN KEY (`audit_profile_id`) REFERENCES `ci_audit_profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_audit_profile_groups` */

/*Table structure for table `ci_config` */

DROP TABLE IF EXISTS `ci_config`;

CREATE TABLE `ci_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cfg_key` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cfg_val` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpo5blbq5wsokwfjfooc1g7ix2` (`cfg_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_config` */

insert  into `ci_config`(`id`,`cfg_key`,`cfg_val`,`description`) values 
(1,'build.docker-host','0.0.0.0:22375','docker远程构建机器'),
(2,'base.url','http://es-web.eazybuilder.com/ci','持续集成平台访问地址'),
(3,'jenkins.csrf.crumb','true','crfs crumb 高版本jenkins需要'),
(4,'registry.url','jinqiu8810',NULL);

/*Table structure for table `ci_deploy` */

DROP TABLE IF EXISTS `ci_deploy`;

CREATE TABLE `ci_deploy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `host_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deploy` */

/*Table structure for table `ci_deploy_config` */

DROP TABLE IF EXISTS `ci_deploy_config`;

CREATE TABLE `ci_deploy_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `app_type` int DEFAULT NULL,
  `container_port` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `hostname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ingress_host` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ingress_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `init_image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `limits_cpu` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `limits_memory` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `replicas` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `storage_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `yaml_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deploy_config` */

insert  into `ci_deploy_config`(`id`,`app_type`,`container_port`,`hostname`,`image_tag`,`ingress_host`,`ingress_path`,`init_image_tag`,`limits_cpu`,`limits_memory`,`name`,`name_space`,`replicas`,`storage_type`,`tag`,`yaml_id`) values 
(1,0,'8080',NULL,'eazybuilder','eazybuilder.eazybuilder-devops.cn',NULL,NULL,'100m','1Gi','eazybuilder',NULL,'1',NULL,NULL,NULL),
(2,0,'8080',NULL,'eazybuilder','eazybuilder.eazybuilder-devops.cn',NULL,NULL,'100m','1Gi','eazybuilder',NULL,'1',NULL,NULL,NULL),
(3,0,'8080',NULL,'eazybuilder','eazybuilder.eazybuilder-devops.cn',NULL,NULL,'100m',NULL,'eazybuilder',NULL,'1',NULL,NULL,NULL);

/*Table structure for table `ci_deploy_config_deploy_config_detail_envs` */

DROP TABLE IF EXISTS `ci_deploy_config_deploy_config_detail_envs`;

CREATE TABLE `ci_deploy_config_deploy_config_detail_envs` (
  `deploy_config_id` int NOT NULL,
  `deploy_config_detail_envs_id` int NOT NULL,
  UNIQUE KEY `UK_opc62xuksus3bhbduki635tsb` (`deploy_config_detail_envs_id`),
  KEY `FK468mkusfd0mjaos3l7x4l5am1` (`deploy_config_id`),
  CONSTRAINT `FK468mkusfd0mjaos3l7x4l5am1` FOREIGN KEY (`deploy_config_id`) REFERENCES `ci_deploy_config` (`id`),
  CONSTRAINT `FKh6uaa0w3qc0bftcki28tfbvef` FOREIGN KEY (`deploy_config_detail_envs_id`) REFERENCES `ci_deploy_config_env` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deploy_config_deploy_config_detail_envs` */

/*Table structure for table `ci_deploy_config_deploy_config_detail_hosts` */

DROP TABLE IF EXISTS `ci_deploy_config_deploy_config_detail_hosts`;

CREATE TABLE `ci_deploy_config_deploy_config_detail_hosts` (
  `deploy_config_id` int NOT NULL,
  `deploy_config_detail_hosts_id` int NOT NULL,
  UNIQUE KEY `UK_9cuw2y4gv5s1q981fa2dg0143` (`deploy_config_detail_hosts_id`),
  KEY `FKa7435f0j332q5950qmwfiveia` (`deploy_config_id`),
  CONSTRAINT `FK9qbqdhcoqwg3ft4wt4m993y21` FOREIGN KEY (`deploy_config_detail_hosts_id`) REFERENCES `ci_deploy_config_env` (`id`),
  CONSTRAINT `FKa7435f0j332q5950qmwfiveia` FOREIGN KEY (`deploy_config_id`) REFERENCES `ci_deploy_config` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deploy_config_deploy_config_detail_hosts` */

/*Table structure for table `ci_deploy_config_env` */

DROP TABLE IF EXISTS `ci_deploy_config_env`;

CREATE TABLE `ci_deploy_config_env` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deploy_config_env` */

/*Table structure for table `ci_deveops_team` */

DROP TABLE IF EXISTS `ci_deveops_team`;

CREATE TABLE `ci_deveops_team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_del` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `ding_secret` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ding_web_hook_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  `project_manage` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_begin_date` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_end_date` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `ding_msg_profile_id` int DEFAULT NULL,
  `mail_msg_profile_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45pcj5vg67iqa949c38ig5lff` (`ding_msg_profile_id`),
  KEY `FK4021ay33to7mitwends70m9k9` (`mail_msg_profile_id`),
  CONSTRAINT `FK4021ay33to7mitwends70m9k9` FOREIGN KEY (`mail_msg_profile_id`) REFERENCES `ci_mail_msg_profile` (`id`),
  CONSTRAINT `FK45pcj5vg67iqa949c38ig5lff` FOREIGN KEY (`ding_msg_profile_id`) REFERENCES `ci_ding_msg_profile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deveops_team` */

insert  into `ci_deveops_team`(`id`,`create_time`,`is_del`,`update_time`,`ding_secret`,`ding_web_hook_url`,`group_id`,`project_manage`,`team_begin_date`,`team_code`,`team_end_date`,`team_name`,`tenant_id`,`user_id`,`ding_msg_profile_id`,`mail_msg_profile_id`) values 
(1,NULL,0,NULL,NULL,NULL,1665997054820,'redmine',NULL,'ci',NULL,'CI项目组',NULL,NULL,NULL,NULL);

/*Table structure for table `ci_deveops_team_devops_projects` */

DROP TABLE IF EXISTS `ci_deveops_team_devops_projects`;

CREATE TABLE `ci_deveops_team_devops_projects` (
  `devops_init_id` int NOT NULL,
  `devops_projects_id` int NOT NULL,
  UNIQUE KEY `UK_tpppyfdifjqvuicp6om9f1vxu` (`devops_projects_id`),
  KEY `FKpeqyb3jw6qja5n5fsdcmpr3e7` (`devops_init_id`),
  CONSTRAINT `FKpeqyb3jw6qja5n5fsdcmpr3e7` FOREIGN KEY (`devops_init_id`) REFERENCES `ci_deveops_team` (`id`),
  CONSTRAINT `FKqkqd09lq1ah4dtyt4m5qilf84` FOREIGN KEY (`devops_projects_id`) REFERENCES `ci_devops_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deveops_team_devops_projects` */

insert  into `ci_deveops_team_devops_projects`(`devops_init_id`,`devops_projects_id`) values 
(1,2);

/*Table structure for table `ci_deveops_team_project_init_statuses` */

DROP TABLE IF EXISTS `ci_deveops_team_project_init_statuses`;

CREATE TABLE `ci_deveops_team_project_init_statuses` (
  `devops_init_id` int NOT NULL,
  `project_init_statuses_id` int NOT NULL,
  PRIMARY KEY (`devops_init_id`,`project_init_statuses_id`),
  UNIQUE KEY `UK_p3veq1j9bgqenic8kvlebwpkf` (`project_init_statuses_id`),
  CONSTRAINT `FK7ct6rpcsdao78tkm0jeics0sv` FOREIGN KEY (`project_init_statuses_id`) REFERENCES `ci_project_init_status` (`id`),
  CONSTRAINT `FKxivh77s7ypdionvdcybj0a7q` FOREIGN KEY (`devops_init_id`) REFERENCES `ci_deveops_team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deveops_team_project_init_statuses` */

/*Table structure for table `ci_deveops_team_team_namespaces` */

DROP TABLE IF EXISTS `ci_deveops_team_team_namespaces`;

CREATE TABLE `ci_deveops_team_team_namespaces` (
  `devops_init_id` int NOT NULL,
  `team_namespaces_id` int NOT NULL,
  PRIMARY KEY (`devops_init_id`,`team_namespaces_id`),
  UNIQUE KEY `UK_hnfqbfhemqmpf2a8yylkbclly` (`team_namespaces_id`),
  CONSTRAINT `FKl3oqsy1h614gcvm197hlwh4gv` FOREIGN KEY (`team_namespaces_id`) REFERENCES `ci_team_namespace` (`id`),
  CONSTRAINT `FKmx10ahif06l0efblpfmrhnmnx` FOREIGN KEY (`devops_init_id`) REFERENCES `ci_deveops_team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_deveops_team_team_namespaces` */

insert  into `ci_deveops_team_team_namespaces`(`devops_init_id`,`team_namespaces_id`) values 
(1,1),
(1,2),
(1,3),
(1,4);

/*Table structure for table `ci_devops_entrance` */

DROP TABLE IF EXISTS `ci_devops_entrance`;

CREATE TABLE `ci_devops_entrance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `inpute_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `valid_status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_devops_entrance` */

/*Table structure for table `ci_devops_project` */

DROP TABLE IF EXISTS `ci_devops_project`;

CREATE TABLE `ci_devops_project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_schema` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legacy_project` int DEFAULT '0',
  `net_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_sln_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_test_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_type` int DEFAULT NULL,
  `pom_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_type` int DEFAULT '0',
  `scm_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sql_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sql_type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_devops_project` */

insert  into `ci_devops_project`(`id`,`description`,`image_schema`,`legacy_project`,`net_path`,`net_sln_path`,`net_test_path`,`net_type`,`pom_path`,`project_code`,`project_name`,`project_type`,`scm_url`,`sql_path`,`sql_type`) values 
(1,'eazybuilder',NULL,0,NULL,NULL,NULL,NULL,'eazybuilder-server/pipeline/buildfile-decorator',NULL,'异构持续集成平台',0,'https://gitee.com/isoftstoneGroup/EazyBuilder.git',NULL,NULL),
(2,'eazybuilder',NULL,1,NULL,NULL,NULL,NULL,'eazybuilder-server/pipeline/buildfile-decorator',NULL,'异构持续集成平台',0,'https://gitee.com/isoftstoneGroup/EazyBuilder.git',NULL,NULL);

/*Table structure for table `ci_devops_project_deploy_config_list` */

DROP TABLE IF EXISTS `ci_devops_project_deploy_config_list`;

CREATE TABLE `ci_devops_project_deploy_config_list` (
  `devops_project_id` int NOT NULL,
  `deploy_config_list_id` int NOT NULL,
  UNIQUE KEY `UK_gko5gx4gbfuwmmkvwuk74pi5k` (`deploy_config_list_id`),
  KEY `FKmk6i3rmxcov8v92dxdrq52y8r` (`devops_project_id`),
  CONSTRAINT `FKmk6i3rmxcov8v92dxdrq52y8r` FOREIGN KEY (`devops_project_id`) REFERENCES `ci_devops_project` (`id`),
  CONSTRAINT `FKn0gfcpgwwgxq0omkwbro2hvd8` FOREIGN KEY (`deploy_config_list_id`) REFERENCES `ci_deploy_config` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_devops_project_deploy_config_list` */

insert  into `ci_devops_project_deploy_config_list`(`devops_project_id`,`deploy_config_list_id`) values 
(1,1),
(2,2);

/*Table structure for table `ci_digest` */

DROP TABLE IF EXISTS `ci_digest`;

CREATE TABLE `ci_digest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `digest` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `namespace` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_digest` */

/*Table structure for table `ci_ding_msg_profile` */

DROP TABLE IF EXISTS `ci_ding_msg_profile`;

CREATE TABLE `ci_ding_msg_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_edit` bit(1) NOT NULL,
  `deploy_run` bit(1) NOT NULL,
  `deploy_wait` bit(1) NOT NULL,
  `dtp_api_test` bit(1) NOT NULL,
  `dtp_performance_test` bit(1) NOT NULL,
  `dtp_security_test` bit(1) NOT NULL,
  `dtp_web_ui_test` bit(1) NOT NULL,
  `focus_redlight_repair_config` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `issues_add` bit(1) NOT NULL,
  `issues_status_update` bit(1) NOT NULL,
  `merge_apply` bit(1) NOT NULL,
  `merge_pass` bit(1) NOT NULL,
  `merge_refused` bit(1) NOT NULL,
  `monitoring_dtp_test_run` bit(1) NOT NULL,
  `monitoring_dtp_test_status` bit(1) NOT NULL,
  `monitoring_job_run` bit(1) NOT NULL,
  `monitoring_measure_data_sync` bit(1) NOT NULL,
  `monitoring_sql` bit(1) NOT NULL,
  `need_to_do_pass3day` bit(1) NOT NULL,
  `online_apply` bit(1) NOT NULL,
  `online_pass` bit(1) NOT NULL,
  `online_refused` bit(1) NOT NULL,
  `online_wait` bit(1) NOT NULL,
  `pipeline_ding_talk` bit(1) NOT NULL,
  `pipeline_fail` bit(1) NOT NULL,
  `push` bit(1) NOT NULL,
  `release_apply` bit(1) NOT NULL,
  `release_pass` bit(1) NOT NULL,
  `release_refused` bit(1) NOT NULL,
  `release_wait` bit(1) NOT NULL,
  `sql_script` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_ding_msg_profile` */

/*Table structure for table `ci_dingtalk_webhook` */

DROP TABLE IF EXISTS `ci_dingtalk_webhook`;

CREATE TABLE `ci_dingtalk_webhook` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `secret` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_dingtalk_webhook` */

/*Table structure for table `ci_docker_image` */

DROP TABLE IF EXISTS `ci_docker_image`;

CREATE TABLE `ci_docker_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pull_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_docker_image` */

insert  into `ci_docker_image`(`id`,`create_date`,`image_tag`,`project_id`,`project_name`,`pull_url`) values 
(1,'2022-10-17 17:20:44.000000','','1','eazybuilder','registry.eazybuilder.com/ci/eazybuilder/');

/*Table structure for table `ci_docker_registry` */

DROP TABLE IF EXISTS `ci_docker_registry`;

CREATE TABLE `ci_docker_registry` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `registry_schema` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_docker_registry` */

insert  into `ci_docker_registry`(`id`,`email`,`password`,`registry_schema`,`team_id`,`url`,`user`) values 
(1,'admin','xxxx','https','1','registry.eazybuilder.com','admin');

/*Table structure for table `ci_dtp_report` */

DROP TABLE IF EXISTS `ci_dtp_report`;

CREATE TABLE `ci_dtp_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint DEFAULT NULL,
  `elapsed_time` bigint DEFAULT NULL,
  `end` bit(1) NOT NULL,
  `env_exception_failed` int DEFAULT NULL,
  `expect_run` int DEFAULT NULL,
  `git_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `message` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_history_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `plan_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `report_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `succeed` bit(1) NOT NULL,
  `test_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `total_fail` int DEFAULT NULL,
  `total_pass` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_dtp_report` */

/*Table structure for table `ci_event` */

DROP TABLE IF EXISTS `ci_event`;

CREATE TABLE `ci_event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `detail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `environment_type` int DEFAULT NULL,
  `event_type` int DEFAULT NULL,
  `is_default` bit(1) NOT NULL,
  `legacy_project` int DEFAULT '0',
  `profile_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_type` int DEFAULT NULL,
  `source_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_event` */

/*Table structure for table `ci_file_resource` */

DROP TABLE IF EXISTS `ci_file_resource`;

CREATE TABLE `ci_file_resource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime(6) DEFAULT NULL,
  `data` mediumblob NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_file_resource` */

/*Table structure for table `ci_guard` */

DROP TABLE IF EXISTS `ci_guard`;

CREATE TABLE `ci_guard` (
  `id` int NOT NULL AUTO_INCREMENT,
  `guard_type` int DEFAULT NULL,
  `level` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `threshold_max` double DEFAULT NULL,
  `threshold_min` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_guard` */

/*Table structure for table `ci_host_info` */

DROP TABLE IF EXISTS `ci_host_info`;

CREATE TABLE `ci_host_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pass` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `port` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_host_info` */

/*Table structure for table `ci_job` */

DROP TABLE IF EXISTS `ci_job`;

CREATE TABLE `ci_job` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cron` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `immed_iately_online` bit(1) NOT NULL,
  `last_trigger` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `next_time` bigint DEFAULT NULL,
  `notify_dingtalk` smallint DEFAULT '0',
  `on_line` bit(1) NOT NULL,
  `on_line_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `qa_job` smallint DEFAULT '0',
  `send_mail_on_fail` bit(1) NOT NULL,
  `status` int DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `trigger_type` int DEFAULT NULL,
  `web_hook_token` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dingtalk_web_hook_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4cj14dix62wsipyu541m5nyd7` (`dingtalk_web_hook_id`),
  CONSTRAINT `FK4cj14dix62wsipyu541m5nyd7` FOREIGN KEY (`dingtalk_web_hook_id`) REFERENCES `ci_dingtalk_webhook` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_job` */

/*Table structure for table `ci_job_projects` */

DROP TABLE IF EXISTS `ci_job_projects`;

CREATE TABLE `ci_job_projects` (
  `build_job_id` int NOT NULL,
  `projects_id` int NOT NULL,
  KEY `FK8a77r0mwwnu2q1uuwgfn1l32b` (`projects_id`),
  KEY `FK1a2jhdt6qyrvu3o02lf3o4i81` (`build_job_id`),
  CONSTRAINT `FK1a2jhdt6qyrvu3o02lf3o4i81` FOREIGN KEY (`build_job_id`) REFERENCES `ci_job` (`id`),
  CONSTRAINT `FK8a77r0mwwnu2q1uuwgfn1l32b` FOREIGN KEY (`projects_id`) REFERENCES `ci_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_job_projects` */

/*Table structure for table `ci_mail_msg_profile` */

DROP TABLE IF EXISTS `ci_mail_msg_profile`;

CREATE TABLE `ci_mail_msg_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_edit` bit(1) NOT NULL,
  `deploy_run` bit(1) NOT NULL,
  `deploy_wait` bit(1) NOT NULL,
  `dtp_api_test` bit(1) NOT NULL,
  `dtp_performance_test` bit(1) NOT NULL,
  `dtp_security_test` bit(1) NOT NULL,
  `dtp_web_ui_test` bit(1) NOT NULL,
  `focus_redlight_repair_config` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `issues_add` bit(1) NOT NULL,
  `issues_status_update` bit(1) NOT NULL,
  `merge_apply` bit(1) NOT NULL,
  `merge_pass` bit(1) NOT NULL,
  `merge_refused` bit(1) NOT NULL,
  `monitoring_dtp_test_run` bit(1) NOT NULL,
  `monitoring_dtp_test_status` bit(1) NOT NULL,
  `monitoring_job_run` bit(1) NOT NULL,
  `monitoring_measure_data_sync` bit(1) NOT NULL,
  `monitoring_sql` bit(1) NOT NULL,
  `need_to_do_pass3day` bit(1) NOT NULL,
  `online_apply` bit(1) NOT NULL,
  `online_pass` bit(1) NOT NULL,
  `online_refused` bit(1) NOT NULL,
  `online_wait` bit(1) NOT NULL,
  `pipeline_fail` bit(1) NOT NULL,
  `push` bit(1) NOT NULL,
  `release_apply` bit(1) NOT NULL,
  `release_pass` bit(1) NOT NULL,
  `release_refused` bit(1) NOT NULL,
  `release_wait` bit(1) NOT NULL,
  `sql_script` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_mail_msg_profile` */

/*Table structure for table `ci_metric` */

DROP TABLE IF EXISTS `ci_metric`;

CREATE TABLE `ci_metric` (
  `id` int NOT NULL AUTO_INCREMENT,
  `assert_succeed` bit(1) NOT NULL,
  `attachment_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `is_red` bit(1) DEFAULT NULL,
  `link` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `threshold_val` double DEFAULT NULL,
  `time` bigint NOT NULL,
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `val` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdhksy0bghqd3ow25x30goap2t` (`pipeline_id`),
  CONSTRAINT `FKdhksy0bghqd3ow25x30goap2t` FOREIGN KEY (`pipeline_id`) REFERENCES `ci_pipeline_history` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_metric` */

/*Table structure for table `ci_online` */

DROP TABLE IF EXISTS `ci_online`;

CREATE TABLE `ci_online` (
  `id` int NOT NULL AUTO_INCREMENT,
  `batch_ddvice` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `batch_detail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `batch_status` int DEFAULT NULL,
  `batch_user_id` bigint DEFAULT NULL,
  `batch_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `dtp_report_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `excutesql` bit(1) DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `immed_iately_online` bit(1) NOT NULL,
  `member_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `member_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `on_line_image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_date` datetime(6) DEFAULT NULL,
  `release_detail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_status` int DEFAULT NULL,
  `release_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sprint_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_nacos` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_online` */

/*Table structure for table `ci_package` */

DROP TABLE IF EXISTS `ci_package`;

CREATE TABLE `ci_package` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `git_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `source_branch_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tag_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_branch_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `top_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_package` */

/*Table structure for table `ci_package_fields` */

DROP TABLE IF EXISTS `ci_package_fields`;

CREATE TABLE `ci_package_fields` (
  `ci_package_id` int NOT NULL,
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ci_package_id`,`name`),
  CONSTRAINT `FKi8qrx86pvqjq5xgyq11y9g6kb` FOREIGN KEY (`ci_package_id`) REFERENCES `ci_package` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_package_fields` */

/*Table structure for table `ci_pipeline_build_history` */

DROP TABLE IF EXISTS `ci_pipeline_build_history`;

CREATE TABLE `ci_pipeline_build_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `arrive_tag_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_tag_detail` longtext COLLATE utf8_bin,
  `create_tag_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crete_branch_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `db_password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `db_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `db_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `docker_image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `gitlab_api_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `online_docker_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_history_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_type` int DEFAULT NULL,
  `profile` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `redmine_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `redmine_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_date` datetime(6) DEFAULT NULL,
  `release_docker_image` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_docker_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rollout_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `source_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_build_history` */

insert  into `ci_pipeline_build_history`(`id`,`arrive_tag_name`,`create_tag_detail`,`create_tag_version`,`crete_branch_version`,`db_password`,`db_url`,`db_user_name`,`docker_image_tag`,`gitlab_api_url`,`name_space`,`online_docker_version`,`pipeline_history_id`,`pipeline_type`,`profile`,`project_id`,`redmine_code`,`redmine_user`,`release_date`,`release_docker_image`,`release_docker_version`,`release_project_id`,`rollout_version`,`source_branch`,`target_branch`) values 
(1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'gitlab-api.eazybuilder-devops.cn',NULL,NULL,'8fd3e966-47a9-4df4-a610-c7d9016830d0',4,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'gitlab-api.eazybuilder-devops.cn',NULL,NULL,'509d00ae-ed3d-4f03-b51b-d6b198dc4901',4,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `ci_pipeline_history` */

DROP TABLE IF EXISTS `ci_pipeline_history`;

CREATE TABLE `ci_pipeline_history` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `assert_threshold_faild` bit(1) NOT NULL,
  `ci_package_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ci_repair_alert_level_enum` int DEFAULT NULL,
  `dtp_task` bit(1) NOT NULL,
  `duration_millis` bigint NOT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `end_time_millis` bigint NOT NULL,
  `job_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `log_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `npm_instal` bit(1) NOT NULL,
  `pipeline_type` int DEFAULT NULL,
  `pipeline_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_date` datetime(6) DEFAULT NULL,
  `rollout_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `run_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `scm_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `source_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_time_millis` bigint NOT NULL,
  `status` int DEFAULT NULL,
  `status_guard` int DEFAULT NULL,
  `target_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_history` */

insert  into `ci_pipeline_history`(`id`,`assert_threshold_faild`,`ci_package_id`,`ci_repair_alert_level_enum`,`dtp_task`,`duration_millis`,`email`,`end_time_millis`,`job_id`,`log_id`,`name`,`name_space`,`npm_instal`,`pipeline_type`,`pipeline_version`,`profile_id`,`profile_name`,`project_name`,`release_date`,`rollout_version`,`run_branch`,`scm_version`,`source_branch`,`start_time_millis`,`status`,`status_guard`,`target_branch`,`project_id`) values 
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','\0',NULL,NULL,'',24779,'admin',1665998436908,NULL,'19282/a3e2c8bb-4fb2-4638-aae8-661df2dfcf31/console.txt','#1','dev','\0',4,NULL,'1','代码编译','eazybuilder/异构持续集成平台',NULL,NULL,NULL,NULL,NULL,1665998412129,2,NULL,'master',1),
('8fd3e966-47a9-4df4-a610-c7d9016830d0','\0',NULL,NULL,'',0,NULL,0,NULL,NULL,NULL,'dev','\0',4,NULL,'1','代码编译','eazybuilder/异构持续集成平台',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,'master',1);

/*Table structure for table `ci_pipeline_history_dtp_reports` */

DROP TABLE IF EXISTS `ci_pipeline_history_dtp_reports`;

CREATE TABLE `ci_pipeline_history_dtp_reports` (
  `pipeline_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `dtp_reports_id` int NOT NULL,
  UNIQUE KEY `UK_iuosmstj38et5wew92kp22duk` (`dtp_reports_id`),
  KEY `FKebh56wln5vetu2rieeoeeoelp` (`pipeline_id`),
  CONSTRAINT `FKbgrmqgkcrxjdkrh7t26v89gv3` FOREIGN KEY (`dtp_reports_id`) REFERENCES `ci_dtp_report` (`id`),
  CONSTRAINT `FKebh56wln5vetu2rieeoeeoelp` FOREIGN KEY (`pipeline_id`) REFERENCES `ci_pipeline_history` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_history_dtp_reports` */

/*Table structure for table `ci_pipeline_history_stages` */

DROP TABLE IF EXISTS `ci_pipeline_history_stages`;

CREATE TABLE `ci_pipeline_history_stages` (
  `pipeline_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `stages_id` varchar(255) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `UK_jja7faw9xxfojs1nb5fu4ot9j` (`stages_id`),
  KEY `FK6b1ti2th2wtdyx5waq71bhbu1` (`pipeline_id`),
  CONSTRAINT `FK6b1ti2th2wtdyx5waq71bhbu1` FOREIGN KEY (`pipeline_id`) REFERENCES `ci_pipeline_history` (`id`),
  CONSTRAINT `FK90yv1dqtuxd88fq0we09src37` FOREIGN KEY (`stages_id`) REFERENCES `ci_pipeline_stage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_history_stages` */

insert  into `ci_pipeline_history_stages`(`pipeline_id`,`stages_id`) values 
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','26658607-dbb1-4932-a8ef-f50487d8a7f4'),
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','3428ebc9-5e46-4dc2-ac53-4c710ee23129'),
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','7a00c4ee-35b1-4e73-b0c7-6625c093c2a7'),
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','94c7072d-5ff7-4a43-9783-a783642d2c91'),
('509d00ae-ed3d-4f03-b51b-d6b198dc4901','ce524d34-97d3-40f7-af81-b3349b4a31e2');

/*Table structure for table `ci_pipeline_log` */

DROP TABLE IF EXISTS `ci_pipeline_log`;

CREATE TABLE `ci_pipeline_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `exception_log` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `execute_type` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pipeline_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_log` */

insert  into `ci_pipeline_log`(`id`,`event_type`,`exception_log`,`execute_type`,`name`,`pipeline_id`,`status`) values 
(1,NULL,NULL,0,NULL,'8fd3e966-47a9-4df4-a610-c7d9016830d0',NULL),
(2,NULL,NULL,0,NULL,'509d00ae-ed3d-4f03-b51b-d6b198dc4901',NULL);

/*Table structure for table `ci_pipeline_profile` */

DROP TABLE IF EXISTS `ci_pipeline_profile`;

CREATE TABLE `ci_pipeline_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `add_tag` bit(1) NOT NULL,
  `all_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `assign_yaml` bit(1) NOT NULL,
  `assign_yaml_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `build_arm64image` bit(1) NOT NULL,
  `build_param` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `build_property` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `check_pom` bit(1) NOT NULL,
  `config_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_pom` bit(1) DEFAULT NULL,
  `createmr` bit(1) NOT NULL,
  `deploy_war` int DEFAULT '0',
  `focus_redlight_repair` bit(1) NOT NULL,
  `gitlab_api_domain` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `init_deploy` bit(1) NOT NULL,
  `is_del` int DEFAULT '0',
  `kubectl_config` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merge_designee` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `online_deploy` bit(1) NOT NULL,
  `public_profile` int DEFAULT '1',
  `release_prefix` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `restart_deploy` bit(1) NOT NULL,
  `rollback_yaml` bit(1) NOT NULL,
  `rollback_yaml_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `rollout` bit(1) NOT NULL,
  `send_mail_switch` int DEFAULT NULL,
  `skip_anchore` bit(1) NOT NULL,
  `skip_clone_code` bit(1) NOT NULL,
  `skip_dependency_check` bit(1) NOT NULL,
  `skip_deploy` bit(1) NOT NULL,
  `skip_docker_build` bit(1) NOT NULL,
  `skip_js_scan` bit(1) NOT NULL,
  `skip_mvn_build` bit(1) NOT NULL,
  `skip_scan` bit(1) NOT NULL,
  `skip_sql_scan` bit(1) NOT NULL,
  `skip_unit_test` bit(1) NOT NULL,
  `sql_remind` bit(1) NOT NULL,
  `sql_script` bit(1) NOT NULL,
  `sql_take_min` bit(1) NOT NULL,
  `sql_take_min_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `static_js` bit(1) DEFAULT NULL,
  `tag_prefix` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `test_delay_time` int DEFAULT NULL,
  `test_switch` int DEFAULT NULL,
  `update_config` bit(1) NOT NULL,
  `update_job` bit(1) NOT NULL,
  `update_tag` bit(1) NOT NULL,
  `upgrade_docker` bit(1) NOT NULL,
  `deploy_info_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKs60ohh2nm68g6q9cgggov8sre` (`name`),
  KEY `FKbnd7bd3c24bool1th5uwilhvu` (`deploy_info_id`),
  CONSTRAINT `FKbnd7bd3c24bool1th5uwilhvu` FOREIGN KEY (`deploy_info_id`) REFERENCES `ci_deploy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_profile` */

insert  into `ci_pipeline_profile`(`id`,`add_tag`,`all_name`,`assign_yaml`,`assign_yaml_path`,`build_arm64image`,`build_param`,`build_property`,`check_pom`,`config_path`,`update_pom`,`createmr`,`deploy_war`,`focus_redlight_repair`,`gitlab_api_domain`,`init_deploy`,`is_del`,`kubectl_config`,`merge_designee`,`name`,`name_space`,`online_deploy`,`public_profile`,`release_prefix`,`restart_deploy`,`rollback_yaml`,`rollback_yaml_path`,`rollout`,`send_mail_switch`,`skip_anchore`,`skip_clone_code`,`skip_dependency_check`,`skip_deploy`,`skip_docker_build`,`skip_js_scan`,`skip_mvn_build`,`skip_scan`,`skip_sql_scan`,`skip_unit_test`,`sql_remind`,`sql_script`,`sql_take_min`,`sql_take_min_url`,`static_js`,`tag_prefix`,`team_id`,`team_name`,`test_delay_time`,`test_switch`,`update_config`,`update_job`,`update_tag`,`upgrade_docker`,`deploy_info_id`) values 
(1,'\0',NULL,'\0',NULL,'\0',NULL,NULL,'\0',NULL,'\0','\0',0,'\0','gitlab-api.eazybuilder.com','\0',0,NULL,NULL,'代码编译','dev','\0',0,NULL,'\0','\0',NULL,'\0',0,'\0','\0','','','','\0','\0','','','','\0','\0','\0',NULL,NULL,NULL,'1','CI项目组',NULL,NULL,'\0','\0','\0','\0',NULL);

/*Table structure for table `ci_pipeline_stage` */

DROP TABLE IF EXISTS `ci_pipeline_stage`;

CREATE TABLE `ci_pipeline_stage` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `duration_millis` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_time_millis` bigint NOT NULL,
  `status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_pipeline_stage` */

insert  into `ci_pipeline_stage`(`id`,`duration_millis`,`name`,`start_time_millis`,`status`) values 
('26658607-dbb1-4932-a8ef-f50487d8a7f4',1593,'Declarative: Post Actions',1665998435237,'SUCCESS'),
('3428ebc9-5e46-4dc2-ac53-4c710ee23129',1659,'checkout from scm',1665998414820,'SUCCESS'),
('7a00c4ee-35b1-4e73-b0c7-6625c093c2a7',3098,'decorate project',1665998432096,'SUCCESS'),
('94c7072d-5ff7-4a43-9783-a783642d2c91',15555,'maven build',1665998416510,'SUCCESS'),
('ce524d34-97d3-40f7-af81-b3349b4a31e2',2174,'update ci-tools',1665998412611,'SUCCESS');

/*Table structure for table `ci_profile_history` */

DROP TABLE IF EXISTS `ci_profile_history`;

CREATE TABLE `ci_profile_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `json_data` longtext COLLATE utf8_bin,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `profile_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `update_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_profile_history` */

insert  into `ci_profile_history`(`id`,`json_data`,`name_space`,`profile_id`,`profile_name`,`team_name`,`update_time`,`update_user`,`user_id`) values 
(1,'{\"addTag\":false,\"assignYaml\":false,\"buildArm64Image\":false,\"checkPom\":false,\"createBranch\":false,\"createMR\":false,\"del\":false,\"deployWar\":false,\"focusRedlightRepair\":false,\"gitlabApiDomain\":\"gitlab-api.eazybuilder.com\",\"id\":\"1\",\"initDeploy\":false,\"name\":\"代码编译\",\"nameSpace\":\"dev\",\"onlineDeploy\":false,\"publicProfile\":false,\"restartDeploy\":false,\"rollbackYaml\":false,\"rollout\":false,\"sendMailSwitch\":\"TOTAL\",\"skipAnchore\":false,\"skipCloneCode\":false,\"skipDependencyCheck\":true,\"skipDeploy\":true,\"skipDockerBuild\":false,\"skipJsScan\":false,\"skipMvnBuild\":false,\"skipScan\":true,\"skipSqlScan\":true,\"skipUnitTest\":true,\"sqlRemind\":false,\"sqlScript\":false,\"sqlTakeMin\":false,\"teamId\":\"1\",\"teamName\":\"CI项目组\",\"updateConfig\":false,\"updateJob\":false,\"updateTag\":false,\"upgradeDocker\":false}','dev','1','代码编译','CI项目组','2022-10-17 16:55:22.568000','平台管理员','1'),
(2,'{\"addTag\":false,\"assignYaml\":false,\"buildArm64Image\":false,\"checkPom\":false,\"createBranch\":false,\"createMR\":false,\"del\":false,\"deployWar\":false,\"focusRedlightRepair\":false,\"gitlabApiDomain\":\"gitlab-api.eazybuilder.com\",\"id\":\"1\",\"initDeploy\":false,\"name\":\"代码编译\",\"nameSpace\":\"dev\",\"onlineDeploy\":false,\"publicProfile\":false,\"restartDeploy\":false,\"rollbackYaml\":false,\"rollout\":false,\"sendMailSwitch\":\"TOTAL\",\"skipAnchore\":false,\"skipCloneCode\":false,\"skipDependencyCheck\":true,\"skipDeploy\":true,\"skipDockerBuild\":false,\"skipJsScan\":false,\"skipMvnBuild\":false,\"skipScan\":true,\"skipSqlScan\":true,\"skipUnitTest\":true,\"sqlRemind\":false,\"sqlScript\":false,\"sqlTakeMin\":false,\"teamId\":\"1\",\"teamName\":\"CI项目组\",\"updateConfig\":false,\"updateJob\":false,\"updateTag\":false,\"upgradeDocker\":false}','dev','1','代码编译','CI项目组','2022-10-17 17:19:45.027000','平台管理员','1');

/*Table structure for table `ci_project` */

DROP TABLE IF EXISTS `ci_project`;

CREATE TABLE `ci_project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_del` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `auto_build` int DEFAULT '1',
  `build_param` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `build_property` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `code_charset` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deploy_type` int DEFAULT '1',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `devops_project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `eazybuilder_ejb_project` int DEFAULT '0',
  `eazybuilder_style_project` int DEFAULT '0',
  `git_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_schema` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `jdk` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `job_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legacy_project` int DEFAULT '0',
  `lib_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_sln_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_test_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `net_type` int DEFAULT NULL,
  `pom_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_type` int DEFAULT '0',
  `sonar_key` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sql_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sql_type` int DEFAULT NULL,
  `src_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `default_profile_id` int DEFAULT NULL,
  `deploy_info_id` int DEFAULT NULL,
  `registry_id` int DEFAULT NULL,
  `scm_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjlf46srkl5s4u6yp1h1j3iwyy` (`name`,`team_id`),
  KEY `FK5iei6qsfyi3eeaqi7sqwrm9wx` (`default_profile_id`),
  KEY `FKh2al60nnwbvsx0a7sk66xmmav` (`deploy_info_id`),
  KEY `FK62i88ye9af5d1cp9dx6ey4xq1` (`scm_id`),
  KEY `FK6842d0bok9aoxkpcu5wcp4v34` (`team_id`),
  CONSTRAINT `FK5iei6qsfyi3eeaqi7sqwrm9wx` FOREIGN KEY (`default_profile_id`) REFERENCES `ci_pipeline_profile` (`id`),
  CONSTRAINT `FK62i88ye9af5d1cp9dx6ey4xq1` FOREIGN KEY (`scm_id`) REFERENCES `ci_scm` (`id`),
  CONSTRAINT `FK6842d0bok9aoxkpcu5wcp4v34` FOREIGN KEY (`team_id`) REFERENCES `ci_team` (`id`),
  CONSTRAINT `FKh2al60nnwbvsx0a7sk66xmmav` FOREIGN KEY (`deploy_info_id`) REFERENCES `ci_deploy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project` */

insert  into `ci_project`(`id`,`create_time`,`is_del`,`update_time`,`auto_build`,`build_param`,`build_property`,`code_charset`,`deploy_type`,`description`,`devops_project_id`,`eazybuilder_ejb_project`,`eazybuilder_style_project`,`git_branch`,`image_schema`,`image_tag`,`jdk`,`job_name`,`legacy_project`,`lib_path`,`name`,`net_path`,`net_sln_path`,`net_test_path`,`net_type`,`pom_path`,`project_type`,`sonar_key`,`sql_path`,`sql_type`,`src_path`,`default_profile_id`,`deploy_info_id`,`registry_id`,`scm_id`,`team_id`) values 
(1,NULL,0,NULL,0,NULL,NULL,NULL,0,'异构持续集成平台','2',0,0,NULL,'ci',NULL,NULL,'eazybuilder',0,NULL,'eazybuilder',NULL,NULL,NULL,NULL,'eazybuilder-server/pipeline/buildfile-decorator',0,NULL,NULL,NULL,NULL,1,NULL,1,1,1);

/*Table structure for table `ci_project_deploy_config_list` */

DROP TABLE IF EXISTS `ci_project_deploy_config_list`;

CREATE TABLE `ci_project_deploy_config_list` (
  `project_id` int NOT NULL,
  `deploy_config_list_id` int NOT NULL,
  UNIQUE KEY `UK_my6npbknh72rsmcpn95nis9pr` (`deploy_config_list_id`),
  KEY `FK5cx1fkjucp2rma5iovasyvfbc` (`project_id`),
  CONSTRAINT `FK522p2228ci4na41bhhke85pnc` FOREIGN KEY (`deploy_config_list_id`) REFERENCES `ci_deploy_config` (`id`),
  CONSTRAINT `FK5cx1fkjucp2rma5iovasyvfbc` FOREIGN KEY (`project_id`) REFERENCES `ci_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_deploy_config_list` */

insert  into `ci_project_deploy_config_list`(`project_id`,`deploy_config_list_id`) values 
(1,3);

/*Table structure for table `ci_project_group` */

DROP TABLE IF EXISTS `ci_project_group`;

CREATE TABLE `ci_project_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `leader` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_group` */

/*Table structure for table `ci_project_group_projects` */

DROP TABLE IF EXISTS `ci_project_group_projects`;

CREATE TABLE `ci_project_group_projects` (
  `project_group_id` int NOT NULL,
  `projects_id` int NOT NULL,
  PRIMARY KEY (`project_group_id`,`projects_id`),
  KEY `FKqaltvjevotfrifdeffwtgv3mr` (`projects_id`),
  CONSTRAINT `FKq8br7r4lf9sck5if0k9cyuoiv` FOREIGN KEY (`project_group_id`) REFERENCES `ci_project_group` (`id`),
  CONSTRAINT `FKqaltvjevotfrifdeffwtgv3mr` FOREIGN KEY (`projects_id`) REFERENCES `ci_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_group_projects` */

/*Table structure for table `ci_project_history` */

DROP TABLE IF EXISTS `ci_project_history`;

CREATE TABLE `ci_project_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `json_data` longtext COLLATE utf8_bin,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `update_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_history` */

insert  into `ci_project_history`(`id`,`json_data`,`project_id`,`project_name`,`team_id`,`team_name`,`update_time`,`update_user`,`user_id`) values 
(1,'{\"autoBuild\":false,\"deployConfigList\":[{\"appType\":\"deployment\",\"containerPort\":\"8080\",\"id\":\"3\",\"imageTag\":\"eazybuilder\",\"ingressHost\":\"eazybuilder.eazybuilder-devops.cn\",\"limitsCpu\":\"100m\",\"name\":\"eazybuilder\",\"replicas\":\"1\"}],\"deployType\":\"k8s\",\"description\":\"异构持续集成平台\",\"devopsProjectId\":\"2\",\"eazybuilderEjbProject\":false,\"eazybuilderStyleProject\":false,\"id\":\"1\",\"imageSchema\":\"ci\",\"isDel\":0,\"legacyProject\":false,\"name\":\"eazybuilder\",\"piplineEventProjectId\":\"2\",\"pomPath\":\"eazybuilder-server/pipeline/buildfile-decorator\",\"projectType\":\"java\",\"registry\":{\"changePwd\":false,\"email\":\"admin\",\"id\":\"1\",\"password\":\"xxxx\",\"schema\":\"https\",\"teamId\":\"1\",\"url\":\"registry.eazybuilder.com\",\"user\":\"admin\"},\"scm\":{\"changePwd\":false,\"id\":\"1\",\"password\":\"xxxx\",\"tagName\":\"master\",\"type\":\"git\",\"url\":\"https://gitee.com/isoftstoneGroup/EazyBuilder.git\",\"user\":\"develop1\"},\"team\":{\"code\":\"ci\",\"configers\":[],\"devopsTeamId\":\"1\",\"groupId\":1665997054820,\"guards\":[],\"id\":\"1\",\"isDel\":0,\"members\":[{\"email\":\"user1@eazybuilder.com\",\"id\":\"2\",\"name\":\"用户1\",\"password\":\"0RxyCrqD5DbMmksCPXVfqivf5cGL4GjZ2sU+enuDtS0=\",\"phone\":\"18888888888\",\"roles\":[]},{\"email\":\"admin\",\"id\":\"1\",\"name\":\"平台管理员\",\"password\":\"psQq8qzGivXpROJH+98jX07G8df9TFJiJlfpXNSshQM=\",\"roles\":[{\"admin\":true,\"auditReader\":true,\"id\":3,\"roleEnum\":\"admin\"}]}],\"name\":\"CI项目组\",\"teamThresholds\":[]}}','1','异构持续集成平台',NULL,'CI项目组','2022-10-17 16:58:16.720000','平台管理员',NULL),
(2,'{\"autoBuild\":false,\"deployConfigList\":[{\"appType\":\"deployment\",\"containerPort\":\"8080\",\"deployConfigDetailEnvs\":[],\"deployConfigDetailHosts\":[],\"id\":\"3\",\"imageTag\":\"eazybuilder\",\"ingressHost\":\"eazybuilder.eazybuilder-devops.cn\",\"limitsCpu\":\"100m\",\"name\":\"eazybuilder\",\"replicas\":\"1\"}],\"deployType\":\"k8s\",\"description\":\"异构持续集成平台\",\"devopsProjectId\":\"2\",\"eazybuilderEjbProject\":false,\"eazybuilderStyleProject\":false,\"id\":\"1\",\"imageSchema\":\"ci\",\"isDel\":0,\"legacyProject\":false,\"name\":\"eazybuilder\",\"piplineEventProjectId\":\"2\",\"pomPath\":\"eazybuilder-server/pipeline/buildfile-decorator\",\"projectType\":\"java\",\"registry\":{\"changePwd\":false,\"email\":\"admin\",\"id\":\"1\",\"password\":\"xxxx\",\"schema\":\"https\",\"teamId\":\"1\",\"url\":\"registry.eazybuilder.com\",\"user\":\"admin\"},\"scm\":{\"changePwd\":false,\"id\":\"1\",\"password\":\"xxxx\",\"tagName\":\"master\",\"type\":\"git\",\"url\":\"https://gitee.com/isoftstoneGroup/EazyBuilder.git\",\"user\":\"develop1\"},\"team\":{\"code\":\"ci\",\"configers\":[],\"devopsTeamId\":\"1\",\"groupId\":1665997054820,\"id\":\"1\",\"isDel\":0,\"members\":[{\"email\":\"admin\",\"id\":\"1\",\"name\":\"平台管理员\",\"password\":\"psQq8qzGivXpROJH+98jX07G8df9TFJiJlfpXNSshQM=\",\"roles\":[{\"admin\":true,\"auditReader\":true,\"id\":3,\"roleEnum\":\"admin\"}]},{\"email\":\"user1@eazybuilder.com\",\"id\":\"2\",\"name\":\"用户1\",\"password\":\"0RxyCrqD5DbMmksCPXVfqivf5cGL4GjZ2sU+enuDtS0=\",\"phone\":\"18888888888\",\"roles\":[]}],\"name\":\"CI项目组\",\"teamThresholds\":[]}}','1','异构持续集成平台',NULL,'CI项目组','2022-10-17 16:58:31.577000','平台管理员',NULL);

/*Table structure for table `ci_project_init_status` */

DROP TABLE IF EXISTS `ci_project_init_status`;

CREATE TABLE `ci_project_init_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_del` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `project_code` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_init_status` */

/*Table structure for table `ci_project_statistic_record` */

DROP TABLE IF EXISTS `ci_project_statistic_record`;

CREATE TABLE `ci_project_statistic_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `err_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `success` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKscx2ubpiqsqf9eegcsreshdad` (`project_id`,`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_project_statistic_record` */

/*Table structure for table `ci_release` */

DROP TABLE IF EXISTS `ci_release`;

CREATE TABLE `ci_release` (
  `id` int NOT NULL AUTO_INCREMENT,
  `batch_ddvice` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `batch_detail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `batch_status` int DEFAULT NULL,
  `batch_user_id` bigint DEFAULT NULL,
  `batch_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `git_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `image_tag` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `issues_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `issues_tree_json` longtext COLLATE utf8_bin,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_date` datetime(6) DEFAULT NULL,
  `release_detail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_status` int DEFAULT NULL,
  `release_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sprint_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tag_detail` longtext COLLATE utf8_bin,
  `team_id` int DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_release` */

/*Table structure for table `ci_release_pipeline_list` */

DROP TABLE IF EXISTS `ci_release_pipeline_list`;

CREATE TABLE `ci_release_pipeline_list` (
  `release_id` int NOT NULL,
  `pipeline_list_id` varchar(255) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `UK_cmq5cyo3rvi4tc6dvar9wvi15` (`pipeline_list_id`),
  KEY `FKtncgwoq1omph26o0deatdsqaa` (`release_id`),
  CONSTRAINT `FKi6m3paswep752upwp518baowt` FOREIGN KEY (`pipeline_list_id`) REFERENCES `ci_pipeline_history` (`id`),
  CONSTRAINT `FKtncgwoq1omph26o0deatdsqaa` FOREIGN KEY (`release_id`) REFERENCES `ci_release` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_release_pipeline_list` */

/*Table structure for table `ci_release_project` */

DROP TABLE IF EXISTS `ci_release_project`;

CREATE TABLE `ci_release_project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_tag_detail` longtext COLLATE utf8_bin,
  `create_tag_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crete_branch_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `history_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `online_docker_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_git_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_docker_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `release_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_release_project` */

/*Table structure for table `ci_release_project_list` */

DROP TABLE IF EXISTS `ci_release_project_list`;

CREATE TABLE `ci_release_project_list` (
  `release_id` int NOT NULL,
  `project_list_id` int NOT NULL,
  KEY `FKhwj4rmu9nxda90e7wwimdhyb4` (`project_list_id`),
  KEY `FKmfoifn1uukdjs2h36u895je5s` (`release_id`),
  CONSTRAINT `FKhwj4rmu9nxda90e7wwimdhyb4` FOREIGN KEY (`project_list_id`) REFERENCES `ci_project` (`id`),
  CONSTRAINT `FKmfoifn1uukdjs2h36u895je5s` FOREIGN KEY (`release_id`) REFERENCES `ci_release` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_release_project_list` */

/*Table structure for table `ci_release_release_projects` */

DROP TABLE IF EXISTS `ci_release_release_projects`;

CREATE TABLE `ci_release_release_projects` (
  `release_id` int NOT NULL,
  `release_projects_id` int NOT NULL,
  UNIQUE KEY `UK_tebrk8648t8iqdc6g2tkm5ggl` (`release_projects_id`),
  KEY `FKiku7m3l37em2cym2fdds537dq` (`release_id`),
  CONSTRAINT `FK8pd5seix3gparnay5at9lbckn` FOREIGN KEY (`release_projects_id`) REFERENCES `ci_release_project` (`id`),
  CONSTRAINT `FKiku7m3l37em2cym2fdds537dq` FOREIGN KEY (`release_id`) REFERENCES `ci_release` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_release_release_projects` */

/*Table structure for table `ci_role` */

DROP TABLE IF EXISTS `ci_role`;

CREATE TABLE `ci_role` (
  `id` int NOT NULL,
  `role_enum` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_role` */

insert  into `ci_role`(`id`,`role_enum`) values 
(3,3);

/*Table structure for table `ci_scm` */

DROP TABLE IF EXISTS `ci_scm`;

CREATE TABLE `ci_scm` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `source_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tag_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `target_branch` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` int DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_scm` */

insert  into `ci_scm`(`id`,`name`,`password`,`source_branch`,`tag_name`,`target_branch`,`type`,`url`,`user`) values 
(1,NULL,'xxxx',NULL,'master',NULL,1,'https://gitee.com/isoftstoneGroup/EazyBuilder.git','develop1');

/*Table structure for table `ci_scm_statistic_job` */

DROP TABLE IF EXISTS `ci_scm_statistic_job`;

CREATE TABLE `ci_scm_statistic_job` (
  `id` int NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `access_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enable` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `repo_type` int DEFAULT NULL,
  `repo_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhla1fu5g3gxdibeseflm6q1h` (`repo_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_scm_statistic_job` */

/*Table structure for table `ci_scm_statistic_record` */

DROP TABLE IF EXISTS `ci_scm_statistic_record`;

CREATE TABLE `ci_scm_statistic_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `err_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `success` bit(1) NOT NULL,
  `job_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe7un06cxw8no8g0dehaemg4pi` (`job_id`),
  CONSTRAINT `FKe7un06cxw8no8g0dehaemg4pi` FOREIGN KEY (`job_id`) REFERENCES `ci_scm_statistic_job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_scm_statistic_record` */

/*Table structure for table `ci_sys_log` */

DROP TABLE IF EXISTS `ci_sys_log`;

CREATE TABLE `ci_sys_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `access_time` datetime(6) DEFAULT NULL,
  `access_url` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `http_method` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `module` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `op_desc` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `op_result` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `op_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remote_addr` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_sys_log` */

insert  into `ci_sys_log`(`id`,`access_time`,`access_url`,`http_method`,`module`,`op_desc`,`op_result`,`op_type`,`remote_addr`,`user_id`,`user_name`) values 
(1,'2022-10-17 16:30:38.194000','/ci/login','POST','auth','登录','success','login','10.10.102.73','1','平台管理员'),
(2,'2022-10-17 16:40:25.772000','/ci/api/systemProperty','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(3,'2022-10-17 16:40:37.129000','/ci/api/systemProperty','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(4,'2022-10-17 16:41:56.069000','/ci/api/systemProperty','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(5,'2022-10-17 16:42:26.567000','/ci/api/systemProperty','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(6,'2022-10-17 16:48:57.260000','/ci/api/user','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(7,'2022-10-17 16:49:39.734000','/ci/api/project-group','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(8,'2022-10-17 16:50:06.783000','/ci/api/project-group','DELETE','persist','删除','success','delete','10.10.102.73','1','平台管理员'),
(9,'2022-10-17 16:53:15.748000','/ci/api/team-resource','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(10,'2022-10-17 16:54:15.049000','/ci/api/team-resource','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(11,'2022-10-17 16:55:22.585000','/ci/api/pipelineProfile','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(12,'2022-10-17 16:58:31.619000','/ci/api/project','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(13,'2022-10-17 16:58:42.666000','/ci/api/team-resource','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(14,'2022-10-17 17:02:23.629000','/ci/api/team-resource','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(15,'2022-10-17 17:02:40.782000','/ci/api/pipeline','POST','pipeline','执行流水线','success','triggerByManual','10.10.102.73','1','平台管理员'),
(16,'2022-10-17 17:13:17.530000','/ci/api/systemProperty','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(17,'2022-10-17 17:19:45.046000','/ci/api/pipelineProfile','POST','persist','保存','success','save','10.10.102.73','1','平台管理员'),
(18,'2022-10-17 17:20:03.730000','/ci/api/pipeline','POST','pipeline','执行流水线','success','triggerByManual','10.10.102.73','1','平台管理员');

/*Table structure for table `ci_team` */

DROP TABLE IF EXISTS `ci_team`;

CREATE TABLE `ci_team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_del` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `check_release_pipeline` bit(1) DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `devops_team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `owner_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `qualityprofiles_json` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sprint_multi_test` bit(1) DEFAULT NULL,
  `team_resource_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgjc7rtju9o1uv5cnlvfonmiys` (`name`),
  KEY `FK8d2diphx6gma5xovpg54df69a` (`team_resource_id`),
  CONSTRAINT `FK8d2diphx6gma5xovpg54df69a` FOREIGN KEY (`team_resource_id`) REFERENCES `ci_team_resource` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team` */

insert  into `ci_team`(`id`,`create_time`,`is_del`,`update_time`,`check_release_pipeline`,`code`,`devops_team_id`,`group_id`,`name`,`owner_id`,`qualityprofiles_json`,`sprint_multi_test`,`team_resource_id`) values 
(1,NULL,0,NULL,NULL,'ci','1',1665997054820,'CI项目组',NULL,NULL,NULL,1);

/*Table structure for table `ci_team_configers` */

DROP TABLE IF EXISTS `ci_team_configers`;

CREATE TABLE `ci_team_configers` (
  `team_id` int NOT NULL,
  `configers_id` int NOT NULL,
  KEY `FKmt8xiubvr8k4j1iengod87c7g` (`configers_id`),
  KEY `FKxeuyi5t6nn2121m7v115yowt` (`team_id`),
  CONSTRAINT `FKmt8xiubvr8k4j1iengod87c7g` FOREIGN KEY (`configers_id`) REFERENCES `ci_user` (`id`),
  CONSTRAINT `FKxeuyi5t6nn2121m7v115yowt` FOREIGN KEY (`team_id`) REFERENCES `ci_team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_configers` */

/*Table structure for table `ci_team_members` */

DROP TABLE IF EXISTS `ci_team_members`;

CREATE TABLE `ci_team_members` (
  `team_id` int NOT NULL,
  `members_id` int NOT NULL,
  KEY `FKqlm0lp0kstohg9y6dwdgropok` (`members_id`),
  KEY `FKtog0hqv08dwvxdc7wb9ulf6ws` (`team_id`),
  CONSTRAINT `FKqlm0lp0kstohg9y6dwdgropok` FOREIGN KEY (`members_id`) REFERENCES `ci_user` (`id`),
  CONSTRAINT `FKtog0hqv08dwvxdc7wb9ulf6ws` FOREIGN KEY (`team_id`) REFERENCES `ci_team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_members` */

insert  into `ci_team_members`(`team_id`,`members_id`) values 
(1,2),
(1,1);

/*Table structure for table `ci_team_namespace` */

DROP TABLE IF EXISTS `ci_team_namespace`;

CREATE TABLE `ci_team_namespace` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `is_del` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `gitlab_api_domain` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `namespace_type` int DEFAULT NULL,
  `remark1` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark2` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark3` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_namespace` */

insert  into `ci_team_namespace`(`id`,`create_time`,`is_del`,`update_time`,`code`,`gitlab_api_domain`,`name`,`namespace_type`,`remark1`,`remark2`,`remark3`) values 
(1,NULL,0,NULL,'test','gitlab-api.eazybuilder.com','测试环境',1,NULL,NULL,NULL),
(2,NULL,0,NULL,'stage','gitlab-api.eazybuilder.com','预生产环境',2,NULL,NULL,NULL),
(3,NULL,0,NULL,'prod','gitlab-api.eazybuilder.com','生产环境',3,NULL,NULL,NULL),
(4,NULL,0,NULL,'dev','gitlab-api.eazybuilder.com','开发环境',0,NULL,NULL,NULL);

/*Table structure for table `ci_team_resource` */

DROP TABLE IF EXISTS `ci_team_resource`;

CREATE TABLE `ci_team_resource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jenkins_k8s_support` int DEFAULT '0',
  `jenkins_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `jenkins_work_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `jenkins_work_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `k8s_yml_path` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `reference_source` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sonar_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_resource` */

insert  into `ci_team_resource`(`id`,`jenkins_k8s_support`,`jenkins_url`,`jenkins_work_path`,`jenkins_work_type`,`k8s_yml_path`,`reference_source`,`sonar_url`,`team_id`,`team_name`) values 
(1,0,'http://es-web.eazybuilder.com/jenkins','jenkins-jenkins-0','pvc',NULL,NULL,NULL,1,'CI项目组');

/*Table structure for table `ci_team_team_thresholds` */

DROP TABLE IF EXISTS `ci_team_team_thresholds`;

CREATE TABLE `ci_team_team_thresholds` (
  `team_id` int NOT NULL,
  `team_thresholds_id` int NOT NULL,
  PRIMARY KEY (`team_id`,`team_thresholds_id`),
  UNIQUE KEY `UK_9w5ypfpg9505q7bjdpqas1uov` (`team_thresholds_id`),
  CONSTRAINT `FK8afggjn0d3i5vlyyqs8mwllcx` FOREIGN KEY (`team_thresholds_id`) REFERENCES `ci_team_threshold` (`id`),
  CONSTRAINT `FKs0qlob2nruo5b83e8ps500bhq` FOREIGN KEY (`team_id`) REFERENCES `ci_team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_team_thresholds` */

/*Table structure for table `ci_team_threshold` */

DROP TABLE IF EXISTS `ci_team_threshold`;

CREATE TABLE `ci_team_threshold` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action_scope` int DEFAULT NULL,
  `blocker_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `input_time` datetime(6) DEFAULT NULL,
  `remark1` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark2` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark3` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `thre_shold_type` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_team_threshold` */

/*Table structure for table `ci_test_detail_result` */

DROP TABLE IF EXISTS `ci_test_detail_result`;

CREATE TABLE `ci_test_detail_result` (
  `id` int NOT NULL AUTO_INCREMENT,
  `execute_order` int DEFAULT NULL,
  `history_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `script_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `total_failed` int DEFAULT NULL,
  `total_pass` int DEFAULT NULL,
  `total_warning` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_detail_result` */

/*Table structure for table `ci_test_env` */

DROP TABLE IF EXISTS `ci_test_env`;

CREATE TABLE `ci_test_env` (
  `id` int NOT NULL AUTO_INCREMENT,
  `env_key` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `env_val` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_env` */

/*Table structure for table `ci_test_envset` */

DROP TABLE IF EXISTS `ci_test_envset`;

CREATE TABLE `ci_test_envset` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `site_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2r2ac80ut2omtnktuf08j0h23` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_envset` */

/*Table structure for table `ci_test_envset_envs` */

DROP TABLE IF EXISTS `ci_test_envset_envs`;

CREATE TABLE `ci_test_envset_envs` (
  `env_set_id` int NOT NULL,
  `envs_id` int NOT NULL,
  UNIQUE KEY `UK_a6niinfx7xrosjupl6q1ktv9c` (`envs_id`),
  KEY `FKr6ar8pl2tn0s1tnh17g2y9lh5` (`env_set_id`),
  CONSTRAINT `FKbvoar850w4ogxhigbpf8n6s9l` FOREIGN KEY (`envs_id`) REFERENCES `ci_test_env` (`id`),
  CONSTRAINT `FKr6ar8pl2tn0s1tnh17g2y9lh5` FOREIGN KEY (`env_set_id`) REFERENCES `ci_test_envset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_envset_envs` */

/*Table structure for table `ci_test_history` */

DROP TABLE IF EXISTS `ci_test_history`;

CREATE TABLE `ci_test_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `end_time` datetime(6) DEFAULT NULL,
  `execute_status` int DEFAULT NULL,
  `plan_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `plan_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remind_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `report_file_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `total_failed` int DEFAULT NULL,
  `total_pass` int DEFAULT NULL,
  `total_warning` int DEFAULT NULL,
  `uid` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5pugu6c4u0c23smcegq07g9nb` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_history` */

/*Table structure for table `ci_test_plan` */

DROP TABLE IF EXISTS `ci_test_plan`;

CREATE TABLE `ci_test_plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cron` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `env_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `execute_type` int DEFAULT NULL,
  `last_trigger` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `next_time` bigint DEFAULT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5203k8ojh5giklm2nxyto1xgh` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_plan` */

/*Table structure for table `ci_test_plan_scripts` */

DROP TABLE IF EXISTS `ci_test_plan_scripts`;

CREATE TABLE `ci_test_plan_scripts` (
  `integrate_test_plan_id` int NOT NULL,
  `scripts_id` int NOT NULL,
  KEY `FKrdo1f3jq666adwx0p83kj5ey7` (`scripts_id`),
  KEY `FKsl5pnkg530s6l5snimxhec4cc` (`integrate_test_plan_id`),
  CONSTRAINT `FKrdo1f3jq666adwx0p83kj5ey7` FOREIGN KEY (`scripts_id`) REFERENCES `ci_atm_script` (`id`),
  CONSTRAINT `FKsl5pnkg530s6l5snimxhec4cc` FOREIGN KEY (`integrate_test_plan_id`) REFERENCES `ci_test_plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_test_plan_scripts` */

/*Table structure for table `ci_user` */

DROP TABLE IF EXISTS `ci_user`;

CREATE TABLE `ci_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_user` */

insert  into `ci_user`(`id`,`department`,`email`,`employee_id`,`name`,`password`,`phone`,`title`) values 
(1,NULL,'admin',NULL,'平台管理员','psQq8qzGivXpROJH+98jX07G8df9TFJiJlfpXNSshQM=',NULL,NULL),
(2,NULL,'user1@eazybuilder.com',NULL,'用户1','0RxyCrqD5DbMmksCPXVfqivf5cGL4GjZ2sU+enuDtS0=','18888888888',NULL);

/*Table structure for table `ci_user_roles` */

DROP TABLE IF EXISTS `ci_user_roles`;

CREATE TABLE `ci_user_roles` (
  `user_id` int NOT NULL,
  `roles_id` int NOT NULL,
  KEY `FK20v3geik05ba7ebf9t7avijvm` (`roles_id`),
  KEY `FK3uhej6bgtv4shgoelroq1lvab` (`user_id`),
  CONSTRAINT `FK20v3geik05ba7ebf9t7avijvm` FOREIGN KEY (`roles_id`) REFERENCES `ci_role` (`id`),
  CONSTRAINT `FK3uhej6bgtv4shgoelroq1lvab` FOREIGN KEY (`user_id`) REFERENCES `ci_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_user_roles` */

insert  into `ci_user_roles`(`user_id`,`roles_id`) values 
(1,3);

/*Table structure for table `ci_user_statistic` */

DROP TABLE IF EXISTS `ci_user_statistic`;

CREATE TABLE `ci_user_statistic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `additions` int NOT NULL,
  `day` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deletions` int NOT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `group_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mergedmrs` int NOT NULL,
  `name_space` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `openedmrs` int NOT NULL,
  `project_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `project_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pushed` int NOT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaha4g9axnctej73wxvj7nglgf` (`day`,`email`,`project_name`,`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_user_statistic` */

/*Table structure for table `ci_warn` */

DROP TABLE IF EXISTS `ci_warn`;

CREATE TABLE `ci_warn` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cron` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `du_liang_type` int DEFAULT NULL,
  `du_liang_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `is_enable` bit(1) DEFAULT NULL,
  `next_time` bigint DEFAULT NULL,
  `warn_type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn` */

/*Table structure for table `ci_warn_receiving_users` */

DROP TABLE IF EXISTS `ci_warn_receiving_users`;

CREATE TABLE `ci_warn_receiving_users` (
  `warn_id` int NOT NULL,
  `receiving_users_id` int NOT NULL,
  PRIMARY KEY (`warn_id`,`receiving_users_id`),
  KEY `FKgpapsb3br4bcpd0udrk9afsii` (`receiving_users_id`),
  CONSTRAINT `FKgpapsb3br4bcpd0udrk9afsii` FOREIGN KEY (`receiving_users_id`) REFERENCES `ci_user` (`id`),
  CONSTRAINT `FKr968jd0ymob47o1l84mm8xvug` FOREIGN KEY (`warn_id`) REFERENCES `ci_warn` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn_receiving_users` */

/*Table structure for table `ci_warn_rule` */

DROP TABLE IF EXISTS `ci_warn_rule`;

CREATE TABLE `ci_warn_rule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `metric_type` int DEFAULT NULL,
  `threshold_max` double DEFAULT NULL,
  `threshold_min` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn_rule` */

/*Table structure for table `ci_warn_scan_groups` */

DROP TABLE IF EXISTS `ci_warn_scan_groups`;

CREATE TABLE `ci_warn_scan_groups` (
  `warn_id` int NOT NULL,
  `scan_groups_id` int NOT NULL,
  PRIMARY KEY (`warn_id`,`scan_groups_id`),
  KEY `FKmkxjcc5hjyhaj49bggrkfb2bh` (`scan_groups_id`),
  CONSTRAINT `FKl54i5kcmixrl629kggxwcosp6` FOREIGN KEY (`warn_id`) REFERENCES `ci_warn` (`id`),
  CONSTRAINT `FKmkxjcc5hjyhaj49bggrkfb2bh` FOREIGN KEY (`scan_groups_id`) REFERENCES `ci_project_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn_scan_groups` */

/*Table structure for table `ci_warn_scan_teams` */

DROP TABLE IF EXISTS `ci_warn_scan_teams`;

CREATE TABLE `ci_warn_scan_teams` (
  `warn_id` int NOT NULL,
  `scan_teams_id` int NOT NULL,
  PRIMARY KEY (`warn_id`,`scan_teams_id`),
  KEY `FKc319yo186qj6ie0fhk858kx57` (`scan_teams_id`),
  CONSTRAINT `FKc319yo186qj6ie0fhk858kx57` FOREIGN KEY (`scan_teams_id`) REFERENCES `ci_team` (`id`),
  CONSTRAINT `FKo4ru0nybi743jy6n0df3jw93c` FOREIGN KEY (`warn_id`) REFERENCES `ci_warn` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn_scan_teams` */

/*Table structure for table `ci_warn_warn_rules` */

DROP TABLE IF EXISTS `ci_warn_warn_rules`;

CREATE TABLE `ci_warn_warn_rules` (
  `warn_id` int NOT NULL,
  `warn_rules_id` int NOT NULL,
  PRIMARY KEY (`warn_id`,`warn_rules_id`),
  KEY `FKg80iu7o8gw5163ln76mvcqdqh` (`warn_rules_id`),
  CONSTRAINT `FKg80iu7o8gw5163ln76mvcqdqh` FOREIGN KEY (`warn_rules_id`) REFERENCES `ci_warn_rule` (`id`),
  CONSTRAINT `FKswi2hrejc0r9wuk4thcwh0ndh` FOREIGN KEY (`warn_id`) REFERENCES `ci_warn` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `ci_warn_warn_rules` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
