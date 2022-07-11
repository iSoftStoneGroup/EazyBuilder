

# Welcome to the EazyBuilder project!

EazyBuilder focuses on multi-team and multi-project unified compilation, build, scan, deployment and other continuous integration automation pipeline requirements, through plug-in automatic injection and decoration, pipeline template, improve traditional continuous integration tools (such as directly using Jenkins/Hudson) configuration is cumbersome and professional High requirements (for example, you must be familiar with Jenkinsfile/pipeline/groovy syntax, etc.), complex environment dependencies (shared library needs to rely on code bases when running), difficult configuration of large-scale projects, and difficult management.

# [中文概述](./README_CN.md)

# Features and Benefits


## Features
- **Flexible and easy to use**: You only need to provide the project SVN/GIT repository information, you can experience the standardization of the whole process from packaging construction, unit testing, coverage scanning, quality vulnerability scanning, component warehouse push, and automatic deployment;
- **Precise reminder**: pipeline process and scan reports, code submission and merging, real-time push via email, DingTalk, etc., and precise subscription configuration;
- **Abundant integration methods**: Support Webhook, timed automatic build, event or integrate and trigger continuous integration pipeline through OpenAPI;
- **Plug-in Enhancement**: Quality, vulnerability scanning, and other extended pipeline steps are based on automated plugin decoration and enhancement technology. There are no additional plugin installation, configuration files, or directory structure requirements for source code projects, and no additional requirements for build outputs. influences;
- **Visual orchestration**: switch-style pipeline visual orchestration method, built-in a large number of processing templates for common CI processes such as build, test, deployment, etc., support rapid customization and expansion of continuous integration pipelines;
- **Elastic Resource Scheduling**: Supports build machine load balancing, K8S elastic scheduling, supports large-scale parallel construction processing and resource scheduling requirements, and makes full use of hardware resources;
- **Mainstream development framework support**: Compatible with mainstream Java, C# and front-end development project code compilation, build and release modes; support Docker image repository, Maven repository and NPM repository push;
- **Project quality monitoring**: comprehensively grasp various engineering quality index data scanned during the operation of the assembly line, can customize quality access control, red light repair reminders, support custom statistical analysis dimensions, and provide online quality reports or emails report.

## Advantage
- **Simple configuration**: The operation steps are simple, no need to be familiar with the pipeline script syntax, visualize the arrangement, and quickly create an efficient pipeline.
- **Non-intrusive integration**: Does not depend on third-party tools, uses modular design, and solves the problem of pipeline sharing and reuse.
- **Enterprise-level security**: Provides fine-grained access control, single sign-on and auditing functions based on roles, records the operation content of each user, and changes the history of the pipeline to meet the security needs of users.
- **Pipeline monitoring and early warning**: Built-in rich early warning methods, comprehensively monitor the pipeline status, actively send abnormal logs, and continuously improve the pipeline through data-driven.
- **Event-driven**: Support wildcards, configure different event types for different code branches, and trigger different pipelines.

EazyBuilder is hosted by the [Cloud Native Computing Foundation (CNCF)](https://cncf.io).

# **Getting Started**


#### Docker installation

- Docker version: 1.13.0+

- EazyBuilder image has been released to [Docker Hub](https://hub.docker.com/ "Docker Hub");
  You can directly use the official image of EazyBuilder
  
- Self-built mirrors

  Generate an image: docker-compose build

- start the app

  docker-compose up -d

  Use default compose file: [dockercompose/docker-compose.yml](./dockercompose/docker-compose.yml)

  

Just visit http://localhost/console/index.html

- Build machine

   The pipeline execution in EazyBuilder depends on jenkins, and the build environment needs to be initialized in jenkins in advance. For details, please refer to [jenkins dockerfile](./eazybuilder/eazybuilder-server/thridparty/jenkins/src/main/docker/Dockerfile)

# **Contribute**

Contributors are welcome to join the EazyBuilder project. Please see [CONTRIBUTING](./CONTRIBUTING_EN.md) to learn how to contribute to this project.


## Scope


### Scope of application

EazyBuilder is designed to solve quick and easy creation of CI pipelines. Therefore, the project will implement or has implemented:

* [Build Resource Management]
* [Visual Pipeline Orchestration]
* [Pipeline shared multiplexing]
* [Pipeline monitoring and warning]
* [event-driven pipeline]
* [Message Alert]
* [OpenAPI integration support]

### Out of range

EazyBuilder will be used with other tools in a cloud-native environment. Therefore, the following specific functions will not be included:

* [Container Resource Management]
* [Quality Protocol Integration]
* [code hosting]
* [automated test]
* [Demand Management]
* [Product Management]

# **Contact us**

- Gitter: EazyBuilder's IM tool for community messaging, collaboration, and discovery.
- Twitter: Follow the latest EazyBuilder news on Twitter.
- Weibo: Follow the latest EazyBuilder news on Weibo (Chinese version of Twitter).
- Email group:

# **Resource**

The latest documentation for each release can be found at:

- [EazyBuilder 1.0.0](./doc/referencebook/v1.0.0/referencebook_en.md)




# **License**

See [license](./LICENSE) for more information.

## Guidelines

We follow the [CNCF Code of Conduct](./CODE_OF_CONDUCT_EN.md).